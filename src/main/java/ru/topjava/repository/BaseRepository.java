package ru.topjava.repository;

import org.springframework.data.domain.Sort;

public interface BaseRepository<T> {
    Sort SORT_NAME = Sort.by("name");

    // false if not found
    boolean delete(int id);

    // null if not found
    T get(int id);
}
