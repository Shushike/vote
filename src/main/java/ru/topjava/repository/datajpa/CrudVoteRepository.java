package ru.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.user.id=:userId AND v.menu.id=:menuId")
    int deleteForMenu(@Param("menuId") int menuId,
                      @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.user.id=:userId AND v.id=:id")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE (:exceptId IS null OR v.id<>:exceptId) AND v.id IN (SELECT vA.id FROM Vote vA WHERE vA.user.id = :userId AND v.menu.date = :menuDate)")
    int deleteForUserByDate(@Param("userId") int userId,
                            @Param("menuDate") LocalDate menuDate,
                            @Param("exceptId") Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.id IN (SELECT vA.id FROM Vote vA WHERE vA.user.id = :userId AND vA.menu.date = (SELECT m.date FROM Menu m WHERE m.id=:menuId))")
    int deleteForUserByDate(@Param("userId") int userId,
                            @Param("menuId") int menuId);

    @Modifying
    @Transactional
        //execute a read query, then delete each of the items one by one
    int removeByUserIdAndMenuDate(@Param("userId") int userId, @Param("menuDate") LocalDate menuDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.menu.id=:menuId")
    Vote get(@Param("menuId") int menuId,
             @Param("userId") int userId);

    @Query("SELECT vA FROM Vote vA WHERE vA.user.id = :userId AND vA.menu.date = (SELECT m.date FROM Menu m WHERE m.id=:menuId)")
    List<Vote> check(@Param("menuId") int menuId,
                     @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.id=:id")
    Vote get(@Param("id") int id);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.menu WHERE v.user.id=:userId ORDER BY v.voteTime DESC")
    List<Vote> getAllForUser(@Param("userId") int userId);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.user WHERE v.menu.id=:menuId ORDER BY v.voteTime DESC")
    List<Vote> getAllForMenu(@Param("menuId") int menuId);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.menu WHERE v.id IN (SELECT vA.id FROM Vote vA WHERE vA.user.id = :userId AND v.menu.date = :menuDate)")
    Vote getForUserByDate(@Param("userId") int userId,
                          @Param("menuDate") LocalDate menuDate);
}
