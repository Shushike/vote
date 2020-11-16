package ru.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.topjava.model.Restaurant;
import ru.topjava.repository.RestaurantRepository;
import ru.topjava.service.RestaurantService;

import java.util.List;

import static ru.topjava.util.ValidationUtil.*;

@Controller
public class RestaurantController extends BaseController<Restaurant> {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service){
        super(service);
        this.service = service;
    }

    public Restaurant create(Restaurant restaurant) {
        int userId = SecurityUtil.authUserId();
        checkRights(SecurityUtil.authUserIsAdmin());
        log.info("create {} by user #{}", restaurant, userId);
        return service.create(restaurant);
    }

    public void update(Restaurant restaurant, int id) {
        int userId = SecurityUtil.authUserId();
        checkRights(SecurityUtil.authUserIsAdmin());
        assureIdConsistent(restaurant, id);
        log.info("update {} for user #{}", restaurant, userId);
        service.update(restaurant);
    }

    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return service.getAll();
    }

    public List<Restaurant> getAllVoted() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll restaurants with menu for user #{}", userId);
        return service.getAllVoted(userId);
    }


}
