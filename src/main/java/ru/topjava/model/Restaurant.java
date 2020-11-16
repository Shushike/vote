package ru.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedDescriptedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 10, max = 100)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Menu> menus;

    public Restaurant(){
    }

    public Restaurant(Integer id, String name, String address, String description){
        super(id, name, description);
        this.address = address;
    }

    public Restaurant(Restaurant restaurant){
        this(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getDescription());
    }

    public String getAddress(){
        return address;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setAddress(String address){
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
