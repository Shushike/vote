package ru.topjava.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.model.Menu;
import ru.topjava.model.Vote;
import ru.topjava.repository.MenuRepository;
import ru.topjava.repository.VoteRepository;
import ru.topjava.repository.datajpa.IVotesNumber;
import ru.topjava.util.exception.ModifyForrbidenException;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.util.DateUtil.atDayOrMax;
import static ru.topjava.util.DateUtil.atDayOrMin;
import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService extends RepositoryService<Menu> {

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

    public void update(Menu menu, int restaurantId) {
        //обновлять и создавать объекты может только администратор!
        Assert.notNull(menu, "Menu must not be null");
        //если изменилась дата и уже есть голоса за меню, то запретить измения даты
        List<Vote> menuVotes = voteRepository.getAllForMenu(menu.id());
        if (menuVotes != null && !menuVotes.isEmpty())
            throw new ModifyForrbidenException(String.format("Menu #%s on date %s already has votes", menu.id(), menu.getDate()));
        checkNotFoundWithId(menuRepository.save(menu, restaurantId), menu.id());
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
        //todo: startDate<=endDate
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
}