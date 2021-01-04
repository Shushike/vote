package ru.topjava.service.datajpa;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.model.Dish;
import ru.topjava.service.AbstractServiceTest;
import ru.topjava.service.DishService;
import ru.topjava.util.exception.IllegalRequestDataException;
import ru.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static ru.topjava.DishTestData.*;
import static ru.topjava.RestaurantTestData.RESTAURANT1_ID;

public class DishServiceTest extends AbstractServiceTest {
    private static final Logger innerLog = LoggerFactory.getLogger(DishServiceTest.class);

    @Autowired
    protected DishService service;

    @Test
    public void delete() {
        service.delete(DISH1_ID);
        assertThrows(NotFoundException.class, () -> service.get(DISH1_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void create() {
        Dish created = service.create(getNew(), RESTAURANT1_ID);
        int newId = created.id();
        Dish newDish = getNew();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId), newDish);
    }

    @Test
    public void get() {
        Dish actual = service.get(DISH1_ID);
        DISH_MATCHER.assertMatch(actual, dish1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void update() {
        Dish updated = getUpdated();
        service.update(updated, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(service.get(DISH1_ID), getUpdated());
    }

    @Test
    public void getAll() {
        DISH_MATCHER.assertMatch(service.getAll(RESTAURANT1_ID), restaurant1Dishes);
    }

    @Test
    public void getByName() {
        List<Dish> found = service.getByName(RESTAURANT1_ID, DISH3_NAME);
        DISH_MATCHER.assertMatch(found, List.of(dish3));
    }

    @Test
    public void createWithException(){
        validateRootCause(() -> service.create(new Dish(null, dish1.getName(), 100, null), NOT_FOUND), NotFoundException.class);
        validateRootCause(() -> service.create(new Dish(dish1.getId(), dish1.getName(), 100, null), RESTAURANT1_ID), IllegalRequestDataException.class);
    }
}