package ru.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Vote;
import ru.topjava.model.VotingKey;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, VotingKey> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.menu WHERE v.user.id=:userId ORDER BY v.voteTime DESC")
    List<Vote> getAllForUser(@Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.menu.id=:menuId ORDER BY v.voteTime DESC")
    List<Vote> getAllForMenu(@Param("menuId") int menuId);
}
