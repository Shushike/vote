package ru.topjava.service;

import ru.topjava.model.AbstractBaseEntity;
import ru.topjava.repository.BaseRepository;

import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

public abstract class RepositoryService<T extends AbstractBaseEntity> {

    protected final BaseRepository<T> repository;

    public RepositoryService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    public T get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public boolean delete(int id) {
        return checkNotFoundWithId(repository.delete(id) != 0, id);
    }

}
