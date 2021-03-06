package ru.topjava.service.datajpa;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.MenuTestData;
import ru.topjava.UserTestData;
import ru.topjava.model.Vote;
import ru.topjava.service.AbstractServiceTest;
import ru.topjava.service.VoteService;
import ru.topjava.util.exception.ModifyForbiddenException;
import ru.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.MenuTestData.MENU1_ID;
import static ru.topjava.MenuTestData.MENU5_ID;
import static ru.topjava.VoteTestData.*;

public class VoteServiceTest extends AbstractServiceTest {
    private static final Logger innerLog = LoggerFactory.getLogger(VoteServiceTest.class);

    @Autowired
    protected VoteService service;

    @Test
    public void deleteForMenu() {
        service.deleteForMenu(MENU1_ID, USER1_ID);
        assertThrows(NotFoundException.class, () -> service.get(MENU1_ID, USER1_ID));
    }

    @Test
    public void deleteById() {
        service.delete(VOTE2_ID);
        assertThrows(NotFoundException.class, () -> service.getById(VOTE2_ID, USER1_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.deleteForMenu(NOT_FOUND, USER1_ID));
    }

    @Test
    public void deleteNotFoundById() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void createBrandNew() {
        innerLog.debug("List before creation {} ", service.getAllForUser(UserTestData.USER2_ID));
        Vote created = service.create(MenuTestData.menu5.id(), UserTestData.USER2_ID);
        innerLog.debug("List after creation {} ", service.getAllForUser(UserTestData.USER2_ID));

        VOTE_MATCHER.assertMatch(service.get(MenuTestData.menu5.id(), UserTestData.USER2_ID), created);
        VOTE_MATCHER.assertMatch(service.getByDateForUser(UserTestData.USER2_ID, MenuTestData.menu5.getDate()), created);
    }

    @Test
    public void create() {
        Vote created = service.create(getNew(), MenuTestData.menu5.id(), UserTestData.user2.id());
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);

        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(service.getById(newId, UserTestData.user2.id()), newVote);
    }

    @Test
    public void get() {
        Vote actual = service.get(MENU1_ID, USER1_ID);
        innerLog.debug("get {} expected {} ", actual, vote1);
        VOTE_MATCHER.assertMatch(actual, vote1);
    }

    @Test
    public void getById() {
        Vote actual = service.getById(VOTE1_ID, USER1_ID);
        innerLog.debug("get {} expected {} ", actual, vote1);
        VOTE_MATCHER.assertMatch(actual, vote1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER1_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(VOTE1_ID, USER2_ID));
    }

    @Test
    public void getNotFoundById() {
        assertThrows(NotFoundException.class, () -> service.getById(NOT_FOUND, USER1_ID));
    }

    @Test
    public void update() {
        Vote updated = getCanUpdated();
        innerLog.debug("{} before update", updated);
        service.update(updated, MENU5_ID, USER1_ID);
        innerLog.debug("{} updated", updated);
        VOTE_MATCHER.assertMatch(service.getById(updated.id(), USER1_ID), updated);
    }

    @Test
    public void invalidUpdate() {
        Vote updated = getCannotUpdated();
        assertThrows(ModifyForbiddenException.class, () ->
                service.update(updated, MENU1_ID, USER1_ID));
    }

    @Test
    public void getAllForMenu() {
        List<Vote> list = service.getAllForMenu(MENU1_ID);
        innerLog.debug("Menu votes: {}", list);
        VOTE_MATCHER.assertMatch(list, menu12Votes);
    }

    @Test
    public void getAllForUser() {
        List<Vote> list = service.getAllForUser(USER1_ID);
        innerLog.debug("User votes: {}", list);
        VOTE_MATCHER.assertMatch(list, user1Votes);
    }

    @Test
    public void createWithException() {
        validateRootCause(() -> service.merge(MENU1_ID, UserTestData.USER_ID), ModifyForbiddenException.class);
        validateRootCause(() -> service.create(NOT_FOUND, UserTestData.USER2_ID), NotFoundException.class);
        validateRootCause(() -> service.create(MENU5_ID, NOT_FOUND), NotFoundException.class);
    }
}