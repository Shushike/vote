package ru.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.model.Dish;
import ru.topjava.service.DishService;
import ru.topjava.util.exception.NotFoundException;
import ru.topjava.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.DishTestData.*;
import static ru.topjava.MenuTestData.MENU1_ID;
import static ru.topjava.RestaurantTestData.RESTAURANT1_ID;
import static ru.topjava.TestUtil.readFromJson;
import static ru.topjava.TestUtil.userHttpBasic;
import static ru.topjava.UserTestData.admin;
import static ru.topjava.UserTestData.user2;


class DishRestControllerTest extends AbstractControllerTest {

    private static final String COMMON_RESTAURANT1_URL = DishRestController.COMMON_URL + '/' + RESTAURANT1_ID + "/dishes/";
    private static final String ADMIN_RESTAURANT1_URL = DishRestController.ADMIN_URL + '/' + RESTAURANT1_ID + "/dishes/";

    @Autowired
    private DishService dishService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL + DISH1_ID)
                .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL + NOT_FOUND)
                .with(userHttpBasic(user2)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_RESTAURANT1_URL + DISH1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> dishService.get(DISH1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_RESTAURANT1_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANT1_URL + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishService.get(DISH1_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANT1_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish))
                .with(userHttpBasic(admin)))
                .andDo(print());

        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishService.get(newId), newDish);
    }

    @Test
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL)
                .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(restaurant1Dishes));
    }

    @Test
    void getBreadForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL)
                .param("name", "bread")
                .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(List.of(dish3)));
    }

    @Test
    void getAllForMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantRestController.COMMON_URL + '/' + RESTAURANT1_ID + "/menus/" + MENU1_ID + "/dishes/")
                        .with(userHttpBasic(user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(menu1Dishes));
    }
}