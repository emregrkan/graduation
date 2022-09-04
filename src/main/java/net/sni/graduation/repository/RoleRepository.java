package net.sni.graduation.repository;

import net.sni.graduation.constant.AuthorityEnum;
import net.sni.graduation.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends ReadOnlyRepository<Role, Long> {
    Optional<Role> findByAuthority(AuthorityEnum authority);
}
