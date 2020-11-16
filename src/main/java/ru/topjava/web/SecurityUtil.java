package ru.topjava.web;

import ru.topjava.model.AbstractBaseEntity;

public class SecurityUtil {

    private static int id = AbstractBaseEntity.START_SEQ;
    private static boolean isAdmin = false;

    private SecurityUtil() {
    }

    public static int authUserId() {
        return id;
    }

    public static boolean authUserIsAdmin() {
        return isAdmin;
    }

    public static void setAuthUserId(int id, boolean isAdmin) {
        SecurityUtil.id = id;
        SecurityUtil.isAdmin = isAdmin;
    }

}