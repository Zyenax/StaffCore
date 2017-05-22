package net.warvale.staffcore.exceptions;

// Thrown when the supplied name doesn't match any groups
public class RankNotFoundException extends Exception {

    private String rank;

    public RankNotFoundException(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }
}
