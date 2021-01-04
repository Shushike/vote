package ru.topjava.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.topjava.AuthorizedUser;
import ru.topjava.model.AbstractBaseEntity;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    private static int id = AbstractBaseEntity.START_SEQ;
    private static boolean isAdmin = true;

    private SecurityUtil() {
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        return requireNonNull(safeGet(), "No authorized user found");
    }

    public static int authUserId() {
        return get().getUserTo().id();
    }

    /*public static boolean authUserIsAdmin() {
        return isAdmin;
    }
    public static void setAuthUserId(int id, boolean isAdmin) {
        SecurityUtil.id = id;
        SecurityUtil.isAdmin = isAdmin;
    }*/

}