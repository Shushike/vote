package ru.topjava.repository;

import ru.topjava.model.Vote;
import ru.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {
    /**
     * Create or update vote.
     * Null if not found, when updated
     */
    Vote save(Vote vote, int menuId, int userId);

    /**
     * Create new vote for menu and user with current time.
     */
    Vote create(int menuId, int userId) throws NotFoundException;

    Vote get(int menuId, int userId);

    Vote getById(int id, int userId);

    /**
     * Return vote of user for menu on menuDate
     * with menu
     */
    Vote getByDate(int userId, LocalDate menuDate);

    boolean delete(int id);

    boolean delete(int id, int userId);

    // false if not found
    boolean deleteForMenu(int menuId, int userId);

    /**
     * Return list of votes with menus
     * ordered by descending vote time
     */
    List<Vote> getAllForUser(int userId);

    /**
     * Return list of votes with users.
     * ordered by descending vote time
     */
    List<Vote> getAllForMenu(int menuId);

    boolean menuHasVotes(int menuId);
}