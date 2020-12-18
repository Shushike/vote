package ru.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.model.Menu;
import ru.topjava.model.Vote;
import ru.topjava.repository.MenuRepository;
import ru.topjava.repository.VoteRepository;
import ru.topjava.util.ValidationUtil;
import ru.topjava.util.exception.ModifyForrbidenException;
import ru.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNotFound;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    private static final Logger innerLog = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private String FAILED_MSG = "Failed to find vote for menu #%s by user #%s";
    private String NOT_NULL_MSG = "Vote must not be null";

    public VoteService(VoteRepository repository, MenuRepository menuRepository) {
        voteRepository = repository;
        this.menuRepository = menuRepository;
    }

    protected boolean checkUpdateTime(int menuId, boolean isNew) throws ModifyForrbidenException, NotFoundException {
        Menu menu = ValidationUtil.checkNotFoundWithId(menuRepository.get(menuId), "menu", menuId);
        innerLog.debug("Menu date {} is after current date {}", menu.getDate(), LocalDate.now().isBefore(menu.getDate()));
        if (LocalDate.now().isBefore(menu.getDate())) {
            innerLog.debug("Can change vote");
        } else {
            innerLog.debug("Need to check time");
            /*If it is before 11:00 we assume that he changed his mind.
              If it is after 11:00 then it is too late, vote can't be changed*/
            if (!LocalDateTime.now().isBefore(LocalDateTime.of(menu.getDate(), LocalTime.of(11, 0))))
                throw new ModifyForrbidenException(isNew ? "Vote can't be created" : "Vote can't be changed");
        }
        return true;
    }

    public Vote create(Vote vote, int menuId, int userId) {
        Assert.notNull(vote, NOT_NULL_MSG);
        checkUpdateTime(menuId, vote.isNew());
        return voteRepository.save(vote, menuId, userId);
    }

    /**
     * Create new vote for menu and user with current time.
     * Return created vote
     */
    public Vote create(int menuId, int userId) {
        checkUpdateTime(menuId, true);
        return voteRepository.create(menuId, userId);
    }

    public void update(Vote vote, int menuId, int userId) {
        Assert.notNull(vote, NOT_NULL_MSG);
        checkUpdateTime(menuId, vote.isNew());
        checkNotFoundWithId(voteRepository.save(vote, menuId, userId), vote.id());
    }

    /**
     * Return vote by ID if it's users' vote
     */
    public Vote getById(int id, int userId) {
        return checkNotFoundWithId(voteRepository.getById(id, userId), id);
    }

    /**
     * Return vote by menu and user
     */
    public Vote get(int menuId, int userId) {
        return checkNotFound(voteRepository.get(menuId, userId), String.format(FAILED_MSG, menuId, userId));
    }

    public void delete(int menuId, int userId) {
        checkNotFound(voteRepository.delete(menuId, userId), String.format(FAILED_MSG, menuId, userId));
    }

    public void delete(int id) {
        checkNotFoundWithId(voteRepository.delete(id), id);
    }

    /**
     * Return list of votes for menu with users.
     * ordered by descending vote time
     */
    public List<Vote> getAllForMenu(int menuId) {
        return voteRepository.getAllForMenu(menuId);
    }

    /**
     * Return list of votes for user with menus.
     * ordered by descending vote time
     */
    public List<Vote> getAllForUser(int userId) {
        return voteRepository.getAllForUser(userId);
    }

    /**
     * Return vote of user for menu on menuDate
     * with menu
     */
    public Vote getByDateForUser(int userId, LocalDate menuDate){ return voteRepository.getByDate(userId, menuDate);}
}