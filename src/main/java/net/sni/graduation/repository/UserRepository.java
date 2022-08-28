package net.sni.graduation.repository;

import net.sni.graduation.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
