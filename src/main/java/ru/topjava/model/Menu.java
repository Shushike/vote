package ru.topjava.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.util.CollectionUtils;
import ru.topjava.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@NamedQueries({
        @NamedQuery(name = Menu.DELETE, query = "DELETE FROM Menu m WHERE m.id=:id"),
        @NamedQuery(name = Menu.BY_DATE, query = "SELECT DISTINCT (m) FROM Menu m LEFT JOIN FETCH m.dish LEFT JOIN FETCH m.vote WHERE m.date=?1"),
        @NamedQuery(name = Menu.ALL_SORTED, query = "SELECT m FROM Menu m ORDER BY m.date DESC, m.id"),
})
@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "menu_restaurant_date_idx")})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "menu-complex-graph",
                attributeNodes = {
                        @NamedAttributeNode(value = "dish"),
                        @NamedAttributeNode(value = "restaurant"),
                        @NamedAttributeNode(value = "vote", subgraph = "subgraph.users")
                },
                subgraphs = {
                        @NamedSubgraph(name = "subgraph.users",
                                attributeNodes = @NamedAttributeNode(value = "user"))
                }
        )
})
@SuppressWarnings("deprecation")
public class Menu extends AbstractBaseEntity {

    public static final String DELETE = "Menu.delete";
    public static final String BY_DATE = "Menu.getByDate";
    public static final String ALL_SORTED = "Menu.getAllSorted";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    //@JsonBackReference(value = "restaurant-menus")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Restaurant.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("restaurantId")
    // @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate date;

    @CollectionTable(name = "menu_dish", joinColumns = @JoinColumn(name = "menu_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Dish> dish;

    @Size(min = 2, max = 120)
    @Column(name = "description")
    @SafeHtml(groups = {View.Web.class}, whitelistType = SafeHtml.WhiteListType.NONE)
    protected String description;

    @OneToMany(mappedBy = "menu", targetEntity = Vote.class)
    //@JsonManagedReference(value = "menu-vote")
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

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", date=" + date +
                ", description=" + String.valueOf(description) +
                '}';
    }
}
