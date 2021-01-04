package ru.topjava.util.exception;

public class ModifyForbiddenException extends RuntimeException {
    public ModifyForbiddenException(String message) {
        super(message);
    }
}