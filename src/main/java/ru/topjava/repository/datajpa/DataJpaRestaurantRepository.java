package ru.topjava.repository.datajpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Restaurant;
import ru.topjava.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {

    private final CrudRestaurantRepository crudRepository;

    public DataJpaRestaurantRepository(CrudRestaurantRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return crudRepository.save(restaurant);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Restaurant get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public List<Restaurant> getByAddress(String address) {
        return crudRepository.getByAddress(address);
    }

    @Override
    public List<Restaurant> getAll() {
        return crudRepository.findAll(SORT_NAME);
    }

    @Override
    public List<Restaurant> getAllVoted(int userId) {
        return crudRepository.getAllVoited(userId);
    }

    @Override
    public Restaurant getWithMenu(int restaurantId) {
        List<Restaurant> list = crudRepository.getWithMenu(restaurantId);
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public Restaurant getWithDishes(int restaurantId) {
        List<Restaurant> list = crudRepository.getWithDishes(restaurantId);
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public Restaurant getWholeInfo(int restaurantId) {
        List<Restaurant> list = crudRepository.getWholeInfo(restaurantId);
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public int getVoteCount(int restaurantId, LocalDate localDate) {
        return crudRepository.getVoteCount(restaurantId, localDate);
    }
}
