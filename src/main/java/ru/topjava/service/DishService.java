package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.model.Dish;
import ru.topjava.model.Menu;
import ru.topjava.repository.datajpa.CrudDishRepository;
import ru.topjava.repository.datajpa.CrudMenuRepository;
import ru.topjava.repository.datajpa.CrudRestaurantRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService extends RepositoryService<Dish> {

    private final CrudDishRepository crudRepository;
    private final CrudRestaurantRepository restaurantRepository;
    private final CrudMenuRepository menuRepository;

    public DishService(CrudDishRepository crudRepository, CrudRestaurantRepository restaurantRepository,
                       CrudMenuRepository menuRepository) {
        super(crudRepository);
        this.crudRepository = crudRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must not be null");
        checkNew(dish);
        return save(dish, restaurantId);
    }

    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(restaurantRepository.findEntityById(restaurantId));
        return crudRepository.save(dish);
    }

    public boolean delete(int id, int restaurantId) {
        return checkNotFoundWithId(crudRepository.delete(id, restaurantId) != 0, id);
    }

    public Dish get(int id, int restaurantId) {
        return checkNotFoundWithId(crudRepository.get(id, restaurantId), id);
    }

    public void update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must not be null");
        checkNotFoundWithId(save(dish, restaurantId), dish.id());
    }

    public List<Dish> getAll(int restaurantId) {
        return crudRepository.getAll(restaurantId);
    }

    public List<Dish> getForMenu(int menuId, int restaurantId) {
        final Menu menu = menuRepository.get(menuId);
        checkNotFoundWithId(menu, menuId);
        if (restaurantId != menu.getRestaurant().id())
            return new ArrayList<>();
        return new ArrayList<>(menu.getDishes());
    }

    public List<Dish> getByName(int restaurantId, String name) {
        return crudRepository.getByName(restaurantId, name == null ? "%" : name.toLowerCase());
    }
}