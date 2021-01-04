package ru.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Menu;
import ru.topjava.repository.DishRepository;
import ru.topjava.repository.MenuRepository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class DataJpaMenuRepository implements MenuRepository {

    private final CrudMenuRepository crudRepository;
    private final CrudRestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    public DataJpaMenuRepository(CrudMenuRepository crudRepository, CrudRestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.crudRepository = crudRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public Menu save(Menu menu, int restaurantId) {
        menu.setRestaurant(restaurantRepository.findEntityById(restaurantId));
        menu.setDishes(dishRepository.filter(menu.getDishes(), restaurantId));
        return crudRepository.save(menu);
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
    public Menu get(int id) {
        return crudRepository.get(id);
    }

    @Override
    public List<Menu> getAll(int restaurantId) {
        return crudRepository.getAllByRestaurant(restaurantId);
    }

    @Override
    public List<Menu> getAll() {
        return crudRepository.getAll();
    }

    @Override
    public Menu getByDate(int restaurantId, LocalDate localDate) {
        List<Menu> tempList = crudRepository.getByDate(restaurantId, localDate);
        return tempList!=null && !tempList.isEmpty() ? tempList.get(0): null;
    }

    @Override
    public List<Menu> getAllByRestaurant(int restaurantId) {
        return crudRepository.getAllByRestaurant(restaurantId);
    }

    @Override
    public List<Menu> getAllByDate(LocalDate localDate) {
        return crudRepository.getAllByDate(localDate);
    }

    @Override
    public List<Menu> getBetweenInclude(LocalDate startDate, LocalDate endDate) {
        return crudRepository.getBetweenInclude(startDate, endDate);
    }

    @Override
    public List<Menu> getVotedBetweenDateForUser(LocalDate startDate, LocalDate endDate, int userId) {
        return crudRepository.getVotedBetweenDateForUser(startDate, endDate, userId);
    }

    @Override
    public List<IVotesNumber> getVoteNumberByDate(LocalDate localDate) {
        return crudRepository.getNumbersByDate(localDate);
    }

    @Override
    public List<IVotesNumber> getVoteNumbers(LocalDate startDate, LocalDate endDate) {
        return crudRepository.getVoteNumbers(startDate, endDate);
    }

    @Override
    public Menu get(int id, int restaurantId) {
        return crudRepository.get(id, restaurantId);
    }
}
