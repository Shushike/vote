package ru.topjava.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = Vote.BY_MENU, query = "SELECT v FROM Vote v WHERE v.menu.id=:menuId"),
        @NamedQuery(name = Vote.BY_USER, query = "SELECT v FROM Vote v WHERE v.user.id=:userId")
})
@Access(AccessType.FIELD)
@Entity
@Table(name = "vote")
public class Vote {

    public static final String BY_MENU = "Voice.getForMenu";
    public static final String BY_USER = "Voice.getForUser";

    @EmbeddedId
    private VotingKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;


    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime voteTime;

    public Vote(){
    }

    public Vote(Menu menu, User user){
        this.menu = menu;
        this.user = user;
        this.id = new VotingKey(menu.getId(), user.getId());
        voteTime = LocalDateTime.now();
    }

    public Menu getMenu() {
        return menu;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getVoteTime() {
        return voteTime;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVoteTime(LocalDateTime voteTime) {
        this.voteTime = voteTime;
    }
}
