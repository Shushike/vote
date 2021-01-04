package ru.topjava.repository.datajpa;

import java.time.LocalDate;

public interface IVotesNumber {
    Integer getRestaurantId();
    Long getVoteNumber();
    LocalDate getMenuDate();
}
