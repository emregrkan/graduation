package net.sni.graduation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sni.graduation.constant.TokenEnum;
import net.sni.graduation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        return super.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        try {
            UserDetails userDetails = (UserDetails) authResult.getPrincipal();

            String accessToken = jwtUtil.generateToken(TokenEnum.ACCESS, userDetails, 10 * 60 * 1000);
            String refreshToken = jwtUtil.generateToken(TokenEnum.REFRESH, userDetails, 30 * 60 * 1000);

            Map<String, String> tokens = new HashMap<>(2) {{
                put("accessToken", accessToken);
                put("refreshToken", refreshToken);
            }};

            response.setContentType("application/json");
            objectMapper.writeValue(response.getOutputStream(), tokens);
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new RuntimeException(e);
        }
    }
}
