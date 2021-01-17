package ru.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Menu;
import ru.topjava.repository.BaseRepository;
import ru.topjava.repository.IMenuVote;
import ru.topjava.repository.IVotesNumber;
import ru.topjava.util.ValidationUtil;
import ru.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer>, BaseRepository<Menu> {

    @Override
    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.restaurant.id=:restaurantId AND m.id=:id")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @Override
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dish WHERE m.id=:id")
    Menu get(@Param("id") int id);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dish WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    Menu get(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM Menu m ORDER BY m.date DESC, m.id")
    List<Menu> getAll();

    @EntityGraph(value = "menu-complex-graph")
    @Query("SELECT DISTINCT m FROM Menu m WHERE m.date=:menuDate ORDER BY m.restaurant.id")
    List<Menu> getAllByDate(@Param("menuDate") LocalDate menuDate);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC")
    List<Menu> getAllByRestaurant(@Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"vote", "dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.date >= :startDate AND m.date <= :endDate ORDER BY m.date DESC, m.restaurant.name ASC")
    List<Menu> getBetweenInclude(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m AS menu, v as userVote FROM Menu m LEFT OUTER JOIN Vote v ON (m.id=v.menu.id AND v.user.id =:userId) WHERE m.date >= :startDate AND m.date <= :endDate ORDER BY m.date DESC, m.restaurant.name ASC")
    List<IMenuVote> getBetweenIncludeWithUserVote(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") int userId);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dish WHERE m.restaurant.id=:restaurantId AND m.date=:menuDate")
    List<Menu> getByDate(@Param("restaurantId") int restaurantId, @Param("menuDate") LocalDate menuDate);

    @Query("SELECT m.restaurant.id AS restaurantId, COUNT(v) AS voteNumber, m.date AS menuDate FROM Menu m LEFT JOIN Vote v ON (v.menu.id=m.id) WHERE m.date=:menuDate GROUP BY m.restaurant.id ORDER BY m.restaurant.name ASC")
    List<IVotesNumber> getNumbersByDate(@Param("menuDate") LocalDate menuDate);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m INNER JOIN Vote v ON (v.menu.id=m.id) WHERE v.user.id=:userId AND m.date >= :startDate AND m.date <= :endDate ORDER BY m.date DESC")
    List<Menu> getVotedBetweenDateForUser(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") int userId);

    @Query("SELECT m.date AS menuDate, m.restaurant.id AS restaurantId, COUNT(v) AS voteNumber FROM Menu m LEFT JOIN Vote v ON (v.menu.id=m.id) WHERE m.date>=:startDate AND m.date<=:endDate GROUP BY m.date, m.restaurant.id ORDER BY m.date DESC, m.restaurant.name ASC")
    List<IVotesNumber> getVoteNumbers(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    default Menu findEntityById(int menuId) throws NotFoundException {
        return ValidationUtil.checkNotFoundWithId(this.findById(menuId).orElse(null), "menu", menuId);
    }
}
