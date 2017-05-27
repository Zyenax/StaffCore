package net.warvale.staffcore.punish.data;

/**
 * Created by Draem on 5/21/2017.
 */
public enum Severity {

    SECOND,
    SEVERITY_1,
    PERMANENT;

    @Override
    public String toString() {
        return this.name().toUpperCase().substring(0, 1) + this.name().toLowerCase().substring(1, this.name().length());
    }

}
