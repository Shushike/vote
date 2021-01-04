package ru.topjava.web;

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

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    static final String COMMON_URL = "/rest/restaurants";
    static final String ADMIN_URL = "/rest/admin/restaurants";
    private final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);

    //all actions can be done only by administrator but get

    @Autowired
    private RestaurantService service;

    @GetMapping(value = {COMMON_URL + "/{id}", ADMIN_URL + "/{id}"})
    public Restaurant get(@PathVariable int id) {
        log.info("Get restaurant {}", id);
        return service.get(id);
    }

    @GetMapping(value = {COMMON_URL + "/{id}/complex", ADMIN_URL + "/{id}/complex"})
    public Restaurant getWholeInfo(@PathVariable int id) {
        log.info("Get complex info about restaurant {}", id);
        return service.getWholeInfo(id);
    }

    @GetMapping(produces = {MediaType.TEXT_PLAIN_VALUE}, value = {COMMON_URL + "/{id}/votes-number", ADMIN_URL + "/{id}/votes-number"})
    public String getVotesNumber(@PathVariable int id,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("Get votes number for restaurant {} on date {}", id, voteDate);
        return String.valueOf(service.getVotesNumber(id, voteDate));
    }

    @DeleteMapping(ADMIN_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete restaurant {} by user {}", id, userId);
        service.delete(id);
    }

    @GetMapping(value = {COMMON_URL, ADMIN_URL})
    public List<Restaurant> getAll() {
        return service.getAll();
    }

    @PutMapping(value = ADMIN_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        //checkRights(SecurityUtil.authUserIsAdmin());
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(restaurant, id);
        log.info("Update {} by user {}", restaurant, userId);
        service.update(restaurant);
    }

    @PostMapping(value = ADMIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody Restaurant restaurant) {
        //checkRights(SecurityUtil.authUserIsAdmin());
        int userId = SecurityUtil.authUserId();
        log.info("Create {} by user {}", restaurant, userId);
        Restaurant created = service.create(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    /*
    * Returns list of restaurants with menu
    * */
    @GetMapping(value = {COMMON_URL+"/filter", ADMIN_URL+"/filter"})
    public List<Restaurant> getBetween(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.getBetween(startDate, endDate);
    }
}