package ru.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.model.Menu;
import ru.topjava.model.Vote;
import ru.topjava.repository.datajpa.CrudMenuRepository;
import ru.topjava.repository.datajpa.CrudUserRepository;
import ru.topjava.repository.datajpa.CrudVoteRepository;
import ru.topjava.util.ValidationUtil;
import ru.topjava.util.exception.InvalidPropertyException;
import ru.topjava.util.exception.ModifyForbiddenException;
import ru.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNotFound;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService extends RepositoryService<Vote> {

    private static final Logger innerLog = LoggerFactory.getLogger(VoteService.class);
    private final CrudVoteRepository crudRepository;
    private final CrudMenuRepository menuRepository;
    private final CrudUserRepository userRepository;
    private final String FAILED_MSG = "Failed to find vote for menu #%s by user #%s";
    private final String NOT_NULL_MSG = "Vote must not be null";

    public VoteService(CrudVoteRepository crudRepository, CrudMenuRepository menuRepository, CrudUserRepository userRepository) {
        super(crudRepository);
        this.crudRepository = crudRepository;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
    }

    protected void checkUpdateTime(int menuId, boolean firstTimeForDate) throws ModifyForbiddenException, NotFoundException {
        Menu menu = ValidationUtil.checkNotFoundWithId(menuRepository.get(menuId), "menu", menuId);
        innerLog.debug("Menu date {} is after current date: {}", menu.getDate(), LocalDate.now().isBefore(menu.getDate()));
        if (LocalDate.now().isBefore(menu.getDate())) {
            innerLog.debug("Can change vote");
        } else {
            innerLog.debug("Need to check time");
            /* User can vote first time on any time
               If it is before 11:00 we assume that he changed his mind.
               If it is after 11:00 then it is too late, vote can't be changed*/
            if (!firstTimeForDate && !LocalDateTime.now().isBefore(LocalDateTime.of(menu.getDate(), LocalTime.of(11, 0))))
                throw new ModifyForbiddenException("Vote can't be changed");

        }
    }

    public Vote create(Vote vote, int menuId, int userId) {
        Assert.notNull(vote, NOT_NULL_MSG);
        checkUpdateTime(menuId, vote.isNew());
        return save(vote, menuId, userId);
    }

    public Vote create(Vote vote, int userId) {
        if (vote.getMenu() == null)
            throw new InvalidPropertyException("Failed to get menu");
        return create(vote, vote.getMenu().getId(), userId);
    }

    /**
     * Create new vote for menu and user with current time.
     * Return created vote
     */
    public Vote create(int menuId, int userId) throws NotFoundException {
        checkUpdateTime(menuId, true);
        Vote newVote = new Vote(menuRepository.findEntityById(menuId), userRepository.findEntityById(userId));
        return crudRepository.save(newVote);
    }

    @Transactional
    public Vote save(Vote vote, int menuId, int userId) {
        //check is this vote of user
        if (!vote.isNew() && getById(vote.id(), userId) == null) {
            innerLog.debug("{} can not be update", vote);
            return null;
        }
        final Menu menu = menuRepository.findEntityById(menuId);
        vote.setMenu(menu);
        vote.setUser(userRepository.findEntityById(userId));
        return crudRepository.save(vote);
    }

    public void update(Vote vote, int menuId, int userId) {
        Assert.notNull(vote, NOT_NULL_MSG);
        checkUpdateTime(menuId, vote.isNew());
        checkNotFoundWithId(save(vote, menuId, userId), vote.id());
    }

    public Vote merge(int menuId, int userId) {
        Menu menu = ValidationUtil.checkNotFoundWithId(menuRepository.get(menuId), "menu", menuId);
        Vote userVote = getByDate(userId, menu.getDate());
        checkUpdateTime(menuId, userVote == null);
        return save(userVote == null ? new Vote(menu, userRepository.get(userId)) : userVote, menuId, userId);
    }

    /**
     * Return vote by ID if it's users' vote
     */
    public Vote getById(int id, int userId) {
        return checkNotFoundWithId(crudRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null), id);
    }

    /**
     * Return vote by menu and user
     */
    public Vote get(int menuId, int userId) {
        return checkNotFound(crudRepository.get(menuId, userId), String.format(FAILED_MSG, menuId, userId));
    }

    public void deleteForMenu(int menuId, int userId) {
        checkNotFound(crudRepository.deleteForMenu(menuId, userId) != 0, String.format(FAILED_MSG, menuId, userId));
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(crudRepository.delete(id, userId) != 0, id);
    }

    /**
     * Return list of votes for menu with users.
     * ordered by descending vote time
     */
    public List<Vote> getAllForMenu(int menuId) {
        return crudRepository.getAllForMenu(menuId);
    }

    /**
     * Return list of votes for user with menus.
     * ordered by descending vote time
     */
    public List<Vote> getAllForUser(int userId) {
        return crudRepository.getAllForUser(userId);
    }

    /**
     * Return vote of user for menu on menuDate
     * with menu
     */
    public Vote getByDateForUser(int userId, LocalDate menuDate) {
        return getByDate(userId, menuDate);
    }

    public Vote getByDate(int userId, LocalDate menuDate) {
        if (menuDate == null)
            return null;
        return crudRepository.getForUserByDate(userId, menuDate);
    }

    public boolean menuHasVotes(int menuId) {
        return crudRepository.menuHasVotes(menuId)>0;
    }
}