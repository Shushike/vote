package ru.topjava.web.user;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.topjava.HasIdAndEmail;
import ru.topjava.model.User;
import ru.topjava.repository.datajpa.CrudUserRepository;

@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    private final CrudUserRepository repository;

    public UniqueMailValidator(CrudUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            User dbUser = repository.getByEmail(user.getEmail().toLowerCase());
            if (dbUser != null && !dbUser.getId().equals(user.getId())) {
                errors.rejectValue("email", "User with this email already exists");
            }
        }
    }
}
