package ru.topjava.repository;

import ru.topjava.model.Dish;

import java.util.List;
import java.util.Set;

public interface DishRepository extends BaseRepository<Dish> {
    // null if not found, when updated
    Dish save(Dish dish, int restaurantId);

    List<Dish> getAll(int restaurantId);

    List<Dish> getByName(int restaurantId, String name);

    Dish get(int id, int restaurantId);

    boolean delete(int id, int restaurantId);

    List<Dish> filter(Set<Dish> dishes, int restaurantId);
}