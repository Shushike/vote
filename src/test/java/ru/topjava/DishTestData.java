package ru.topjava;

import ru.topjava.model.Dish;
import java.util.List;

import static ru.topjava.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class,"restaurant");

    public static final int NOT_FOUND = 10;
    public static final int DISH1_ID = START_SEQ + 5;
    public static final int DISH2_ID = START_SEQ + 6;
    public static final int DISH3_ID = START_SEQ + 7;
    public static final int DISH4_ID = START_SEQ + 8;

    public static final String DISH3_NAME = "Bread";

    public static final Dish dish1 = new Dish(DISH1_ID, "Fried fish", 4000, "Fried sea fish slices");
    public static final Dish dish2 = new Dish(DISH2_ID, "Spicy soup", 3800, "Hot chili");
    public static final Dish dish3 = new Dish(DISH3_ID, "Bread", 500, "Piece of white bread");
    public static final Dish dish4 = new Dish(DISH4_ID, "Chicken soup", 8000, "Soup recipe");

    public static final List<Dish> restaurant1Dishes = List.of(dish3, dish1, dish2);
    public static final List<Dish> menu1Dishes = List.of(dish1, dish3);

    public static Dish getNew() {
        return new Dish(null, "Kebab", 1330, "Grilled meat");
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Seabass", 4200, "Fried slices of fresh seabass");
    }
}
