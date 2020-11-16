package ru.topjava.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dish")
public class Dish extends AbstractNamedDescriptedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range
    private Integer price;

    public Dish() {
    }

    public Dish(Integer id, String name, Integer price, String description) {
        super(id, name, description);
        this.price = price;
    }


    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getPrice(), dish.getDescription());
    }

    public Integer getPrice() {
        return price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name=" + name +
                ", price=" + price +
                ", description=" + String.valueOf(description) +
                '}';
    }
}
