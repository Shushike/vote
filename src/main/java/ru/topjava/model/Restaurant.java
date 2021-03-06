package ru.topjava.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "complex-graph",
                attributeNodes = {
                        @NamedAttributeNode(value = "menus", subgraph = "subgraph.dish"),
                        @NamedAttributeNode(value = "dishes")
                },
                subgraphs = {
                        @NamedSubgraph(name = "subgraph.dish",
                                attributeNodes = @NamedAttributeNode(value = "dish"))
                }
        )
})
public class Restaurant extends AbstractNamedDescribedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    //@JsonManagedReference(value = "restaurant-menus")
    private Set<Menu> menus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference(value = "restaurant-dish")
    private Set<Dish> dishes;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, String address, String description) {
        super(id, name, description);
        this.address = address;
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getDescription());
    }

    public String getAddress() {
        return address;
    }

    public Set<Menu> getMenus() {
        return menus == null ? Collections.emptySet() : menus;
    }

    public Set<Dish> getDishes() {
        return dishes == null ? Collections.emptySet() : dishes;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name=" + name +
                ", address=" + address +
                ", description=" + description +
                '}';
    }
}

