package ru.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Vote;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.user.id=:userId and v.menu.id=:menuId")
    int delete(@Param("menuId") int menuId,
               @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId and v.menu.id=:menuId")
    Vote get(@Param("menuId") int menuId,
            @Param("userId") int userId);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.menu WHERE v.user.id=:userId ORDER BY v.voteTime DESC")
    List<Vote> getAllForUser(@Param("userId") int userId);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.user WHERE v.menu.id=:menuId ORDER BY v.voteTime DESC")
    List<Vote> getAllForMenu(@Param("menuId") int menuId);
}
