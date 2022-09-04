package net.sni.graduation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sni.graduation.constant.TokenEnum;
import net.sni.graduation.service.UserDetailsService;
import net.sni.graduation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(path = "/api/v1/refresh", method = RequestMethod.POST)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring(7);
                String username = jwtUtil.getUsernameFromToken(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                String accessToken = jwtUtil.generateToken(TokenEnum.ACCESS, userDetails, 10 * 60 * 1000);

                Map<String, String> tokens = new HashMap<>(2) {{
                    put("access_token", accessToken);
                    put("refresh_token", refreshToken);
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
