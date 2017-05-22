package net.warvale.staffcore.exceptions;


// Thrown when the provided name or UUID doesn't match anybody in the DataStore
public class UserNotFoundException extends Exception {

    private String user;

    public UserNotFoundException(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
