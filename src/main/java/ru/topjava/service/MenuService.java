package ru.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.model.Menu;
import ru.topjava.repository.MenuRepository;
import ru.topjava.repository.VoteRepository;
import ru.topjava.repository.datajpa.IMenuVote;
import ru.topjava.repository.datajpa.IVotesNumber;
import ru.topjava.util.exception.ModifyForbiddenException;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.util.DateUtil.atDayOrMax;
import static ru.topjava.util.DateUtil.atDayOrMin;
import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService extends RepositoryService<Menu> {
    private static final Logger innerLog = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;
    private final VoteRepository voteRepository;

    public MenuService(MenuRepository repository, VoteRepository voteRepository) {
        super(repository);
        this.menuRepository = repository;
        this.voteRepository = voteRepository;
    }

    public Menu create(Menu menu, int restaurantId) {
        Assert.notNull(menu, "Menu must not be null");
        checkNew(menu);
        return menuRepository.save(menu, restaurantId);
    }

    public Menu update(Menu menu, int restaurantId) {
        Assert.notNull(menu, "Menu must not be null");
        LocalDate oldDate = menuRepository.get(menu.getId()).getDate();
        //if menu date is changing and this menu already has votes, menu modifying is forbidden
        if (oldDate!=null && !oldDate.equals(menu.getDate())) {
            innerLog.debug("Menu date is changing (old value {}, new value {}), check votes", oldDate, menu.getDate());
            boolean menuHasVotes = voteRepository.menuHasVotes(menu.id());
            if (menuHasVotes)
                throw new ModifyForbiddenException(String.format("Menu #%s on date %s already has votes", menu.id(), menu.getDate()));
        }
        return checkNotFoundWithId(menuRepository.save(menu, restaurantId), menu.id());
    }

    public boolean delete(int id, int restaurantId) {
        return checkNotFoundWithId(menuRepository.delete(id, restaurantId), id);
    }

    public Menu get(int id, int restaurantId) {
        return checkNotFoundWithId(menuRepository.get(id, restaurantId), id);
    }

    public List<Menu> getAll(int restaurantId) {
        return menuRepository.getAll(restaurantId);
    }

    /**
     * Return list of menu with dishes for date in all restaurants.
     */
    public List<Menu> getAllByDate(LocalDate localDate) {
        return menuRepository.getAllByDate(localDate);
    }

    /**
     * Return menu with dish list for date and restaurant.
     * Null if not found
     */
    public Menu getByDate(int restaurantId, LocalDate localDate) {
        return menuRepository.getByDate(restaurantId, localDate);
    }

    public List<Menu> getAllByRestaurant(int restaurantId) {
        return menuRepository.getAllByRestaurant(restaurantId);
    }

    /**
     * Return menu list for all restaurants without dishes and restaurant.
     * Ordered by descending menu date and ascending ID
     */
    public List<Menu> getAll() {
        return menuRepository.getAll();
    }

    /**
     * Return list of menus which user voted
     * between dates with dish list
     */
    public List<Menu> getVotedBetweenDateForUser(LocalDate startDate, LocalDate endDate, int userId) {
        return menuRepository.getVotedBetweenDateForUser(startDate, endDate, userId);
    }

    /*
    * Return list of pairs restaurantId and votes number for date
    * */
    public List<IVotesNumber> get(LocalDate localDate) {
        return menuRepository.getVoteNumberByDate(localDate);
    }

    /*
     * Return list of pairs restaurantId and votes number for date range
     * */
    public List<IVotesNumber> get(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return menuRepository.getVoteNumbers(atDayOrMin(startDate), atDayOrMax(endDate));
    }

    /**
     * Return list of menu with dishes for date in all restaurants.
     */
    public List<Menu> getAllForPeriod(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return menuRepository.getBetweenInclude(atDayOrMin(startDate), atDayOrMax(endDate));
    }

    /**
     * Return list of pair menu (with dishes) and vote for period in all restaurants.
     * Vote defined if user voted for menu, otherwise vote is null
     */
    public List<IMenuVote> getAllForPeriod(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return menuRepository.getBetweenIncludeWithUserVote(atDayOrMin(startDate), atDayOrMax(endDate), userId);
    }
}