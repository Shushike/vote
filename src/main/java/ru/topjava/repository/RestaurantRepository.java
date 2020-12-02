package ru.topjava.repository;

import ru.topjava.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository extends BaseRepository<Restaurant> {
    // null if not found, when updated
    Restaurant save(Restaurant restaurant);

    // null if not found
    List<Restaurant> getByAddress(String address);

    List<Restaurant> getAll();

    List<Restaurant> getAllVoted(int userId);

    int getVoteCount(int restaurantId, LocalDate localDate);

    Restaurant getWithMenu(int restaurantId);

    Restaurant getWithDishes(int restaurantId);

    Restaurant getWholeInfo(int restaurantId);
}