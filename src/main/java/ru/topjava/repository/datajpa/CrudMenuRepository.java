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

    //если добавить rownum=1 будет ли один элемент в результате
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dish WHERE m.restaurant.id=:restaurantId AND m.date=:menuDate")
    List<Menu> getByDate(@Param("restaurantId") int restaurantId, @Param("menuDate") LocalDate menuDate);

    //LEFT JOIN FETCH m.vote
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC")
    List<Menu> getAllByRestaurant(@Param("restaurantId") int restaurantId);
    
    @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.dish WHERE m.date=:menuDate ORDER BY m.restaurant.id")
    List<Menu> getAllByDate(@Param("menuDate") LocalDate menuDate);

    //с голосованием и блюдами
    @Query("SELECT m FROM Menu m WHERE m.date >= :startDate AND m.date <= :endDate ORDER BY m.date DESC")
    List<Menu> getBetweenInclude(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT m FROM Menu m ORDER BY m.date DESC, m.id")
    List<Menu> getAll();

    default Menu findEntityById(int menuId) throws NotFoundException {
        return ValidationUtil.checkNotFoundWithId(this.findById(menuId).orElse(null), "menu", menuId);
    }
}
