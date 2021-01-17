package ru.topjava.repository;

import ru.topjava.model.Menu;
import ru.topjava.model.Vote;

public interface IMenuVote {
    Menu getMenu();
    Vote getUserVote();
}
