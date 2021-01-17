package ru.topjava.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.AuthorizedUser;
import ru.topjava.model.User;
import ru.topjava.repository.datajpa.CrudUserRepository;
import ru.topjava.to.UserTo;
import ru.topjava.util.UserUtil;

import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNotFound;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService extends RepositoryService<User> implements UserDetailsService {

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");
    private final CrudUserRepository crudRepository;
    private final PasswordEncoder passwordEncoder;
    private final String NOT_NULL_MSG = "User must not be null";

    public UserService(CrudUserRepository crudRepository, PasswordEncoder passwordEncoder) {
        super(crudRepository);
        this.crudRepository = crudRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        Assert.notNull(user, NOT_NULL_MSG);
        return crudRepository.save(user);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(crudRepository.getByEmail(email), "Failed to find user by email=" + email);
    }

    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }

    public void update(User user) {
        Assert.notNull(user, NOT_NULL_MSG);
        checkNotFoundWithId(crudRepository.save(user), user.id());
    }

    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.getId());
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user) {
        return crudRepository.save(UserUtil.prepareToSave(user, passwordEncoder));
    }

    public User getWithVotes(int id) {
        return checkNotFoundWithId(crudRepository.getWithVotes(id), id);
    }
}