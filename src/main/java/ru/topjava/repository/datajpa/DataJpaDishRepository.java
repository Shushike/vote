package ru.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Dish;
import ru.topjava.repository.DishRepository;

import java.util.List;

@Repository
public class DataJpaDishRepository implements DishRepository {

    private final CrudDishRepository crudRepository;
    private final CrudRestaurantRepository restaurantRepository;

    public DataJpaDishRepository(CrudDishRepository crudRepository, CrudRestaurantRepository restaurantRepository) {
        this.crudRepository = crudRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return crudRepository.save(dish);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Dish get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public List<Dish> getAll(int restaurantId) {
        return crudRepository.getAll(restaurantId);
    }

    @Override
    public List<Dish> getByName(int restaurantId, String name) {
        return null;
    }
}
