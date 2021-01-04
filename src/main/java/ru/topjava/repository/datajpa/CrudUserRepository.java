package ru.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.model.User;
import ru.topjava.util.ValidationUtil;
import ru.topjava.util.exception.NotFoundException;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    User getByEmail(String email);

    @EntityGraph(attributePaths = {"roles"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT u FROM User u WHERE u.id=?1")
    User getWithRoles(int id);

    @EntityGraph(attributePaths = {"roles", "vote", "vote.menu"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT u FROM User u WHERE u.id=?1")
    User getWithVotes(int id);

    default User findEntityById(int userId) throws NotFoundException {
        return ValidationUtil.checkNotFoundWithId(this.findById(userId).orElse(null), "user", userId);
    }
}

