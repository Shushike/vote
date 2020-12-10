package ru.topjava.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.LazyInitializationException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedDescriptedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 10, max = 100)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference
    private Set<Menu> menus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference
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
        return menus;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public boolean isMenusLoaded() {
        try {
            getMenus().isEmpty();
        } catch (LazyInitializationException e) {
            return false;
        }
        return true;
    }

    public boolean isDishesLoaded() {
        try {
            getDishes().isEmpty();
        } catch (LazyInitializationException e) {
            return false;
        }
        return true;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        String subElementStart = "\n\t\t";
        String listStart = "\n\t";
        return "Restaurant{" +
                "id=" + id +
                ", name=" + name +
                ", address=" + address +
                ", description=" + description +
                (listStart + "Dishes: " + (isDishesLoaded() ?
                        (getDishes().isEmpty()?"<empty>":
                        subElementStart + dishes.stream().map(Dish::toString).collect(Collectors.joining("," + subElementStart))):
                        "<was not loaded>")) +
                (listStart + "Menus: " + (isMenusLoaded() ?
                        (getMenus().isEmpty()?"<empty>":
                        subElementStart + menus.stream().map(Menu::toString).collect(Collectors.joining("," + subElementStart))) :
                        "<was not loaded>")) +
                '}';
    }
}
