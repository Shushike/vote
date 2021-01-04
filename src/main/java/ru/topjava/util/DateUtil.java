package ru.topjava.util;

import java.time.LocalDate;

public class DateUtil {
    // DB doesn't support LocalDate.MIN/MAX
    private static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    private DateUtil() {
    }

    public static LocalDate atDayOrMin(LocalDate localDate) {
        return localDate != null ? localDate : MIN_DATE;
    }
    public static LocalDate atDayOrMax(LocalDate localDate) {
        return localDate != null ? localDate : MAX_DATE;
    }
}
