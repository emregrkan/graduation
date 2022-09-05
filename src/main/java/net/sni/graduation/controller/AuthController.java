package net.sni.graduation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sni.graduation.constant.TokenEnum;
import net.sni.graduation.service.UserDetailsService;
import net.sni.graduation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/api/v1/auth/refresh", method = RequestMethod.GET)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("refresh_token")).findAny();

        if (refreshTokenCookie.isPresent()) {
            try {
                String refreshToken = refreshTokenCookie.get().getValue();
                String email = jwtUtil.getEmailFromToken(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                String accessToken = jwtUtil.generateToken(TokenEnum.ACCESS, userDetails, 10 * 60 * 1000);

                Map<String, String> tokens = new HashMap<>(1) {{
                    put("accessToken", accessToken);
                }};

                response.setContentType("application/json");
                objectMapper.writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                log.error("Exception:", e);

                response.setStatus(403);
                response.setContentType("application/json");
                objectMapper.writeValue(response.getOutputStream(), Map.of("error", e.getMessage()));
            }
        }
    }
}
