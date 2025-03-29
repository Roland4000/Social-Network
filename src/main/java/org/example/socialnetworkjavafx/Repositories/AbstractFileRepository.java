package org.example.socialnetworkjavafx.Repositories;

import org.example.socialnetworkjavafx.Entities.Entity;
import org.example.socialnetworkjavafx.Infrastructure.InMemoryRepository;
import org.example.socialnetworkjavafx.Validators.Validator;

import java.io.FileNotFoundException;
import java.util.Optional;

abstract public class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    protected String filePath;

    public AbstractFileRepository(String filePath, Validator<E> validator) {
        super(validator);
        this.filePath = filePath;
    }

    protected abstract void readFromFile() throws FileNotFoundException;
    protected abstract void writeToFile() throws FileNotFoundException;

    @Override
    public Optional<E> update(E entity) {
        return super.update(entity);
    }

    @Override
    public Optional<E> delete(ID id) {
        return super.delete(id);
    }

    @Override
    public Optional<E> save(E entity) {
        return super.save(entity);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<E> findOne(ID id) {
        return super.findOne(id);
    }

    public AbstractFileRepository(Validator<E> validator) {
        super(validator);
    }
}
