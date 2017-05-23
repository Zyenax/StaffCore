package net.warvale.staffcore.punish.data;

/**
 * Created by Draem on 5/21/2017.
 */
public enum Severity {

    SEVERITY_1,
    SEVERITY_2,
    PERMANENT;

    @Override
    public String toString() {
        return this.name();
    }

}
