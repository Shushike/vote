package ru.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Vote;
import ru.topjava.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {

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
        vote.setMenu(menuRepository.getOne(menuId));
        vote.setUser(userRepository.getOne(userId));
        //должно быть get для проверки, что этот объект есть в бд, если это update

        return crudRepository.save(vote);
    }

    @Override
    @Transactional
    public Vote create(int menuId, int userId) {
        Vote newVote = new Vote(menuRepository.getOne(menuId), userRepository.getOne(userId));
        return crudRepository.save(newVote);
    }

    @Override
    @Transactional
    public boolean delete(int menuId, int userId) {
        return crudRepository.delete(menuId, userId) != 0;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Vote get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public Vote get(int menuId, int userId) {
        return crudRepository.get(menuId, userId);
    }

    @Override
    public List<Vote> getAllForUser(int userId) {
        //подгружать меню, рестораны
        return crudRepository.getAllForUser(userId);
    }

    @Override
    public List<Vote> getAllForMenu(int menuId) {
        //подгружать пользователей
        return crudRepository.getAllForMenu(menuId);
    }
}
