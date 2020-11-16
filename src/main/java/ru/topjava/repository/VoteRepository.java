package ru.topjava.repository;

import ru.topjava.model.Vote;

import java.util.List;

public interface VoteRepository{
    // null if not found, when updated
    Vote save(Vote vote);

    // false if not found
    boolean delete(int menuId, int userId);

    // null if not found
    Vote get(int menuId, int userId);

    List<Vote> getAllForUser(int userId);

    List<Vote> getAllForMenu(int menuId);
}