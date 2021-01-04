package ru.topjava.util;

import ru.topjava.HasId;
import ru.topjava.util.exception.IllegalRequestDataException;
import ru.topjava.util.exception.InsufficientRightsException;
import ru.topjava.util.exception.NotFoundException;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static boolean checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "ID=" + id);
        return found;
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

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void checkRights(boolean hasRights) {
        if (!hasRights)
            throw new InsufficientRightsException();
    }

    public static void assureIdConsistent(HasId bean, int id) {
        // conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalArgumentException(bean + " must be with ID=" + id);
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