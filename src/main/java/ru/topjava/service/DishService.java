package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.model.Dish;
import ru.topjava.model.Restaurant;
import ru.topjava.repository.DishRepository;
import ru.topjava.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService extends RepositoryService<Dish> {

    private final DishRepository dishRepository;
    public DishService(DishRepository repository) {
        super(repository);
        dishRepository = repository;
    }

    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must not be null");
        checkNew(dish);
        return dishRepository.save(dish, restaurantId);
    }

    public void update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must not be null");
        checkNotFoundWithId(dishRepository.save(dish, restaurantId), dish.id());
    }

    public List<Dish> getAll(int restaurantId) {
        return dishRepository.getAll(restaurantId);
    }

    public List<Dish> getByName(int restaurantId, String name) {
        return dishRepository.getByName(restaurantId, name);
    }
}