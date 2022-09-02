package net.sni.graduation.event;

import lombok.RequiredArgsConstructor;
import net.sni.graduation.constant.RoleEnum;
import net.sni.graduation.entity.Role;
import net.sni.graduation.entity.User;
import net.sni.graduation.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEventHandler {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @HandleBeforeCreate
    public void hashUserPassword(User user) {
        Optional<Role> userRole = roleRepository.findByRole(RoleEnum.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRole.ifPresent(role -> user.getRoles().add(role));
    }
}
