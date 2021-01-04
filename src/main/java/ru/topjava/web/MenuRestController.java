package ru.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.View;
import ru.topjava.model.Menu;
import ru.topjava.repository.datajpa.IVotesNumber;
import ru.topjava.service.MenuService;
import ru.topjava.to.MenuTo;
import ru.topjava.to.StatisticsTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.topjava.util.DateUtil.*;
import static ru.topjava.util.ValidationUtil.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {
    static final String COMMON_URL = "/rest/restaurants/{restaurantId}/menus";
    static final String ADMIN_URL = "/rest/admin/restaurants/{restaurantId}/menus";
    static final String MENU_URL = "/rest/menus";
    static final String PROFILE_URL = "/rest/profile/menus";
    private final Logger log = LoggerFactory.getLogger(MenuRestController.class);

    @Autowired
    private MenuService service;

    @GetMapping(value = {COMMON_URL + "/{id}", ADMIN_URL + "/{id}"})
    public Menu get(@PathVariable int restaurantId,
                    @PathVariable int id) {
        log.info("Get menu {} for restaurant {}", id, restaurantId);
        return service.get(id, restaurantId);
    }

    @GetMapping(value = {RestaurantRestController.COMMON_URL + "/votes-number", RestaurantRestController.ADMIN_URL + "/votes-number"})
    public List<StatisticsTo> get(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate,
                                  @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<IVotesNumber> result;
        if (voteDate!=null) {
            log.info("Get vote numbers for date {}", voteDate);
            result = service.get(voteDate);
        }
        else{
            result = service.get(startDate, endDate);
        }
        return result.stream().map(data -> new StatisticsTo(data.getRestaurantId(), data.getVoteNumber(), data.getMenuDate())).collect(Collectors.toList());
    }

    @DeleteMapping(ADMIN_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId,
                       @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete menu {} by user {}", id, userId);
        service.delete(id, restaurantId);
    }

    @GetMapping(value = {ADMIN_URL, COMMON_URL})
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("Get all menus for restaurant {}", restaurantId);
        return service.getAll(restaurantId);
    }

    @PutMapping(value = ADMIN_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Menu update(@Validated(View.Web.class) @RequestBody Menu menu, @PathVariable int restaurantId, @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(menu, id);
        //can add dishes to the menu
        log.info("Update {} by user {} for restaurant {}", menu, userId, restaurantId);
        return service.update(menu, restaurantId);
    }

    @PostMapping(value = ADMIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Validated(View.Web.class) @RequestBody Menu menu, @PathVariable int restaurantId) {
        int userId = SecurityUtil.authUserId();
        checkNew(menu);
        log.info("Create {} by user {} for restaurant {}", menu, userId, restaurantId);

        Menu created = service.create(menu, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping(MENU_URL + "/by")
    public List<Menu> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("Get all menus for date {}", menuDate);
        return service.getAllByDate(menuDate);
    }

    @GetMapping(value ={ADMIN_URL+ "/by", COMMON_URL+ "/by"} )
    public Menu getOneMenuByDate(@PathVariable int restaurantId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("Get menu for #{} restaurant and date {}", restaurantId, menuDate);
        return service.getByDate(restaurantId, menuDate);
    }

    /*
     * Return list of menu for date with flag which shows was menu voted by current user or not
     * */
    @GetMapping(PROFILE_URL + "/by")
    public List<MenuTo> getByDateForUser(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        int userId = SecurityUtil.authUserId();
        log.info("Get menus for date {} and user {}", menuDate, userId);
        return service.getAllByDate(menuDate)
                .stream()
                .map(menu -> createTo(menu, menu.hasVote(userId)))
                .collect(Collectors.toList());
    }

    @GetMapping(PROFILE_URL + "/filter")
    public List<Menu> getVotedBetweenDateForUser(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int userId = SecurityUtil.authUserId();
        log.info("Get voted menus between {} and {} for user {}", startDate, endDate, userId);
        return service.getVotedBetweenDateForUser(atDayOrMin(startDate), atDayOrMax(endDate), userId);
    }

    public static MenuTo createTo(Menu menu, boolean voted) {
        return new MenuTo(menu.getId(), menu.getDate(), menu.getDescription(), menu.getDishes(), voted);
    }
}