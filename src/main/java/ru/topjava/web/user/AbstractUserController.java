package ru.topjava.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.topjava.HasId;
import ru.topjava.View;
import ru.topjava.model.User;
import ru.topjava.service.UserService;
import ru.topjava.to.UserTo;
import ru.topjava.util.UserUtil;

import java.util.List;

import static ru.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService service;

    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public List<User> getAll() {
        log.info("Get all users");
        return service.getAll();
    }

    public User get(int id) {
        log.info("Get user #{}", id);
        return service.get(id);
    }

    public User create(UserTo userTo) {
        log.info("Create from TO {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public User create(User user) {
        log.info("Create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("Delete user #{}", id);
        service.delete(id);
    }

    public void update(UserTo userTo, int id) {
        log.info("Update {} with ID={}", userTo, id);
        service.update(userTo);
    }

    public User getByMail(String email) {
        log.info("Get user by email {}", email);
        return service.getByEmail(email);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "Set enable #{}" : "Set disable #{}", id);
        service.enable(id, enabled);
    }

    public User getWithVotes(int id) {
        log.info("Get user #{} with vote data", id);
        return service.getWithVotes(id);
    }

    protected void validateBeforeUpdate(HasId user, int id) throws BindException {
        assureIdConsistent(user, id);
        DataBinder binder = new DataBinder(user);
        binder.addValidators(emailValidator, validator);
        binder.validate(View.Web.class);
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}