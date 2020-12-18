package ru.topjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.LazyInitializationException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Transient;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@NamedQueries({
        @NamedQuery(name = Menu.DELETE, query = "DELETE FROM Menu m WHERE m.id=:id"),
        @NamedQuery(name = Menu.BY_DATE, query = "SELECT DISTINCT (m) FROM Menu m LEFT JOIN FETCH m.dish WHERE m.date=?1"),
        @NamedQuery(name = Menu.ALL_SORTED, query = "SELECT m FROM Menu m ORDER BY m.date DESC, m.id"),
})
@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "menu_restaurant_date_idx")})
public class Menu extends AbstractBaseEntity {

    public static final String DELETE = "Menu.delete";
    public static final String BY_DATE = "Menu.getByDate";
    public static final String ALL_SORTED = "Menu.getAllSorted";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonBackReference
    private Restaurant restaurant;

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate date;

    @CollectionTable(name = "menu_dish", joinColumns = @JoinColumn(name = "menu_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Dish> dish;

    @Size(min = 2, max = 120)
    @Column(name = "description", nullable = true)
    protected String description;

    //@CollectionTable(name = "vote", joinColumns = @JoinColumn(name = "menu_id"))
    /*@JoinColumns({
            @JoinColumn(name = "vote_menu_id", referencedColumnName = "vote_menu_id", insertable = false, updatable = false)
            ,
            @JoinColumn(name = "vote_user_id", referencedColumnName = "vote_user_id", insertable = false, updatable = false)
    })*/
    @OneToMany(mappedBy = "menu", targetEntity = Vote.class)
    @JsonManagedReference
    private Set<Vote> vote;

    public Menu() {
    }

    public Menu(Integer id, LocalDate localDate, Collection<Dish> dishes, String description) {
        super(id);
        this.date = localDate;
        this.description = description;
        setDishes(dishes);
    }

    public Menu(Menu menu) {
        this(menu.getId(), menu.getDate(), menu.getDishes(),
                menu.getDescription());
    }

    public LocalDate getDate() {
        return date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getDescription() {
        return description;
    }

    public Set<Dish> getDishes() {
        return dish;
    }

    public Set<Vote> getVotes() {
        return vote;
    }

    public void setVote(Set<Vote> votes) {
        this.vote = CollectionUtils.isEmpty(votes) ? new HashSet<>() : Set.copyOf(votes);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDishes(Collection<Dish> dishes) {
        this.dish = CollectionUtils.isEmpty(dishes) ? new HashSet<>() : Set.copyOf(dishes);
    }

    public boolean isVotesLoaded() {
        try {
            if (getVotes() != null)
                getVotes().isEmpty();
        } catch (LazyInitializationException e) {
            return false;
        }
        return getVotes() != null;
    }

    public boolean isDishesLoaded() {
        try {
            getDishes().isEmpty();
        } catch (LazyInitializationException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String subElementStart = "\n\t\t";
        String listStart = "\n\t";
        return "Menu{" +
                "id=" + id +
                ", date=" + date +
                ", description=" + String.valueOf(description) +
                (listStart + "Dishes: " + (isDishesLoaded() ?
                        (getDishes().isEmpty() ? EMPTY :
                                subElementStart + dish.stream().map(Dish::toString).collect(Collectors.joining("," + subElementStart))) :
                        WAS_NOT_LOADED)) +
                (listStart + "Votes: " + (isVotesLoaded() ?
                        (getVotes().isEmpty() ? EMPTY :
                                subElementStart + vote.stream().map(Vote::toString).collect(Collectors.joining("," + subElementStart))) :
                        WAS_NOT_LOADED)) +
                '}';
    }
}
