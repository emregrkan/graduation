package net.sni.graduation.service;

import lombok.RequiredArgsConstructor;
import net.sni.graduation.entity.User;
import net.sni.graduation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> foundUser = userRepository.findByEmail(email);

        return foundUser.orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s is not found", email)));

    }
}
