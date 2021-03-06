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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.model.Restaurant;
import ru.topjava.service.RestaurantService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.topjava.util.ValidationUtil.assureIdConsistent;

@Api(tags = {"Restaurant controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Restaurant controller", description = "Provides operations with restaurants")
})
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {

    static final String COMMON_URL = "/rest/restaurants";
    static final String ADMIN_URL = "/rest/admin/restaurants";
    private final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);

    //all actions can be done only by administrator but get

    @Autowired
    private RestaurantService service;

    @ApiOperation(value = "Get by ID")
    @GetMapping(value = {COMMON_URL + "/{id}", ADMIN_URL + "/{id}"})
    public Restaurant get(@PathVariable int id) {
        log.info("Get restaurant #{}", id);
        return service.get(id);
    }

    @ApiOperation(value = "Get complex information with dishes and menus")
    @GetMapping(value = {COMMON_URL + "/{id}/complex", ADMIN_URL + "/{id}/complex"})
    public Restaurant getWholeInfo(@PathVariable int id) {
        log.info("Get complex info about restaurant #{}", id);
        return service.getWholeInfo(id);
    }

    @ApiOperation(value = "Get number of votes on date for restaurant", notes = "Return amount of votes")
    @GetMapping(produces = {MediaType.TEXT_PLAIN_VALUE}, value = {COMMON_URL + "/{id}/votes-number", ADMIN_URL + "/{id}/votes-number"})
    public String getVotesNumber(@ApiParam(type = "Integer", value = "Restaurant ID", example = "100003", required = true)
                                 @PathVariable int id,
                                 @ApiParam(type = "Date", value = "Date on which number of votes is asked", example = "2020-11-04", required = true)
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("Get votes number for restaurant #{} on date {}", id, voteDate);
        return String.valueOf(service.getVotesNumber(id, voteDate));
    }

    @ApiOperation(value = "Delete by ID")
    @DeleteMapping(ADMIN_URL + "/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete restaurant #{} by user #{}", restaurantId, userId);
        service.delete(restaurantId);
    }

    @ApiOperation(value = "Get list of restaurants")
    @GetMapping(value = {COMMON_URL, ADMIN_URL})
    public List<Restaurant> getAll() {
        return service.getAll();
    }

    @ApiOperation(value = "Update")
    @PutMapping(value = ADMIN_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(restaurant, id);
        log.info("Update {} by user #{}", restaurant, userId);
        service.update(restaurant);
    }

    @ApiOperation(value = "Create")
    @PostMapping(value = ADMIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody Restaurant restaurant) {
        //checkRights(SecurityUtil.authUserIsAdmin());
        int userId = SecurityUtil.authUserId();
        log.info("Create {} by user #{}", restaurant, userId);
        Restaurant created = service.create(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    /**
     * Returns list of restaurants with menu
     * */
    @ApiOperation(value = "Get list of restaurants with menu for period")
    @GetMapping(value = {COMMON_URL + "/filter", ADMIN_URL + "/filter"})
    public List<Restaurant> getBetween(
            @ApiParam(type = "Date", value = "Start date of period", example = "2020-11-04")
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @ApiParam(type = "Date", value = "End date of period", example = "2020-12-04")
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.getBetween(startDate, endDate);
    }
}