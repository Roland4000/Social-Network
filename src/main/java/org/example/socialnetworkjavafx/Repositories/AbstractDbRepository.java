package org.example.socialnetworkjavafx.Repositories;

import org.example.socialnetworkjavafx.Entities.Entity;

import java.sql.Connection;
import java.util.Optional;

public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements PagingRepository<ID, E> {
    protected Connection connection;

    public AbstractDbRepository(Connection connection) {
        this.connection = connection;
    };

    @Override
    public Optional<E> findOne(ID id) {
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        return null;
    }

    @Override
    public Optional<E> save(E entity) {
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        return Optional.empty();
    }

    @Override
    public Optional<E> update(E entity) {
        return Optional.empty();
    }
}
