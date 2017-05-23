package net.warvale.staffcore.punish.data;

/**
 * Created by Draem on 5/22/2017.
 */
public class Reason {

    private String reason;
    private String description;
    private Severity sev;
    private PunishmentType type;

    public Reason(String reason, String description, Severity sev, PunishmentType type) {
        this.reason = reason;
        this.description = description;
        this.sev = sev;
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSev() {
        return sev;
    }

    public void setSev(Severity sev) {
        this.sev = sev;
    }

    public PunishmentType getType() {
        return type;
    }

    public void setType(PunishmentType type) {
        this.type = type;
    }

    public static Reason compile(String reason, Severity severity, PunishmentType type) {
        return new Reason(reason, "", severity, type);
    }

    @Override
    public String toString() {
        return this.reason;
    }
}
