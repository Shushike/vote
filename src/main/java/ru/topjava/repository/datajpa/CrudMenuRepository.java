package ru.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Menu;
import ru.topjava.util.ValidationUtil;
import ru.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.restaurant.id=:restaurantId and m.id=:id")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

    //если добавить rownum=1 будет ли один элемент в результате
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dish WHERE m.restaurant.id=:restaurantId AND m.date=:menuDate")
    List<Menu> getByDate(@Param("restaurantId") int restaurantId, @Param("menuDate") LocalDate menuDate);

    //LEFT JOIN FETCH m.vote
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC")
    List<Menu> getAllByRestaurant(@Param("restaurantId") int restaurantId);

    @EntityGraph(value = "menu-complex-graph")
    @Query("SELECT DISTINCT m FROM Menu m WHERE m.date=:menuDate ORDER BY m.restaurant.id")
    List<Menu> getAllByDate(@Param("menuDate") LocalDate menuDate);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dish WHERE m.id=:id")
    Menu get(@Param("id") int id);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dish WHERE m.id=:id and m.restaurant.id=:restaurantId")
    Menu get(@Param("id") int id, @Param("restaurantId") int restaurantId);

    //с голосованием и блюдами
    @Query("SELECT m FROM Menu m WHERE m.date >= :startDate AND m.date <= :endDate ORDER BY m.date DESC")
    List<Menu> getBetweenInclude(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m INNER JOIN Vote v on (v.menu.id=m.id) WHERE v.user.id=:userId AND m.date >= :startDate AND m.date <= :endDate ORDER BY m.date DESC")
    List<Menu> getVotedBetweenDateForUser(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") int userId);

    @Query("SELECT m FROM Menu m ORDER BY m.date DESC, m.id")
    List<Menu> getAll();

    @Query("SELECT m.restaurant.id as restaurantId, count(v) as voteNumber, m.date as menuDate FROM Menu m LEFT JOIN Vote v on (v.menu.id=m.id) WHERE m.date=:menuDate group by m.restaurant.id order by m.restaurant.id ASC")
    List<IVotesNumber> getNumbersByDate(@Param("menuDate") LocalDate menuDate);

    @Query("SELECT m.date as menuDate, m.restaurant.id as restaurantId, count(v) as voteNumber FROM Menu m LEFT JOIN Vote v on (v.menu.id=m.id) WHERE m.date>=:startDate and m.date<=:endDate group by m.date, m.restaurant.id order by m.date DESC, m.restaurant.id ASC")
    List<IVotesNumber> getVoteNumbers(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    default Menu findEntityById(int menuId) throws NotFoundException {
        return ValidationUtil.checkNotFoundWithId(this.findById(menuId).orElse(null), "menu", menuId);
    }
}
