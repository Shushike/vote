package ru.topjava.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.model.Restaurant;
import ru.topjava.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService extends RepositoryService<Restaurant> {

    private final RestaurantRepository restaurantRepository;
    public RestaurantService(RestaurantRepository repository) {
        super(repository);
        restaurantRepository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not be null");
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not be null");
        checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id());
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.getAll();
    }

    public List<Restaurant> getByAddress(String address) {
        return restaurantRepository.getByAddress(address);
    }

    public List<Restaurant> getAllVoted(int userId) {
        return restaurantRepository.getAllVoted(userId);
    }

    public int getVoteCount(int restaurantId, LocalDate voteDate){
        return restaurantRepository.getVoteCount(restaurantId, voteDate);
    }
}