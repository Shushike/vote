package ru.topjava.service.datajpa;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.MenuTestData;
import ru.topjava.UserTestData;
import ru.topjava.model.Vote;
import ru.topjava.service.AbstractServiceTest;
import ru.topjava.service.VoteService;
import ru.topjava.util.exception.ModifyForrbidenException;
import ru.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.VoteTestData.*;

public class VoteServiceTest extends AbstractServiceTest {
    private static final Logger innerLog = LoggerFactory.getLogger(VoteServiceTest.class);

    @Autowired
    protected VoteService service;

    @Test
    public void delete() {
        service.delete(MENU12_ID, USER1_ID);
        assertThrows(NotFoundException.class, () -> service.get(MENU12_ID, USER1_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER1_ID));
    }

    @Test
    public void createBrandNew() {
        Vote created = service.create(MENU12_ID, USER2_ID);
        innerLog.debug("{} created", created);
        Vote actual = service.get(MENU12_ID, USER2_ID);
        VOTE_MATCHER.assertMatch(actual, created);
    }

    @Test
    public void create() {
        Vote created = service.create(getNew(), MenuTestData.menu4.id(), UserTestData.user.id());
        VOTE_MATCHER.assertMatch(getNew(), created);
    }

    @Test
    public void get() {
        Vote actual = service.get(MENU12_ID, USER1_ID);
        VOTE_MATCHER.assertMatch(actual, vote1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER1_ID));
    }

    @Test
    public void canUpdate() {
        Vote updated = getCanUpdated();
        service.update(updated, MENU15_ID, USER1_ID);
        /* DISH_MATCHER.assertMatch(service.get(DISH1_ID), getUpdated());*/
    }

    @Test
    public void canNotUpdate() {
        Vote updated = getCannotUpdated();
        assertThrows(ModifyForrbidenException.class, () ->
                service.update(updated, MENU12_ID, USER1_ID));
    }

    @Test
    public void getAllForMenu() {
        List<Vote> list = service.getAllForMenu(MENU12_ID);
        System.out.println(">>"+list);
        //DISH_MATCHER.assertMatch(service.getAll(RESTAURANT1_ID), restaurant1Dishes);
    }

    @Test
    public void getAllForUser() {
        List<Vote> list = service.getAllForUser(USER1_ID);
        System.out.println(">>"+list
        );
        //DISH_MATCHER.assertMatch(found, List.of(dish3));
    }

    @Test
    public void createWithException() {
     /*   validateRootCause(() -> service.create(new Dish(null, dish1.getName(), 100, null), NOT_FOUND), JdbcSQLIntegrityConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish(dish1.getId(), dish1.getName(), 100, null), RESTAURANT1_ID), IllegalArgumentException.class);*/
    }
}