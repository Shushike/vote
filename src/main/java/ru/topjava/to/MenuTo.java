package ru.topjava.to;

import ru.topjava.model.Dish;

import java.time.LocalDate;
import java.util.List;

public class MenuTo extends BaseTo {
    private Integer id;
    private LocalDate date;
    private List<Dish> dishList;
    private boolean voted = false;
}
