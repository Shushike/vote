package ru.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.model.Menu;
import ru.topjava.model.Vote;
import ru.topjava.repository.MenuRepository;
import ru.topjava.repository.VoteRepository;
import ru.topjava.util.exception.ModifyForrbidenException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.topjava.util.ValidationUtil.checkNotFound;
import static ru.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService{
    private static final Logger innerLog = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;

    public VoteService(VoteRepository repository, MenuRepository menuRepository) {
        voteRepository = repository;
        this.menuRepository = menuRepository;
    }

    public Vote create(Vote vote, int menuId, int userId){
        Assert.notNull(vote, "Vote must not be null");
        //checkNew - проверялось, что ид нул
        //можно проверить, что vote.menuId==null/userId==null
        return voteRepository.save(vote, menuId, userId);
    }

    public Vote create(int menuId, int userId){
        return voteRepository.create(menuId, userId);
    }

    public void update(Vote vote, int menuId, int userId) {
        Assert.notNull(vote, "Vote must not be null");
        Menu menu = menuRepository.get(menuId);
        innerLog.warn(menu.getDate()+" "+String.valueOf(LocalDate.now().isBefore(menu.getDate())));
        if (LocalDate.now().isBefore(menu.getDate())){
            innerLog.debug("Can change vote");
        }
        else {
            innerLog.debug("Need to check time");
            //проверить, что раньше 11 часов меню
            /*If it is before 11:00 we assume that he changed his mind.
              If it is after 11:00 then it is too late, vote can't be changed*/
            if (!LocalDateTime.now().isBefore(LocalDateTime.of(menu.getDate(), LocalTime.of(11,0))))
                throw new ModifyForrbidenException("Vote can't be changed");
        }

        checkNotFound(voteRepository.save(vote, menuId, userId), String.format("menu #%s by user #%s", menuId, userId));
        voteRepository.save(vote, menuId, userId);
    }

    public Vote get(int id){
        return checkNotFoundWithId(voteRepository.get(id), id);
    }

    public Vote get(int menuId, int userId){
        //? права доступа к чужим голосам
        return checkNotFound(voteRepository.get(menuId, userId), String.format("menu #%s by user #%s", menuId, userId));
    }

    public void delete(int menuId, int userId){
        checkNotFound(voteRepository.delete(menuId, userId), String.format("menu #%s by user #%s", menuId, userId));
    }

    public List<Vote> getAllForMenu(int menuId) {
        return voteRepository.getAllForMenu(menuId);
    }
    public List<Vote> getAllForUser(int userId) {
        return voteRepository.getAllForUser(userId);
    }
}