package ru.topjava.repository;

import ru.topjava.model.Menu;

import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> getAll(int restaurantId);
}