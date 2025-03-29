package org.example.socialnetworkjavafx.Infrastructure;

import org.example.socialnetworkjavafx.Entities.Entity;
import org.example.socialnetworkjavafx.Repositories.Repository;
import org.example.socialnetworkjavafx.Validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private Validator<E> validator;
    protected Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        this.entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        E found = entities.get(id);
        if (found != null) {
            return Optional.of(found);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        validator.validate(entity);
        entities.put(entity.getId(), entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<E> delete(ID id) {
        E entity = entities.remove(id);
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<E> update(E entity) {
        entities.put(entity.getId(), entity);
        return Optional.of(entity);
    }

    public static class FriendshipDbRepository {
    }
}
