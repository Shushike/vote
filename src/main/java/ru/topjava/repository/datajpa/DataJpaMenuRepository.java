package ru.topjava.repository.datajpa;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Dish;
import ru.topjava.model.Menu;
import ru.topjava.repository.DishRepository;
import ru.topjava.repository.MenuRepository;
import ru.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class DataJpaMenuRepository implements MenuRepository {

    private final CrudMenuRepository crudRepository;
    private final CrudRestaurantRepository restaurantRepository;

    public DataJpaMenuRepository(CrudMenuRepository crudRepository, CrudRestaurantRepository restaurantRepository) {
        this.crudRepository = crudRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public Menu save(Menu menu, int restaurantId) {
        menu.setRestaurant(restaurantRepository.findEntityById(restaurantId));
        //todo: фильтровать dish по принадлежности к ресторану
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
