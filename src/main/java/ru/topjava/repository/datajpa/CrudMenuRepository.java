package ru.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC")
    List<Menu> getAllForRestaurant(@Param("restaurantId") int restaurantId);

    //выборка с блюдами
    @Query("SELECT m FROM Menu m WHERE m.date=:menuDate")
    List<Menu> getAllForDate(@Param("menuDate") LocalDate menuDate);

    //с голосованием и блюдами
    @Query("SELECT m FROM Menu m WHERE m.date >= :startDate AND m.date <= :endDate ORDER BY m.date DESC")
    List<Menu> getBetweenInclude(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
