package ru.topjava.to;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class StatisticsTo implements Serializable {
    private static final long serialVersionUID = 3L;
    private LocalDate menuDate;
    private Integer restaurantId;
    private Long votesNumber;

    @ConstructorProperties({"restaurantId", "votesNumber", "date"})
    public StatisticsTo(Integer restaurantId, Long votesNumber, LocalDate menuDate) {
        this.menuDate = menuDate;
        this.votesNumber = votesNumber;
        this.restaurantId = restaurantId;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public Long getVotesNumber() {
        return votesNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticsTo statisticsTo = (StatisticsTo) o;
        return Objects.equals(restaurantId, statisticsTo.restaurantId) &&
               Objects.equals(menuDate, statisticsTo.menuDate) &&
               Objects.equals(votesNumber, statisticsTo.votesNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, menuDate, votesNumber);
    }

    @Override
    public String toString() {
        return "StatisticsTo{" +
                "restaurantId=" + restaurantId +
                ", menuDate=" + menuDate +
                ", votesNumber=" + votesNumber +
                '}';
    }

}
