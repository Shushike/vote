package ru.topjava.repository;

import ru.topjava.model.Dish;
import ru.topjava.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {
    // null if not found, when updated
    Menu save(Menu menu, int restaurantId);

    List<Menu> getAll(int restaurantId);

    List<Menu> getAll();
    // null if not found
    Menu getByDate(int restaurantId, LocalDate localDate);

    List<Menu> getAllByDate(LocalDate localDate);

    List<Menu> getAllByRestaurant(int restaurantId);

    List<Menu> getBetweenInclude(LocalDate startDate, LocalDate endDate);
}