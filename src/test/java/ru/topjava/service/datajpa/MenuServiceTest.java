package ru.topjava.service.datajpa;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.model.Menu;
import ru.topjava.service.AbstractServiceTest;
import ru.topjava.service.MenuService;
import ru.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static ru.topjava.MenuTestData.*;
import static ru.topjava.RestaurantTestData.RESTAURANT1_ID;

public class MenuServiceTest extends AbstractServiceTest {
    private static final Logger innerLog = LoggerFactory.getLogger(MenuServiceTest.class);

    @Autowired
    protected MenuService service;

    @Test
    public void delete() {
        service.delete(MENU1_ID);
        assertThrows(NotFoundException.class, () -> service.get(MENU1_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void create() {
        Menu created = service.create(getNew(), RESTAURANT1_ID);
        int newId = created.id();
        Menu newMenu = getNew();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(service.get(newId), newMenu);
    }

    @Test
    public void get() {
        Menu actual = service.get(MENU1_ID);
        MENU_MATCHER.assertMatch(actual, menu1);
        innerLog.debug("Actual menu: "+actual.toString());
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void update() {
        Menu updated = getUpdated();
        service.update(updated, RESTAURANT1_ID);
        MENU_MATCHER.assertMatch(service.get(MENU1_ID), getUpdated());
    }

    @Test
    public void getAllForRestaurant() {
        MENU_MATCHER.assertMatch(service.getAll(RESTAURANT1_ID), restaurant1_menus);
    }

    @Test
    public void getByDate() {
        Menu found = service.getByDate(RESTAURANT1_ID, MENU_DATE1);
        MENU_MATCHER.assertMatch(found, menu1);
        innerLog.debug("Found menu: "+found.toString());
    }

    @Test
    public void getAllByDate() {
        List<Menu> found = service.getAllByDate(MENU_DATE1);
        MENU_MATCHER.assertMatch(found, date1_menus);
    }

    @Test
    public void getAll() {
        List<Menu> found = service.getAll();
        MENU_MATCHER.assertMatch(found, menus);
    }

    @Test
    public void createWithException(){
        validateRootCause(() -> service.create(new Menu(null, menu1.getDate(), null/*menu1.getDishList()*/, null), RESTAURANT1_ID), IllegalArgumentException.class);
        validateRootCause(() -> service.create(new Menu(null, LocalDate.now(), null/*menu1.getDishList()*/, null), NOT_FOUND), JdbcSQLIntegrityConstraintViolationException.class);
    }
}