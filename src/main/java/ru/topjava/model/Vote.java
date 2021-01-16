package ru.topjava.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NamedQueries({
        @NamedQuery(name = Vote.BY_MENU, query = "SELECT v FROM Vote v WHERE v.menu.id=:menuId"),
        @NamedQuery(name = Vote.BY_USER, query = "SELECT v FROM Vote v WHERE v.user.id=:userId")
})
@Access(AccessType.FIELD)
@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "user_id"}, name = "vote_unique_menu_user_idx")})
public class Vote extends AbstractBaseEntity {

    public static final String BY_MENU = "Voice.getForMenu";
    public static final String BY_USER = "Voice.getForUser";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Menu.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("menuId")
    //@JsonBackReference(value = "menu-vote")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    //@JsonBackReference(value = "user-vote")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = User.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("userId")
    private User user;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime voteTime;

    public Vote() {
        voteTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public Vote(Menu menu, User user) {
        this();
        this.menu = menu;
        this.user = user;
    }

    public Vote(Menu menu, User user, LocalDateTime localDateTime) {
        this.menu = menu;
        this.user = user;
        voteTime = localDateTime;
    }

    public Vote(Integer id, Menu menu, User user, LocalDateTime localDateTime) {
        super(id);
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
        return "Vote{" +
                "id = " + id +
                ", vote time = " + getVoteTime() +
                '}';
    }
}
