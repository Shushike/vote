package ru.topjava;

import ru.topjava.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "menus", "dishes");

    public static final int NOT_FOUND = 10;
    public static final int RESTAURANT1_ID = START_SEQ + 3;
    public static final int RESTAURANT2_ID = START_SEQ + 4;
    public static final String RESTAURANT1_ADDRESS = "Long st. 50";
    public static final LocalDate VOTE_DATE = LocalDate.of(2020, 11, 4);

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "First", RESTAURANT1_ADDRESS, "Sea food");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Second", "Broadway st. 40", "Popular place");

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant2);

    public static Restaurant getNew() {
        return new Restaurant(null, "Kebab", "Lenina 100", "Fried meat");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, restaurant1.getName()+" Modified", "Too Long st. 50", "Sea food restaurant");
    }
}
