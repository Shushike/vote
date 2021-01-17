package ru.topjava.repository;

import java.time.LocalDate;

public interface IVotesNumber {
    Integer getRestaurantId();
    Long getVoteNumber();
    LocalDate getMenuDate();
}
