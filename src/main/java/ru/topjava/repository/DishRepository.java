package ru.topjava.repository;

import ru.topjava.model.Dish;

import java.util.List;

public interface DishRepository extends BaseRepository<Dish> {
    // null if not found, when updated
    Dish save(Dish dish, int restaurantId);

    List<Dish> getAll(int restaurantId);

    List<Dish> getByName(int restaurantId, String name);
}