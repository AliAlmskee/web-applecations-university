package com.main.core.repository;


import jakarta.persistence.LockModeType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.ArrayList;
import java.util.List;


@NoRepositoryBean
public abstract interface BaseRepositoryParent<T>
        extends JpaRepository<T, Long> {

    public default T findOne(Long id) {
        return findById(id)
                .orElse(null);
    }

    public default <S extends T> List<S> save(Iterable<S> entities) {
        return saveAll(entities);
    }

    public default <S extends T> List<S> saveAndFlush(Iterable<S> entities) {
        return saveAllAndFlush(entities);
    }

    public default <S extends T> List<S> saveAndIgnoreDuplication(Iterable<S> entities) {
        List<S> saved = new ArrayList<>();
        entities.forEach(entity -> {
            S savedEntity = saveAndIgnoreDuplication(entity);
            if (savedEntity != null) {
                saved.add(savedEntity);
            }
        });
        return saved;
    }

    public default <S extends T> S saveAndIgnoreDuplication(S entity) {
        S result = null;
        try {
            result  = save(entity);
        } catch (DataIntegrityViolationException ex) {
            // ignore reapeated entity
        }
        return result;
    }

    public default T silentSave(T entity) {
        return save(entity);
    }

    public default void delete(Iterable<? extends T> entities) {
        deleteAll(entities);
    }

    public default void delete(Long id) {
        deleteById(id);
    }

    default void silentDelete(Iterable<? extends T> entities) {
        deleteAll(entities);
    }

    default void silentDelete(Long id) {
        deleteById(id);
    }

    public default List<T> findAll(Iterable<Long> ids) {
        return findAllById(ids);
    }

    public default boolean exists(Long id) {
        return existsById(id);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public T findWithLockingById(Long id);
}
