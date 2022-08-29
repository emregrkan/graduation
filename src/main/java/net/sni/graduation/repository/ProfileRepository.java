package net.sni.graduation.repository;

import net.sni.graduation.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
}
