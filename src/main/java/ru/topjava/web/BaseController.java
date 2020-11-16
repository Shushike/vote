package ru.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.topjava.model.AbstractBaseEntity;
import ru.topjava.model.Restaurant;
import ru.topjava.service.RepositoryService;

import static ru.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.util.ValidationUtil.checkRights;

public abstract class BaseController<T extends AbstractBaseEntity> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final RepositoryService<T> service;

    public BaseController(RepositoryService<T> service){
        this.service = service;
    }

    public T get(int id) {
        //todo: добавить класс объекта
        log.info("get #{}",  id);
        return service.get(id);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        checkRights(SecurityUtil.authUserIsAdmin());
        log.info("delete restaurant #{} by user #{}", id, userId);
        service.delete(id);
    }
}
