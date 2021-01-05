package ru.topjava.web;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.model.Dish;
import ru.topjava.service.DishService;

import java.net.URI;
import java.util.List;

import static ru.topjava.util.ValidationUtil.*;

@Api(tags = {"Dish controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Dish controller", description = "Provides operations with dishes")
})
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {
    static final String COMMON_URL = "/rest/restaurants";
    static final String COMMON_REST_URL = COMMON_URL + "/{restaurantId}/dishes";
    static final String COMMON_MENU_URL = COMMON_URL + "/{restaurantId}/menus/{menuId}/dishes";
    static final String ADMIN_URL = "/rest/admin/restaurants";
    static final String ADMIN_REST_URL = ADMIN_URL + "/{restaurantId}/dishes";
    private final Logger log = LoggerFactory.getLogger(DishRestController.class);

    @Autowired
    private DishService service;

    @ApiOperation(value = "Get dish by ID for restaurant")
    @GetMapping(value = {COMMON_REST_URL + "/{id}", ADMIN_REST_URL + "/{id}"})
    public Dish get(@PathVariable int restaurantId,
                    @PathVariable int id) {
        log.info("Get dish #{} for restaurant #{}", id, restaurantId);
        return service.get(id, restaurantId);
    }

    @ApiOperation(value = "Delete dish by ID for restaurant")
    @DeleteMapping(ADMIN_REST_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId,
                       @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete dish #{} by user #{}", id, userId);
        service.delete(id, restaurantId);
    }

    @ApiOperation(value = "Get dishes",
            notes = "Return dishes list for restaurant or for menu. For first case list can be filtered by dish name and ordered by price. " +
                    "In second case menuId must be defined in the request. ")
    @GetMapping(value = {COMMON_REST_URL, ADMIN_REST_URL, COMMON_MENU_URL})
    public List<Dish> getAll(@ApiParam(type = "Integer", value = "Restaurant ID", example = "100003", required = true)
                             @PathVariable int restaurantId,
                             @ApiParam(type = "Integer", value = "Menu ID", example = "100012")
                             @PathVariable(required = false) Integer menuId,
                             @ApiParam(type = "String", value = "Dish name for filtering ", example = "%bread")
                             @RequestParam @Nullable String name) {
        if (menuId == null) {
            if (name == null) {
                log.info("Get all dishes for restaurant #{}", restaurantId);
                return service.getAll(restaurantId);
            } else {
                log.info("Get dishes {} for restaurant #{}", name, restaurantId);
                return service.getByName(restaurantId, name);
            }
        } else {
            log.info("Get all dishes for menu #{}", menuId);
            return service.getForMenu(menuId, restaurantId);
        }
    }

    @ApiOperation(value = "Update dish by ID for restaurant")
    @PutMapping(value = ADMIN_REST_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(dish, id);
        log.info("Update {} by user #{} for restaurant #{}", dish, userId, restaurantId);
        service.update(dish, restaurantId);
    }

    @ApiOperation(value = "Create dish for restaurant")
    @PostMapping(value = ADMIN_REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@RequestBody Dish dish, @PathVariable int restaurantId) {
        int userId = SecurityUtil.authUserId();
        checkNew(dish);
        log.info("Create {} by user #{} for restaurant #{}", dish, userId, restaurantId);
        Dish created = service.create(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}