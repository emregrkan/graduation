package net.sni.graduation.event;

import lombok.RequiredArgsConstructor;
import net.sni.graduation.constant.AuthorityEnum;
import net.sni.graduation.entity.User;
import net.sni.graduation.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEventHandler {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @HandleBeforeCreate
    @SuppressWarnings("unused")
    public void hashUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        roleRepository.findByAuthority(AuthorityEnum.USER)
                .ifPresent(role -> user.getRoles().add(role));
    }
}
