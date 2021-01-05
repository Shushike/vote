package ru.topjava;

import ru.topjava.model.Menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.topjava.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dish", "vote");

    public static final int NOT_FOUND = 10;
    public static final int MENU1_ID = START_SEQ + 12;
    public static final int MENU2_ID = START_SEQ + 13;
    public static final int MENU3_ID = START_SEQ + 14;
    public static final int MENU4_ID = START_SEQ + 15;
    public static final int MENU5_ID = START_SEQ + 16;
    public static final int MENU6_ID = START_SEQ + 17;

    public static final LocalDate MENU_DATE1 = LocalDate.of(2020, 11, 4);
    public static final LocalDate MENU_DATE2 = LocalDate.of(2020, 11, 5);
    public static final LocalDate MENU_DATE5 = LocalDate.of(2021, 12, 5);

    public static final Menu menu1 = new Menu(MENU1_ID, MENU_DATE1, null, "Grand opening");
    public static final Menu menu2 = new Menu(MENU2_ID, MENU_DATE2, null, "Thursday");
    public static final Menu menu3 = new Menu(MENU3_ID, MENU_DATE1, null, null);
    public static final Menu menu4 = new Menu(MENU4_ID, MENU_DATE2, null, "Short day");
    public static final Menu menu5 = new Menu(MENU5_ID, MENU_DATE5, null, "After one year");
    public static final Menu menu6 = new Menu(MENU6_ID, MENU_DATE5, null, "After one year");

    public static final List<Menu> menus = new ArrayList<>();// List.of(menu5, menu6, menu2, menu4, menu1, menu3);
    public static final List<Menu> restaurant1_menus = new ArrayList<>();//List.of(menu6, menu2, menu1);
    public static final List<Menu> date1_menus = new ArrayList<>();//List.of(menu1, menu3);

    static {
        menus.add(menu5);
        menus.add(menu6);
        menus.add(menu2);
        menus.add(menu4);
        menus.add(menu1);
        menus.add(menu3);

        restaurant1_menus.add(menu6);
        restaurant1_menus.add(menu2);
        restaurant1_menus.add(menu1);

        date1_menus.add(menu1);
        date1_menus.add(menu3);
    }

    public static Menu getNew() {
        return new Menu(null, LocalDate.now(), null, "New menu");
    }

    public static Menu getCanUpdated() {
        Menu menu = new Menu(MENU4_ID, LocalDate.now(), null, "Updated menu");
        //menu.setRestaurant(RestaurantTestData.restaurant1);
        return menu;
    }

    public static Menu getCannotUpdated() {
        return new Menu(MENU1_ID, LocalDate.now(), null, "Invalid update menu");
    }
}
