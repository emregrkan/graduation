package net.sni.graduation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum AuthorityEnum implements GrantedAuthority {
    USER(new SimpleGrantedAuthority("USER")), ADMIN(new SimpleGrantedAuthority("ADMIN"));

    private final GrantedAuthority grantedAuthority;

    @Override
    public String getAuthority() {
        return grantedAuthority.getAuthority();
    }
}
