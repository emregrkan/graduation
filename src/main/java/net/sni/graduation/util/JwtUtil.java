package net.sni.graduation.util;

import net.sni.graduation.constant.AuthorityEnum;
import net.sni.graduation.constant.TokenEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
public interface JwtUtil {
    String getUsernameFromToken(String token) throws Exception;

    List<AuthorityEnum> getAuthoritiesFromToken(String token) throws Exception;

    Date getCreatedDateFromToken(String token) throws Exception;

    Date getExpirationDateFromToken(String token) throws Exception;

    String generateToken(TokenEnum tokenEnum, UserDetails userDetails, int expireTime) throws Exception;

    Boolean validateToken(String token, UserDetails userDetails) throws Exception;
}
