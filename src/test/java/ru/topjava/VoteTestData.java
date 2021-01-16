package ru.topjava;

import ru.topjava.model.Vote;

import java.time.LocalDateTime;
import java.util.List;


import static ru.topjava.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "menu");

    public static final int NOT_FOUND = 10;
    public static final int USER1_ID = UserTestData.USER_ID;// START_SEQ;
    public static final int USER2_ID = START_SEQ + 1;
    public static final int USER3_ID = START_SEQ + 2;

    public static final int VOTE1_ID = START_SEQ + 18;
    public static final int VOTE2_ID = START_SEQ + 19;
    public static final int VOTE3_ID = START_SEQ + 20;
    public static final int VOTE4_ID = START_SEQ + 21;
    public static final int VOTE5_ID = START_SEQ + 22;

    public static final Vote vote1 = new Vote(VOTE1_ID, MenuTestData.menu1, UserTestData.user1, LocalDateTime.of(2020, 11, 04, 9, 34, 18));
    public static final Vote vote2 = new Vote(VOTE2_ID, MenuTestData.menu3, UserTestData.user2, LocalDateTime.of(2020, 11, 04, 8, 0, 2));
    public static final Vote vote3 = new Vote(VOTE3_ID, MenuTestData.menu3, UserTestData.user1, LocalDateTime.of(2020, 11, 04, 10, 00, 00));
    public static final Vote vote4 = new Vote(VOTE4_ID, MenuTestData.menu1, UserTestData.user2, LocalDateTime.of(2020, 11, 03, 15, 20, 00));
    public static final Vote vote5 = new Vote(VOTE5_ID, MenuTestData.menu5, UserTestData.user1, LocalDateTime.of(2020, 12, 03, 12, 26, 00));

    public static final List<Vote> menu12Votes = List.of(vote1, vote4);
    public static final List<Vote> user1Votes = List.of(vote5, vote3, vote1);

    /**
     * menu5 and user1
     * */
    public static Vote getCanUpdated() {
        return new Vote(VOTE5_ID, MenuTestData.menu5, UserTestData.user1, LocalDateTime.of(2020, 12, 05, 1, 0, 0));
    }

    public static Vote getCannotUpdated() {
        return new Vote(VOTE1_ID, MenuTestData.menu1, UserTestData.user1, LocalDateTime.of(2020, 12, 05, 1, 0, 0));
    }

    public static Vote getNew() {
        return new Vote(MenuTestData.menu5, UserTestData.user2, LocalDateTime.of(2020, 12, 05, 1, 0, 0));
    }

}
