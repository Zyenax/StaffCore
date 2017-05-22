package net.warvale.staffcore.exceptions;

// Thrown when not enough arguments are passed to a command
public class InsufficientArgumentException extends Exception {

    private int required;

    public InsufficientArgumentException(int required) {
        this.required = required;
    }

    public int getRequired() {
        return required;
    }
}
