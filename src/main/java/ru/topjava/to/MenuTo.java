package ru.topjava.to;

import io.swagger.annotations.ApiModelProperty;
import ru.topjava.model.Dish;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class MenuTo extends BaseTo {
    @ApiModelProperty(notes = "Menu date")
    private LocalDate date;
    @ApiModelProperty(notes = "List of dishes for this date")
    private Set<Dish> dish;
    @ApiModelProperty(notes = "Menu description")
    private String description;
    @ApiModelProperty(notes = "Flag shows if menu are voted by current user")
    private boolean voted = false;

    @ConstructorProperties({"id", "date", "description", "dishes", "voted"})
    public MenuTo(Integer id, LocalDate date, String description, Set<Dish> dish, boolean voted) {
        super(id);
        this.date = date;
        this.description = description;
        this.dish = dish;
        this.voted = voted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTo menuTo = (MenuTo) o;
        return voted == menuTo.voted &&
                Objects.equals(dish, menuTo.dish) &&
                Objects.equals(id, menuTo.id) &&
                Objects.equals(date, menuTo.date) &&
                Objects.equals(description, menuTo.description);
    }

    public LocalDate getDate() {
        return date;
    }

    public Set<Dish> getDish() {
        return dish;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVoted() {
        return voted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, description, dish, voted);
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", dish=" + dish +
                ", voted=" + voted +
                '}';
    }
}
