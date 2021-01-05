package ru.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.topjava.model.Dish;
import ru.topjava.repository.DishRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(restaurantRepository.findEntityById(restaurantId));
        return crudRepository.save(dish);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    @Transactional
    public boolean delete(int id, int restaurantId) {
        return crudRepository.delete(id, restaurantId) != 0;
    }

    @Override
    public Dish get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public Dish get(int id, int restaurantId) {
        return crudRepository.get(id, restaurantId);
    }

    @Override
    public List<Dish> getAll(int restaurantId) {
        return crudRepository.getAll(restaurantId);
    }

    @Override
    public List<Dish> getByName(int restaurantId, String name) {
        return crudRepository.getByName(restaurantId, name == null ? "%" : name.toLowerCase());
    }

    @Override
    public List<Dish> filter(Set<Dish> dishes, int restaurantId) {
        if (!CollectionUtils.isEmpty(dishes)) {
            return crudRepository.filter(dishes.stream().map(dish -> dish.getId()).collect(Collectors.toList()), restaurantId);
        }
        return null;
    }
}
