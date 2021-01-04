package ru.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.model.Vote;
import ru.topjava.service.VoteService;
import ru.topjava.to.MenuTo;

import static ru.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.util.ValidationUtil.checkNew;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
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
        //assureIdConsistent(menu, id);
        log.info("Vote for menu #{} by user {}", menuId, userId);

        return service.create(menuId, userId);
    }

/*
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Vote vote, @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(vote, id);
        log.info("Update vote {} for user {}", vote, userId);
        service.update(vote, userId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@RequestBody Vote vote) {

        //?? как в json укладывается/ть menuId
        int userId = SecurityUtil.authUserId();
        checkNew(vote);
        log.info("Create vote {} for user {}", vote, userId);
        Vote created = service.create(vote, userId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }*/


/*    @GetMapping("/filter")
    public List<Vote> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }*/
}