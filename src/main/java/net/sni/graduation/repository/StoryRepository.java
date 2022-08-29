package net.sni.graduation.repository;

import net.sni.graduation.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface StoryRepository extends JpaRepository<Story, UUID> {
}
