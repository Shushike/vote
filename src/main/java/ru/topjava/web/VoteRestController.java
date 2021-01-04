package ru.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.topjava.model.Vote;
import ru.topjava.service.VoteService;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    static final String REST_URL = "/rest/profile/votes";
    static final String PROFILE_URL = MenuRestController.PROFILE_URL;
    private final Logger log = LoggerFactory.getLogger(VoteRestController.class);

    @Autowired
    private VoteService service;

    @GetMapping(REST_URL+"/{id}")
    public Vote get(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Get vote {} for user {}", id, userId);
        return service.getById(id, userId);
    }

    @DeleteMapping(REST_URL+"/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete vote {} for user {}", id, userId);
        service.delete(id, userId);
    }

    @GetMapping(REST_URL)
    public List<Vote> getAll() {
        //без меню в ответе не имеет смысла
        int userId = SecurityUtil.authUserId();
        log.info("Get all votes for user {}", userId);
        return service.getAllForUser(userId);
    }

    @PutMapping(value = PROFILE_URL + "/{menuId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Vote updateVote(@PathVariable int menuId) {
        int userId = SecurityUtil.authUserId();
        log.info("Vote for menu #{} by user {}", menuId, userId);

        return service.create(menuId, userId);
    }
}