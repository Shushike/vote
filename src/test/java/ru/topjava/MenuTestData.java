package ru.topjava;

import ru.topjava.TestMatcher;
import ru.topjava.model.Dish;
import ru.topjava.model.Menu;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingIgnoringFieldsComparator("restaurant");

    public static final int NOT_FOUND = 10;
    public static final int MENU1_ID = START_SEQ + 12;
    public static final int MENU2_ID = START_SEQ + 13;
    public static final int MENU3_ID = START_SEQ + 14;
    public static final int MENU4_ID = START_SEQ + 15;

    public static final LocalDate MENU_DATE1 = LocalDate.of(2020, 11, 4);
    public static final LocalDate MENU_DATE2 = LocalDate.of(2020, 11, 5);

    public static final Menu menu1 = new Menu(MENU1_ID, MENU_DATE1, List.of(START_SEQ + 5, START_SEQ + 7), "Grand opening");
    public static final Menu menu2 = new Menu(MENU2_ID, MENU_DATE2, List.of(START_SEQ + 6, START_SEQ + 7), "Thursday");
    public static final Menu menu3 = new Menu(MENU3_ID, MENU_DATE1, List.of(START_SEQ + 8, START_SEQ + 9, START_SEQ +10), null);
    public static final Menu menu4 = new Menu(MENU4_ID, MENU_DATE2, List.of(START_SEQ + 9, START_SEQ + 10), "Short day");

    public static final List<Menu> menus = List.of(menu2, menu4, menu1, menu3);
    public static final List<Menu> restaurant1_menus = List.of(menu2, menu1);
    public static final List<Menu> date1_menus = List.of(menu1, menu3);

    public static Menu getNew() {
        return new Menu(null, LocalDate.now(), List.of(START_SEQ + 5, START_SEQ + 7), "New menu");
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, LocalDate.now(), List.of(START_SEQ + 6), "Updated menu");
    }
}
