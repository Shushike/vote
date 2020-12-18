package ru.topjava.util;

import ru.topjava.model.AbstractBaseEntity;
import ru.topjava.util.exception.InsufficientRightsException;
import ru.topjava.util.exception.NotFoundException;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "ID=" + id);
    }

    public static <T> T checkNotFoundWithId(T object, String msg,  int id) {
        checkNotFoundWithId(object != null, msg, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, String msg, int id) {
        checkNotFound(found, "Failed to find "+msg+" with ID=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException(msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (ID=null)");
        }
    }

    public static void checkRights(boolean hasRights) {
        if (!hasRights)
            throw new InsufficientRightsException();
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with ID=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}