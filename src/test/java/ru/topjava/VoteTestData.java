package ru.topjava;

import ru.topjava.model.Vote;

import java.time.LocalDateTime;


import static ru.topjava.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator("user", "menu");

    public static final int NOT_FOUND = 10;
    public static final int USER1_ID = UserTestData.USER_ID;// START_SEQ;
    public static final int USER2_ID = START_SEQ + 1;
    public static final int USER3_ID = START_SEQ + 2;
    public static final int MENU12_ID = MenuTestData.MENU1_ID;// START_SEQ + 12;
    public static final int MENU13_ID = START_SEQ + 13;
    public static final int MENU14_ID = START_SEQ + 14;
    public static final int MENU15_ID = MenuTestData.MENU5_ID;

    public static final int VOTE2_ID = START_SEQ + 6;
    public static final int VOTE3_ID = START_SEQ + 7;

    /*INSERT INTO vote (menu_id, user_id, date_time)
    VALUES (100012, 100000, TIMESTAMP '2020-11-04 09:34:18'),
       (100014, 100002, TIMESTAMP '2020-11-04 08:00:02'),
            (100013, 100000, TIMESTAMP '2020-11-04 10:00:00');*/

    public static final Vote vote1 = new Vote(MenuTestData.menu1, UserTestData.user, LocalDateTime.of(2020, 11, 04, 9, 34, 18)); //MENU12_ID, USER1_ID
/*    public static final Vote vote2 = new Vote(DISH2_ID, "Spicy soup", 3800, "Hot chili");
    public static final Vote vote3 = new Vote(DISH3_ID, "Bread", 500, "Piece of white bread");*/

    /*public static final List<Dish> restaurant1Dishes = List.of(dish3, dish1, dish2);
*/
    public static Vote getCanUpdated() {
        return new Vote(MenuTestData.menu5, UserTestData.user, LocalDateTime.of(2020, 12, 05, 1, 0, 0));
    }

    public static Vote getCannotUpdated() {
        return new Vote(MenuTestData.menu1, UserTestData.user, LocalDateTime.of(2020, 12, 05, 1, 0, 0));
    }

    public static Vote getBrandNew() {
        return new Vote(LocalDateTime.of(2020, 12, 05, 1, 0, 0));
    }

    public static Vote getNew() {
        return new Vote(MenuTestData.menu4, UserTestData.user, LocalDateTime.of(2020, 12, 05, 1, 0, 0));
    }

}
