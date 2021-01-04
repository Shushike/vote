package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.model.Dish;
import ru.topjava.model.Menu;
import ru.topjava.repository.DishRepository;
import ru.topjava.repository.MenuRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService extends RepositoryService<Dish> {

    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;

    public DishService(DishRepository repository, MenuRepository menuRepository) {
        super(repository);
        this.dishRepository = repository;
        this.menuRepository = menuRepository;
    }

    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must not be null");
        checkNew(dish);
        return dishRepository.save(dish, restaurantId);
    }

    public Dish get(int id, int restaurantId) {
        return checkNotFoundWithId(dishRepository.get(id, restaurantId), id);
    }

    public void update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must not be null");
        checkNotFoundWithId(dishRepository.save(dish, restaurantId), dish.id());
    }

    public List<Dish> getAll(int restaurantId) {
        return dishRepository.getAll(restaurantId);
    }

    public List<Dish> getForMenu(int menuId, int restaurantId) {
        final Menu menu = menuRepository.get(menuId);
        checkNotFoundWithId(menu, menuId);
        if (restaurantId != menu.getRestaurant().id())
            return new ArrayList<>();
        return new ArrayList<>(menu.getDishes());
    }

    public boolean delete(int id, int restaurantId) {
        return checkNotFoundWithId(dishRepository.delete(id, restaurantId), id);
    }

    public List<Dish> getByName(int restaurantId, String name) {
        return dishRepository.getByName(restaurantId, name);
    }
}