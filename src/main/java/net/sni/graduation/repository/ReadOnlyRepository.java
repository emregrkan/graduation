package net.sni.graduation.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

@NoRepositoryBean
@SuppressWarnings("unused")
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
    Optional<T> findById(ID id);
    Collection<T> findAll();
}
