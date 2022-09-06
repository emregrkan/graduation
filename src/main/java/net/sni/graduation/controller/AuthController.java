package net.sni.graduation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sni.graduation.constant.TokenEnum;
import net.sni.graduation.service.UserDetailsService;
import net.sni.graduation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/api/v1/auth/refresh", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> refreshToken(@Nullable @CookieValue("refresh_token") String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            try {
                String email = jwtUtil.getEmailFromToken(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String accessToken = jwtUtil.generateToken(TokenEnum.ACCESS, userDetails, 10 * 60 * 1000);

                response.setStatus(200);
                return Map.of("accessToken", accessToken);
            } catch (Exception e) {
                log.error("Exception:", e);
                response.setStatus(403);
                return Map.of("error", e.getMessage());
            }
        }

        response.setStatus(401);
        return Map.of("error", "No token provided");
    }

    @RequestMapping(path = "/api/v1/auth/me", method = RequestMethod.GET)
    public @ResponseBody UserDetails getAuthenticatedUser(@Nullable @RequestHeader("Authorization") String authorizationHeader, HttpServletResponse response) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                String email = jwtUtil.getEmailFromToken(token);

                response.setStatus(200);
                return userDetailsService.loadUserByUsername(email);
            } catch (Exception e) {
                log.error("Exception:", e);
                response.setStatus(403);
                return null;
            }
        }

        response.setStatus(401);
        return null;
    }
}
