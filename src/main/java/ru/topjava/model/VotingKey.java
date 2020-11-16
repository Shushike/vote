package ru.topjava.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VotingKey implements Serializable {

    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "user_id")
    private Integer userId;

    public VotingKey(){
    }

    public VotingKey(Integer menuId, Integer userId){
        this.menuId = menuId;
        this.userId = userId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VotingKey that = (VotingKey) o;
        return Objects.equals(menuId, that.menuId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, userId);
    }
}
