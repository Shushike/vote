package ru.topjava.web;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.UserTestData;
import ru.topjava.model.Vote;
import ru.topjava.service.VoteService;
import ru.topjava.util.exception.NotFoundException;

import static ru.topjava.MenuTestData.*;
import static ru.topjava.TestUtil.userHttpBasic;
import static ru.topjava.UserTestData.USER_ID;
import static ru.topjava.VoteTestData.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.TestUtil.readFromJson;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';
    private static final String PROFILE_URL = VoteRestController.PROFILE_URL + '/';

    @Autowired
    private VoteService voteService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE1_ID)
                .with(userHttpBasic(UserTestData.user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE1_ID)
                .with(userHttpBasic(UserTestData.user1)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.getById(VOTE1_ID, USER_ID));
    }

    @Test
    void canVoteMenu() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.put(PROFILE_URL + MENU5_ID)
                .with(userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isAccepted());
        Vote created = readFromJson(action, Vote.class);
        VOTE_MATCHER.assertMatch(created, voteService.get(MENU5_ID, USER1_ID));
        VOTE_MATCHER.assertMatch(created, voteService.getByDateForUser(USER1_ID, menu5.getDate()));
    }

    @Test
    void cantVoteMenu() throws Exception {
        perform(MockMvcRequestBuilders.put(PROFILE_URL + MENU1_ID)
                .with(userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(UserTestData.user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        //.andExpect(MEAL_TO_MATCHER.contentJson(getTos(meals, user.getCaloriesPerDay())));
    }
}