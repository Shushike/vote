package ru.topjava.repository;

import org.springframework.data.domain.Sort;

public interface BaseRepository<T> {

    // false if not found
    int delete(int id);

    // null if not found
    T get(int id);
}
