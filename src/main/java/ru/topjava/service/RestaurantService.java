package ru.topjava.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.model.Restaurant;
import ru.topjava.repository.datajpa.CrudRestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.util.DateUtil.*;
import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService extends RepositoryService<Restaurant> {

    private final CrudRestaurantRepository crudRepository;

    public RestaurantService(CrudRestaurantRepository crudRepository) {
        super(crudRepository);
        this.crudRepository = crudRepository;
    }

    @CachePut(value = "restaurants")
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not be null");
        checkNew(restaurant);
        return crudRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants")
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not be null");
        checkNotFoundWithId(crudRepository.save(restaurant), restaurant.id());
    }

    @Override
    @CacheEvict(value = "restaurants")
    public boolean delete(int id) {
        return checkNotFoundWithId(crudRepository.delete(id)!=0, id);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return crudRepository.findAll(Sort.by("name"));
    }

    public List<Restaurant> getByAddress(String address) {
        return crudRepository.getByAddress(address);
    }

    public List<Restaurant> getAllVoted(int userId) {
        return crudRepository.getAllVoted(userId);
    }

    public Restaurant getWithMenu(int restaurantId) {
        List<Restaurant> list = crudRepository.getWithMenu(restaurantId);
        return DataAccessUtils.singleResult(list);
    }

    public Restaurant getWithDishes(int restaurantId) {
        List<Restaurant> list = crudRepository.getWithDishes(restaurantId);
        return DataAccessUtils.singleResult(list);
    }

    public List<Restaurant> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return crudRepository.getBetween(atDayOrMin(startDate), atDayOrMax(endDate));
    }

    public Restaurant getWholeInfo(int restaurantId) {
        List<Restaurant> list = crudRepository.getWholeInfo(restaurantId);
        return DataAccessUtils.singleResult(list);
    }

    public int getVotesNumber(int restaurantId, LocalDate voteDate) {
        return crudRepository.getVotesNumber(restaurantId, voteDate);
    }
}