package ru.topjava.repository;

import ru.topjava.model.Dish;
import ru.topjava.model.Vote;

import java.util.List;

public interface VoteRepository extends BaseRepository<Vote> {
    // null if not found, when updated
    Vote save(Vote vote, int menuId, int userId);

    Vote create(int menuId, int userId);

    Vote get(int menuId, int userId);

    // false if not found
    boolean delete(int menuId, int userId);

    List<Vote> getAllForUser(int userId);

    List<Vote> getAllForMenu(int menuId);
}