package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.model.Menu;
import ru.topjava.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService extends RepositoryService<Menu> {

    private final MenuRepository menuRepository;
    public MenuService(MenuRepository repository) {
        super(repository);
        menuRepository = repository;
    }

    public Menu create(Menu menu, int restaurantId) {
        Assert.notNull(menu, "Menu must not be null");
        checkNew(menu);
        return menuRepository.save(menu, restaurantId);
    }

    public void update(Menu menu, int restaurantId) {
        Assert.notNull(menu, "Menu must not be null");
        checkNotFoundWithId(menuRepository.save(menu, restaurantId), menu.id());
    }

    public List<Menu> getAll(int restaurantId) {
        return menuRepository.getAll(restaurantId);
    }

    public List<Menu> getAllByDate(LocalDate localDate) {
        return menuRepository.getAllByDate(localDate);
    }

    public Menu getByDate(int restaurantId, LocalDate localDate) {
        return menuRepository.getByDate(restaurantId, localDate);
    }

    public List<Menu> getAll() {
        return menuRepository.getAll();
    }
}