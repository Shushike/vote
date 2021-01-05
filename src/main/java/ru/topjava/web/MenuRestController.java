package ru.topjava.web;

import io.swagger.annotations.*;
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

@Api(tags = {"Menu controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Menu controller", description = "Provides operations with menus")
})
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

    @ApiOperation(value = "Get menu for restaurant by ID")
    @GetMapping(value = {COMMON_URL + "/{id}", ADMIN_URL + "/{id}"})
    public Menu get(@PathVariable int restaurantId,
                    @PathVariable int id) {
        log.info("Get menu #{} for restaurant #{}", id, restaurantId);
        return service.get(id, restaurantId);
    }

    @ApiOperation(value = "Get statistics data for restaurants",
            notes = "Return statistics for date or period. In first case you need to set parameter voteDate, otherwise startDate and endDate")
    @GetMapping(value = {RestaurantRestController.COMMON_URL + "/votes-number", RestaurantRestController.ADMIN_URL + "/votes-number"})
    public List<StatisticsTo> get(
            @ApiParam(type = "Date", value = "Date to calculate statistics", example = "2020-11-04")
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate,
            @ApiParam(type = "Date", value = "Start date to collecting statistics", example = "2020-11-04")
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @ApiParam(type = "Date", value = "End date to collecting statistics", example = "2020-12-04")
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<IVotesNumber> result;
        if (voteDate != null) {
            log.info("Get vote numbers for date {}", voteDate);
            result = service.get(voteDate);
        } else {
            result = service.get(startDate, endDate);
        }
        return result.stream().map(data -> new StatisticsTo(data.getRestaurantId(), data.getVoteNumber(), data.getMenuDate())).collect(Collectors.toList());
    }

    @ApiOperation(value = "Delete menu by ID for restaurant")
    @DeleteMapping(ADMIN_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId,
                       @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete menu #{} by user #{}", id, userId);
        service.delete(id, restaurantId);
    }

    @ApiOperation(value = "Get list of menus for restaurant")
    @GetMapping(value = {ADMIN_URL, COMMON_URL})
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("Get all menus for restaurant #{}", restaurantId);
        return service.getAll(restaurantId);
    }

    @ApiOperation(value = "Update menu for restaurant", notes = "List of dishes is filtered by restaurant's ownership before updating")
    @PutMapping(value = ADMIN_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Menu update(@Validated(View.Web.class) @RequestBody Menu menu, @PathVariable int restaurantId, @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(menu, id);
        //can add dishes to the menu
        log.info("Update {} by user #{} for restaurant #{}", menu, userId, restaurantId);
        return service.update(menu, restaurantId);
    }

    @ApiOperation(value = "Create menu for restaurant")
    @PostMapping(value = ADMIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Validated(View.Web.class) @RequestBody Menu menu, @PathVariable int restaurantId) {
        int userId = SecurityUtil.authUserId();
        checkNew(menu);
        log.info("Create {} by user #{} for restaurant #{}", menu, userId, restaurantId);
        Menu created = service.create(menu, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Get all menus on date")
    @GetMapping(MENU_URL + "/by")
    public List<Menu> getByDate(@ApiParam(type = "Date", value = "Date on which menus are asked", example = "2020-11-04", required = true)
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("Get all menus for date {}", menuDate);
        return service.getAllByDate(menuDate);
    }

    @ApiOperation(value = "Get menu for restaurant on date")
    @GetMapping(value = {ADMIN_URL + "/by", COMMON_URL + "/by"})
    public Menu getOneMenuByDate(@ApiParam(type = "Integer", value = "Restaurant ID for which menu is asked", example = "100003", required = true)
                                 @PathVariable int restaurantId,
                                 @ApiParam(type = "Date", value = "Date on which menu is asked", example = "2020-11-04", required = true)
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("Get menu for #{} restaurant and date {}", restaurantId, menuDate);
        return service.getByDate(restaurantId, menuDate);
    }

    /*
     * Return list of menu for date or period with flag which shows was menu voted by current user or not
     * */
    @ApiOperation(value = "Get list of menu for date with vote flag",
            notes = "Return list of all menus for date or period with flag which shows was menu voted by current user or not. " +
                    "In first case you need to set parameter menuDate, otherwise startDate and endDate")
    @GetMapping(PROFILE_URL + "/by")
    public List<MenuTo> getByDateForUser(
            @ApiParam(type = "Date", value = "Date to get menus on", example = "2020-12-04")
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate,
            @ApiParam(type = "Date", value = "Start date to get menus", example = "2020-11-04")
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @ApiParam(type = "Date", value = "End date to get menus", example = "2020-12-04")
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int userId = SecurityUtil.authUserId();
        if (menuDate != null) {
            log.info("Get menus for date {} and user #{}", menuDate, userId);
            return createTos(service.getAllByDate(menuDate), userId);
        } else {
            log.info("Get menus from {} to {} and user #{}", startDate, endDate, userId);
            return createTos(service.getAllForPeriod(startDate, endDate), userId);
        }
    }

    @ApiOperation(value = "Get voted menus", notes = "Return list of menus which was voted by current user")
    @GetMapping(PROFILE_URL + "/filter")
    public List<Menu> getVotedBetweenDateForUser(@ApiParam(type = "Date", value = "Start date of period", example = "2020-11-04")
                                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                 @ApiParam(type = "Date", value = "End date of period", example = "2020-12-04")
                                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int userId = SecurityUtil.authUserId();
        log.info("Get voted menus between {} and {} for user #{}", startDate, endDate, userId);
        return service.getVotedBetweenDateForUser(atDayOrMin(startDate), atDayOrMax(endDate), userId);
    }

    private static MenuTo createTo(Menu menu, boolean voted) {
        return new MenuTo(menu.getId(), menu.getDate(), menu.getDescription(), menu.getDishes(), voted);
    }

    private static List<MenuTo> createTos(List<Menu> menus, int userId) {
        return menus.stream()
                .map(menu -> createTo(menu, menu.hasVote(userId)))
                .collect(Collectors.toList());
    }
}