package ru.topjava.service.datajpa;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.model.Menu;
import ru.topjava.repository.IVotesNumber;
import ru.topjava.service.AbstractServiceTest;
import ru.topjava.service.MenuService;
import ru.topjava.util.exception.ModifyForbiddenException;
import ru.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import static ru.topjava.MenuTestData.*;
import static ru.topjava.RestaurantTestData.RESTAURANT1_ID;
import static ru.topjava.UserTestData.USER_ID;

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
        innerLog.debug("{} created", created);
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
        innerLog.debug("Actual menu: " + actual.toString());
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void update() {
        Menu updated = getCanUpdated();
        service.update(updated, RESTAURANT1_ID);
        MENU_MATCHER.assertMatch(service.get(MENU4_ID), getCanUpdated());
    }

    @Test
    public void invalidUpdate() {
        Menu updated = getCannotUpdated();
        assertThrows(ModifyForbiddenException.class, () -> service.update(updated, RESTAURANT1_ID));
    }

    @Test
    public void getAllForRestaurant() {
        MENU_MATCHER.assertMatch(service.getAll(RESTAURANT1_ID), restaurant1_menus);
    }

    @Test
    public void getByDate() {
        Menu found = service.getByDate(RESTAURANT1_ID, MENU_DATE1);
        MENU_MATCHER.assertMatch(found, menu1);
        innerLog.debug("Found menu: " + found.toString());
    }

    @Test
    public void getAllByDate() {
        List<Menu> found = service.getAllByDate(MENU_DATE1);
        MENU_MATCHER.assertMatch(found, date1_menus);
    }

    @Test
    public void getVotedBetweenDateForUser() {
        List<Menu> found = service.getVotedBetweenDateForUser(MENU_DATE1, MENU_DATE2, USER_ID);
        innerLog.debug("Found voted menus: " + found.toString());
        MENU_MATCHER.assertMatch(found, menu2, menu1);
    }

    @Test
    public void getByRestaurant() {
        List<Menu> found = service.getAllByRestaurant(RESTAURANT1_ID);
        MENU_MATCHER.assertMatch(found, restaurant1_menus);
        innerLog.debug("Found menus: " + found.toString());
    }

    @Test
    public void getAll() {
        List<Menu> found = service.getAll();
        MENU_MATCHER.assertMatch(found, menus);
    }

    @Test
    public void createWithException() {
        validateRootCause(() -> service.create(new Menu(null, menu1.getDate(), null/*menu1.getDishList()*/, null), RESTAURANT1_ID), JdbcSQLIntegrityConstraintViolationException.class);
        validateRootCause(() -> service.create(new Menu(null, LocalDate.now(), null/*menu1.getDishList()*/, null), NOT_FOUND), NotFoundException.class);
    }

    @Test
    public void getVotes() {
        List<IVotesNumber> numbers = service.get(MENU_DATE1);
        Assertions.assertArrayEquals(
                numbers.stream()
                        .mapToInt(record -> Optional.ofNullable(record.getVoteNumber()).orElse(0L).intValue())
                        .toArray(),
                new int[]{2, 1});
        innerLog.debug("Actual votes: \n" +
                numbers.stream()
                        .map(record -> String.format("%s : %s", record.getRestaurantId(), record.getVoteNumber()))
                        .collect(Collectors.joining("\n"))
        );
    }

    @Test
    public void getVoteNumbers() {
        List<IVotesNumber> numbers = service.get(MENU_DATE1, MENU_DATE2);
        Assertions.assertArrayEquals(
                numbers.stream()
                        .mapToInt(record -> Optional.ofNullable(record.getVoteNumber()).orElse(0L).intValue())
                        .toArray(),
                new int[]{1, 0, 2, 1});
        innerLog.debug("Actual votes: \n" +
                numbers.stream()
                        .map(record -> String.format("%s %s: %s", record.getMenuDate(), record.getRestaurantId(), record.getVoteNumber()))
                        .collect(Collectors.joining("\n"))
        );
    }
}