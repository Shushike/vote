package ru.topjava.repository;

import ru.topjava.model.User;

import java.util.List;

public interface UserRepository extends BaseRepository<User> {
    // null if not found, when updated
    User save(User user);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();

    default User getWithVotes(int id) {
        throw new UnsupportedOperationException();
    }
}