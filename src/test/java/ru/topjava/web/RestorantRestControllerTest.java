package ru.topjava.web;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.topjava.model.Restaurant;
import ru.topjava.service.RestaurantService;
import ru.topjava.util.exception.NotFoundException;
import ru.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.RestaurantTestData.*;
import static ru.topjava.TestUtil.readFromJson;
import static ru.topjava.TestUtil.userHttpBasic;
import static ru.topjava.UserTestData.admin;
import static ru.topjava.UserTestData.user2;

class RestorantRestControllerTest extends AbstractControllerTest {

    private static final String COMMON_URL = RestaurantRestController.COMMON_URL + '/';
    private static final String ADMIN_URL = RestaurantRestController.ADMIN_URL + '/';

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void getCommon() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_URL + RESTAURANT1_ID)
                .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    void getComplex() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_URL + RESTAURANT1_ID + "/complex")
                .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_URL + "/filter")
                .param("startDate", VOTE_DATE.toString())
                .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1, restaurant2));
    }

    @Test
    void getVotesInfo() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(COMMON_URL + RESTAURANT1_ID + "/votes-number")
                .param("voteDate", VOTE_DATE.toString())
                .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.content().string("2"));

    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_URL + NOT_FOUND)
                .with(userHttpBasic(user2)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_URL + RESTAURANT1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantService.get(RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(ADMIN_URL + RESTAURANT1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantService.get(RESTAURANT1_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant))
                .with(userHttpBasic(admin)))
                .andDo(print());

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_URL)
                .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}