package ru.topjava.service;

import org.springframework.util.Assert;
import ru.topjava.model.AbstractBaseEntity;
import ru.topjava.repository.BaseRepository;

import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

public abstract class RepositoryService<T extends AbstractBaseEntity> {

    protected final BaseRepository<T> repository;

    public RepositoryService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    public T get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

}
