package ru.topjava.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.DishTestData;
import ru.topjava.MenuTestData;
import ru.topjava.UserTestData;
import ru.topjava.model.Dish;
import ru.topjava.model.Menu;
import ru.topjava.service.MenuService;
import ru.topjava.util.exception.NotFoundException;
import ru.topjava.web.json.JsonUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.DishTestData.DISH_MATCHER;
import static ru.topjava.DishTestData.dish4;
import static ru.topjava.MenuTestData.*;
import static ru.topjava.RestaurantTestData.RESTAURANT1_ID;
import static ru.topjava.TestUtil.readFromJson;
import static ru.topjava.TestUtil.userHttpBasic;
import static ru.topjava.util.exception.ErrorType.DATA_ERROR;
import static ru.topjava.util.exception.ErrorType.VALIDATION_ERROR;
import static ru.topjava.web.json.JacksonObjectMapper.getMapper;


class MenuRestControllerTest extends AbstractControllerTest {
    private static final Logger innerLog = LoggerFactory.getLogger(MenuRestControllerTest.class);
    private static final String COMMON_RESTAURANT1_URL = RestaurantRestController.COMMON_URL + '/' + RESTAURANT1_ID + "/menus/";
    private static final String ADMIN_RESTAURANT1_URL = RestaurantRestController.ADMIN_URL + '/' + RESTAURANT1_ID + "/menus/";

    @Autowired
    private MenuService menuService;

    @Test
    void get() throws Exception {

        ResultMatcher matcher = MENU_MATCHER.contentJson(menu1);
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL + MENU1_ID)
                .with(userHttpBasic(UserTestData.user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(matcher);
    }


    @Test
    public void jsonRead() throws IOException {
        String json =
                "{\"id\":2,\"description\":\"opening\",\"date\":\"2020-11-04\",\"dish\":null, \"restaurantId\":\"100003\"}";
        Menu item = getMapper().readerFor(Menu.class).readValue(json);
        System.out.println(item);
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL + MenuTestData.NOT_FOUND)
                .with(userHttpBasic(UserTestData.user2)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_RESTAURANT1_URL + MENU1_ID)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> menuService.get(MENU1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_RESTAURANT1_URL + MenuTestData.NOT_FOUND)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateMenuDate() throws Exception {
        Menu updated = getCanUpdated();
        innerLog.debug("Before update \n{}", menuService.get(updated.getId()));
        perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANT1_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isOk());
        innerLog.debug("After update \n{}", menuService.get(updated.getId()));
        MENU_MATCHER.assertMatch(menuService.get(updated.getId()), updated);
    }

    @Test
    void updateDishes() throws Exception {
        Menu updated = getCanUpdated();
        innerLog.debug("Before update \n{}", menuService.get(updated.getId()));
        updated.setDishes(DishTestData.restaurant1Dishes);
        perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANT1_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isOk());
        innerLog.debug("After update \n{}", menuService.get(updated.getId()));
        MENU_MATCHER.assertMatch(menuService.get(updated.getId()), updated);
    }

    @Test
    void updateNotOwnDishes() throws Exception {
        Menu updated = getCanUpdated();
        ArrayList<Dish> list = new ArrayList<Dish>();
        list.add(DishTestData.dish4);
        list.add(DishTestData.dish1);
        updated.setDishes(list);
        perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANT1_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isOk());
        Menu actual = menuService.get(updated.getId());
        MENU_MATCHER.assertMatch(actual, updated);
        DISH_MATCHER.assertMatch(actual.getDishes(), DishTestData.dish1);
    }

    @Test
    void invalidUpdate() throws Exception {
        Menu updated = getCannotUpdated();
        perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANT1_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isConflict()).andExpect(errorType(DATA_ERROR));
    }

    @Test
    void createWithLocation() throws Exception {
        Menu newMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANT1_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu))
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print());

        Menu created = readFromJson(action, Menu.class);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuService.get(newId), newMenu);
    }

    @Test
    void create() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANT1_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\":\"2020-12-26\",\"dish\":[{\"id\":\"100005\"},{\"id\":\"100006\"}],\"description\":\"New menu\"}")
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print());

        Menu created = readFromJson(action, Menu.class);
        int newId = created.id();
        System.out.println("Created menu " + menuService.get(newId));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(COMMON_RESTAURANT1_URL)
                .with(userHttpBasic(UserTestData.user2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu6, menu2, menu1));
    }

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(MenuRestController.MENU_URL + "/by")
                .param("menuDate", "2020-11-04")
                .with(userHttpBasic(UserTestData.user2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu1, menu3));
    }

    @Test
    void getAllForPeriod() throws Exception {
        perform(MockMvcRequestBuilders.get(MenuRestController.PROFILE_URL + "/by")
                .param("startDate", "2020-11-04")
                .with(userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantRestController.ADMIN_URL + "/" + RESTAURANT1_ID + "/menus/by")
                .param("menuDate", "2020-11-04")
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu1));
    }

    @Test
    void getByEmptyDate() throws Exception {
        perform(MockMvcRequestBuilders.get(MenuRestController.MENU_URL + "/by?menuDate=")
                .with(userHttpBasic(UserTestData.user2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByDateForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(MenuRestController.PROFILE_URL + "/by")
                .param("menuDate", "2020-11-04")
                .with(userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @CsvSource({", 2020-11-04", "2020-11-04, 2020-11-06", ","})
    void getVotedBetweenDateForUser(LocalDate startDate, LocalDate endDate) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(MenuRestController.PROFILE_URL + "/filter");
        if (startDate != null)
            builder.param("startDate", String.valueOf(startDate));
        if (endDate != null)
            builder.param("endDate", String.valueOf(endDate));
        perform(builder
                .with(userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        Menu invalid = getCanUpdated();
        invalid.setDescription("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANT1_URL + invalid.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void getVotesNumberByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantRestController.COMMON_URL + "/votes-number")
                .param("voteDate", "2020-11-04")
                .with(userHttpBasic(UserTestData.user2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @CsvSource({", 2020-11-04", "2020-11-04, 2020-11-06", ","})
    void getVoteNumbers(LocalDate startDate, LocalDate endDate) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(RestaurantRestController.COMMON_URL + "/votes-number");
        if (startDate != null)
            builder.param("startDate", String.valueOf(startDate));
        if (endDate != null)
            builder.param("endDate", String.valueOf(endDate));
        perform(builder
                .with(userHttpBasic(UserTestData.user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}