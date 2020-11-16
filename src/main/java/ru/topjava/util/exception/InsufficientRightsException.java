package ru.topjava.util.exception;

public class InsufficientRightsException extends RuntimeException {
    public InsufficientRightsException() {
        super("The current user has insufficient rights");
    }
}
