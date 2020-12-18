package ru.topjava.service.datajpa;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.model.Restaurant;
import ru.topjava.util.exception.NotFoundException;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.AbstractServiceTest;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static ru.topjava.RestaurantTestData.*;
import static ru.topjava.UserTestData.USER_ID;

public class RestaurantServiceTest extends AbstractServiceTest {
    private static final Logger innerLog = LoggerFactory.getLogger(RestaurantServiceTest.class);

    @Autowired
    protected RestaurantService service;

    @Test
    public void delete() {
        service.delete(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT1_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void create() {
        Restaurant created = service.create(getNew());
        int newId = created.id();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    public void get() {
        Restaurant actual = service.get(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(actual, restaurant1);
        innerLog.debug("Actual restaurant: "+actual.toString());
    }

    @Test
    public void getWithMenu() {
        Restaurant actual = service.getWithMenu(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(actual, restaurant1);
        innerLog.debug("Actual restaurant: "+actual.toString());
    }

    @Test
    public void getWithDishes() {
        Restaurant actual = service.getWithDishes(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(actual, restaurant1);
        innerLog.debug("Actual restaurant: "+actual.toString());
    }

    @Test
    public void getWholeInfo() {
        Restaurant actual = service.getWholeInfo(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(actual, restaurant1);
        innerLog.debug("Actual restaurant: "+actual.toString());
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void update() {
        Restaurant updated = getUpdated();
        service.update(updated);
        RESTAURANT_MATCHER.assertMatch(service.get(RESTAURANT1_ID), getUpdated());
    }

    @Test
    public void getAll() {
        RESTAURANT_MATCHER.assertMatch(service.getAll(), restaurants);
    }

    @Test
    public void getByAddress() {
        RESTAURANT_MATCHER.assertMatch(service.getByAddress(RESTAURANT1_ADDRESS), List.of(restaurant1));
    }

    @Test
    public void getVoted() {
        List<Restaurant> allVoted = service.getAllVoted(USER_ID);
        //RESTAURANT_MATCHER.assertMatch(allVoted, List.of(restaurant1));
        innerLog.warn("Check request and list {}", allVoted);
    }

    @Test
    public void getVoteCount() {
        Assertions.assertEquals(service.getVoteCount(RESTAURANT1_ID, VOTE_DATE), 1);
    }

    @Test
    public void createWithException(){
        validateRootCause(() -> service.create(new Restaurant(RESTAURANT1_ID, "First", "Long st. 50", "Sea food")), IllegalArgumentException.class);
    }
}