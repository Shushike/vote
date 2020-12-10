package ru.topjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.stream.Collectors;

@NamedQueries({
        @NamedQuery(name = Vote.BY_MENU, query = "SELECT v FROM Vote v WHERE v.menu.id=:menuId"),
        @NamedQuery(name = Vote.BY_USER, query = "SELECT v FROM Vote v WHERE v.user.id=:userId")
})
@Access(AccessType.FIELD)
@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "user_id"}, name = "vote_unique_menu_user_idx")})
public class Vote extends AbstractBaseEntity{

    public static final String BY_MENU = "Voice.getForMenu";
    public static final String BY_USER = "Voice.getForUser";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @MapsId("menu_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonBackReference
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonBackReference
    private User user;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime voteTime;

    public Vote() {
    }

    public Vote(Menu menu, User user) {
        this.menu = menu;
        this.user = user;
        voteTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public Vote(Menu menu, User user, LocalDateTime localDateTime) {
        this.menu = menu;
        this.user = user;
        voteTime = localDateTime;
    }

    public Vote(LocalDateTime localDateTime) {
        voteTime = localDateTime;
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

    @Override
    public String toString() {
        String subElementStart = "\n\t\t";
        String listStart = "\n\t";
        return "Vote{" +
                "vote time = " + getVoteTime() +
                '}';
    }
}
