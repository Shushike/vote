package ru.topjava.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menudate"}, name = "menu_restaurant_date_idx")})
public class Menu extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "menudate", nullable = false)
    @NotNull
    private LocalDate date;

    /*
    @Column(name = "dish_list")
    @ElementCollection
    @CollectionTable(name = "dish", joinColumns = @JoinColumn(name = "id"))
    @Transient
    private List<Integer> dishList;*/
    /*
        @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "COMPUTERS", joinColumns = @JoinColumn(name = "ID_IMAGE"))
    private List<Computer> computers;
    * */


    @Size(min = 2, max = 120)
    @Column(name = "description", nullable = true)
    protected String description;

    public Menu(){
    }

    public Menu(Integer id, LocalDate localDate, List<Integer> dishList, String description){
        super(id);
        this.date = localDate;
        //this.dishList = dishList;
        this.description = description;
    }

    public Menu(Menu menu){
        this(menu.getId(), menu.getDate(), null, //menu.getDishList(),
                 menu.getDescription());
    }

    public LocalDate getDate(){
        return date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getDescription() {
        return description;
    }

   /* public List<Integer> getDishList() {
        return dishList;
    }*/

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /*public void setDishList(List<Integer> dishList) {
        this.dishList = dishList;
    }*/

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", date=" + date +
              //  ", dishes=" + dishList +
                ", description=" + String.valueOf(description) +
                '}';
    }
}
