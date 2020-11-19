package ru.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository  extends JpaRepository<Restaurant, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    List<Restaurant> getByAddress(@Param("address") String address);

    @EntityGraph(attributePaths = {"menus"})
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH Menu m ON (m.restaurant.id=r.id) INNER JOIN Vote v on (v.menu.id=m.id) WHERE v.user.id=?1 ORDER BY r.name")
    List<Restaurant> getAllVoited(int userId);

    @Query("SELECT count(r) FROM Restaurant r INNER JOIN Menu m ON (m.restaurant.id=r.id) INNER JOIN Vote v on (v.menu.id=m.id) WHERE r.id=?1 AND m.date=?2")
    int getVoteCount(int restaurantId, LocalDate localDate);
}
