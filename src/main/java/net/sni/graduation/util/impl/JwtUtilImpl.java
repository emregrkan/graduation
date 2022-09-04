package net.sni.graduation.util.impl;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import net.sni.graduation.constant.AuthorityEnum;
import net.sni.graduation.constant.TokenEnum;
import net.sni.graduation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyStore;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtilImpl implements JwtUtil {

    private final String passphrase;
    private final Key key;
    private final JwtParser jwtParser;

    @Autowired
    public JwtUtilImpl(@Value("${keystore.passphrase}") String passphrase) throws Exception {
        this.passphrase = passphrase;
        this.key = getKeyFromKeystore();
        this.jwtParser = getJwtParser();
    }

    @Override
    public String getUsernameFromToken(String token) throws Exception {
        Claims claims = getAllClaims(token);

        if (claims != null) {
            return claims.getSubject();
        }

        throw new Exception("No claims found to get username");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AuthorityEnum> getAuthoritiesFromToken(String token) throws Exception {
        Claims claims = getAllClaims(token);

        if (claims != null) {
            List<String> authorities = claims.get("authorities", ArrayList.class);

            return authorities.stream()
                    .map(AuthorityEnum::valueOf)
                    .collect(Collectors.toList());
        }

        throw new Exception("No claims found to get authorities");
    }

    @Override
    public Date getCreatedDateFromToken(String token) throws Exception {
        Claims claims = getAllClaims(token);

        if (claims != null) {
            return claims.getIssuedAt();
        }

        throw new Exception("No claims found get creation date");
    }

    @Override
    public Date getExpirationDateFromToken(String token) throws Exception {
        Claims claims = getAllClaims(token);

        if (claims != null) {
            return claims.getExpiration();
        }

        throw new Exception("No claims found to get expiration date");
    }

    @Override
    @SuppressWarnings("unchecked")
    public String generateToken(TokenEnum tokenEnum, UserDetails userDetails, int expireTime) throws Exception {
        if (key != null) {
            Map<String, Object> claims = new HashMap<>(3);

            claims.put("sub", userDetails.getUsername());
            claims.put("iat", new Date(System.currentTimeMillis()));

            if (tokenEnum == TokenEnum.ACCESS) {
                Set<AuthorityEnum> authorities = (Set<AuthorityEnum>) userDetails.getAuthorities();
                claims.put("authorities", authorities);
            }

            Date issuedAt = ((Date) claims.get("iat"));
            Date expirationDate = new Date(issuedAt.getTime() + expireTime);

            return Jwts.builder()
                    .addClaims(claims)
                    .setExpiration(expirationDate)
                    .signWith(key, SignatureAlgorithm.RS512)
                    .compact();
        }

        throw new Exception("Sign key cannot be null");
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) throws Exception {
        return userDetails.getUsername().equals(getUsernameFromToken(token)) && !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) throws Exception {
        Date issuedAt = getExpirationDateFromToken(token);

        if (issuedAt != null) {
            return issuedAt.before(new Date(System.currentTimeMillis()));
        }

        return true;
    }

    private Claims getAllClaims(String token) throws Exception {
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        if (claimsJws == null) {
            throw new Exception("Could not get jws");
        }

        return claimsJws.getBody();
    }

    private Key getKeyFromKeystore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        ClassPathResource keyStoreResource = new ClassPathResource("keystores/tokenkeys.jks");

        if (passphrase == null) {
            throw new Exception("Passphrase cannot be null");
        }

        keyStore.load(keyStoreResource.getInputStream(), passphrase.toCharArray());

        return keyStore.getKey("tokenkeys", passphrase.toCharArray());
    }

    private JwtParser getJwtParser() throws Exception {
        if (key != null) {
            return Jwts.parserBuilder().setSigningKey(key).build();
        }

        throw new Exception("Key cannot be null");
    }
}
