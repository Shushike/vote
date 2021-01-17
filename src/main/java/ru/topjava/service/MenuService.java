package ru.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import ru.topjava.model.Menu;
import ru.topjava.repository.IMenuVote;
import ru.topjava.repository.IVotesNumber;
import ru.topjava.repository.datajpa.*;
import ru.topjava.util.exception.ModifyForbiddenException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.topjava.util.DateUtil.atDayOrMax;
import static ru.topjava.util.DateUtil.atDayOrMin;
import static ru.topjava.util.ValidationUtil.checkNew;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService extends RepositoryService<Menu> {

    private static final Logger innerLog = LoggerFactory.getLogger(MenuService.class);
    private final CrudMenuRepository crudRepository;
    private final CrudDishRepository dishRepository;
    private final CrudRestaurantRepository restaurantRepository;
    private final VoteService voteService;

    public MenuService(CrudMenuRepository crudRepository, CrudDishRepository dishRepository, CrudRestaurantRepository restaurantRepository, VoteService voteService) {
        super(crudRepository);
        this.crudRepository = crudRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.voteService = voteService;
    }

    public Menu create(Menu menu, int restaurantId) {
        Assert.notNull(menu, "Menu must not be null");
        checkNew(menu);
        return save(menu, restaurantId);
    }

    @Transactional
    public Menu save(Menu menu, int restaurantId) {
        menu.setRestaurant(restaurantRepository.findEntityById(restaurantId));
        menu.setDishes(CollectionUtils.isEmpty(menu.getDishes()) ? menu.getDishes() :
                dishRepository.filter(menu.getDishes().stream().map(dish -> dish.getId()).collect(Collectors.toList()),
                        restaurantId));
        return crudRepository.save(menu);
    }

    public Menu update(Menu menu, int restaurantId) {
        Assert.notNull(menu, "Menu must not be null");
        LocalDate oldDate = crudRepository.get(menu.getId()).getDate();
        //if menu date is changing and this menu already has votes, menu modifying is forbidden
        if (oldDate != null && !oldDate.equals(menu.getDate())) {
            innerLog.debug("Menu date is changing (old value {}, new value {}), check votes", oldDate, menu.getDate());
            boolean menuHasVotes = voteService.menuHasVotes(menu.id());
            if (menuHasVotes)
                throw new ModifyForbiddenException(String.format("Menu #%s on date %s already has votes", menu.id(), menu.getDate()));
        }
        return checkNotFoundWithId(save(menu, restaurantId), menu.id());
    }

    public boolean delete(int id, int restaurantId) {
        return checkNotFoundWithId(crudRepository.delete(id, restaurantId)!=0, id);
    }

    public Menu get(int id, int restaurantId) {
        return checkNotFoundWithId(crudRepository.get(id, restaurantId), id);
    }

    public List<Menu> getAll(int restaurantId) {
        return crudRepository.getAllByRestaurant(restaurantId);
    }

    /**
     * Return list of menus with dishes for date in all restaurants.
     */
    public List<Menu> getAllByDate(LocalDate localDate) {
        return crudRepository.getAllByDate(localDate);
    }

    /**
     * Return menu with dish list for date and restaurant.
     * Null if not found
     */
    public Menu getByDate(int restaurantId, LocalDate localDate) {
        List<Menu> tempList = crudRepository.getByDate(restaurantId, localDate);
        return tempList!=null && !tempList.isEmpty() ? tempList.get(0): null;
    }

    public List<Menu> getAllByRestaurant(int restaurantId) {
        return crudRepository.getAllByRestaurant(restaurantId);
    }

    /**
     * Return menu list for all restaurants without dishes and restaurant.
     * Ordered by descending menu date and ascending ID
     */
    public List<Menu> getAll() {
        return crudRepository.getAll();
    }

    /**
     * Return list of menus which user voted
     * between dates with dish list
     */
    public List<Menu> getVotedBetweenDateForUser(LocalDate startDate, LocalDate endDate, int userId) {
        return crudRepository.getVotedBetweenDateForUser(startDate, endDate, userId);
    }

    /**
     * Return list of pairs restaurantId and votes number for date
     * */
    public List<IVotesNumber> get(LocalDate localDate) {
        return crudRepository.getNumbersByDate(localDate);
    }

    /**
     * Return list of pairs restaurantId and votes number for date range
     * */
    public List<IVotesNumber> get(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return crudRepository.getVoteNumbers(atDayOrMin(startDate), atDayOrMax(endDate));
    }

    /**
     * Return list of menus with dishes for date in all restaurants.
     */
    public List<Menu> getAllForPeriod(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return crudRepository.getBetweenInclude(atDayOrMin(startDate), atDayOrMax(endDate));
    }

    /**
     * Return list of pairs menu (with dishes) and vote for period in all restaurants.
     * Vote defined if user voted for menu, otherwise vote is null
     */
    public List<IMenuVote> getAllForPeriod(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return crudRepository.getBetweenIncludeWithUserVote(atDayOrMin(startDate), atDayOrMax(endDate), userId);
    }
}