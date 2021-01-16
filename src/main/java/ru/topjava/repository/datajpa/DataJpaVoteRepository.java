package ru.topjava.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Menu;
import ru.topjava.model.Vote;
import ru.topjava.repository.VoteRepository;
import ru.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Logger innerLog = LoggerFactory.getLogger(DataJpaVoteRepository.class);

    private final CrudVoteRepository crudRepository;
    private final CrudMenuRepository menuRepository;
    private final CrudUserRepository userRepository;

    public DataJpaVoteRepository(CrudVoteRepository crudRepository, CrudMenuRepository menuRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Vote save(Vote vote, int menuId, int userId) {
        //check is this vote of user
        if (!vote.isNew() &&
                getById(vote.id(), userId) == null) {
            innerLog.debug("{} can not be update", vote);
            return null;
        }
        final Menu menu = menuRepository.findEntityById(menuId);
        vote.setMenu(menu);
        vote.setUser(userRepository.findEntityById(userId));
        return crudRepository.save(vote);
    }

    @Override
    @Transactional
    public Vote create(int menuId, int userId) throws NotFoundException {
        Vote newVote = new Vote(menuRepository.findEntityById(menuId), userRepository.findEntityById(userId));
        return crudRepository.save(newVote);
    }

    @Override
    @Transactional
    public boolean deleteForMenu(int menuId, int userId) {
        return crudRepository.deleteForMenu(menuId, userId) != 0;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Vote getById(int id, int userId) {
        return crudRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public Vote get(int menuId, int userId) {
        return crudRepository.get(menuId, userId);
    }

    @Override
    public Vote getByDate(int userId, LocalDate menuDate) {
        if (menuDate == null)
            return null;
        return crudRepository.getForUserByDate(userId, menuDate);
    }

    @Override
    public List<Vote> getAllForUser(int userId) {
        return crudRepository.getAllForUser(userId);
    }

    @Override
    public List<Vote> getAllForMenu(int menuId) {
        return crudRepository.getAllForMenu(menuId);
    }

    @Override
    public boolean menuHasVotes(int menuId) {
        return crudRepository.menuHasVotes(menuId)>0;
    }
}
