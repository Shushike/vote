package ru.topjava.repository;

import ru.topjava.model.Menu;
import ru.topjava.repository.datajpa.IMenuVote;
import ru.topjava.repository.datajpa.IVotesNumber;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {
    // null if not found, when updated
    Menu save(Menu menu, int restaurantId);

    boolean delete(int id, int restaurantId);

    List<Menu> getAll(int restaurantId);

    /**
     * Return menu list for all restaurants without dishes and restaurant.
     * Ordered by descending menu date and ascending ID
     */
    List<Menu> getAll();

    /**
     * Return menu with dish list for date and restaurant.
     * Null if not found
     */
    Menu getByDate(int restaurantId, LocalDate localDate);

    /**
     * Return list of menu with dishes for date in all restaurants.
     * Ordered by restaurant ID
     */
    List<Menu> getAllByDate(LocalDate localDate);

    List<Menu> getAllByRestaurant(int restaurantId);

    List<Menu> getBetweenInclude(LocalDate startDate, LocalDate endDate);

    List<Menu> getVotedBetweenDateForUser(LocalDate startDate, LocalDate endDate, int userId);

    List<IVotesNumber> getVoteNumberByDate(LocalDate localDate);

    List<IVotesNumber> getVoteNumbers(LocalDate startDate, LocalDate endDate);

    List<IMenuVote> getBetweenIncludeWithUserVote(LocalDate startDate, LocalDate endDate, int userId);

    Menu get(int id, int restaurantId);
}