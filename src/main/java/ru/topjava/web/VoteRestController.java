package ru.topjava.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.topjava.model.Vote;
import ru.topjava.service.VoteService;
import java.util.List;

@Api(tags = {"Vote controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Vote controller", description = "Provides operations with votes")
})
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    static final String REST_URL = "/rest/profile/votes";
    static final String PROFILE_URL = MenuRestController.PROFILE_URL;
    private final Logger log = LoggerFactory.getLogger(VoteRestController.class);

    @Autowired
    private VoteService service;

    @ApiOperation(value = "Get by ID")
    @GetMapping(REST_URL+"/{id}")
    public Vote get(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Get vote #{} for user #{}", id, userId);
        return service.getById(id, userId);
    }

    @ApiOperation(value = "Delete by ID")
    @DeleteMapping(REST_URL+"/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete vote #{} for user #{}", id, userId);
        service.delete(id, userId);
    }

    @ApiOperation(value = "Get all votes of current user")
    @GetMapping(REST_URL)
    public List<Vote> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("Get all votes for user #{}", userId);
        return service.getAllForUser(userId);
    }

    @ApiOperation(value = "Vote for menu")
    @PutMapping(value = PROFILE_URL + "/{menuId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Vote updateVote(@PathVariable int menuId) {
        int userId = SecurityUtil.authUserId();
        log.info("Vote for menu #{} by user #{}", menuId, userId);
        return service.create(menuId, userId);
    }
}