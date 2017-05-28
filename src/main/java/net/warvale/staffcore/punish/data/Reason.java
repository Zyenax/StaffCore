package net.warvale.staffcore.punish.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Draem on 5/22/2017.
 */
public class Reason {

    public static List<Reason> reasonList = new ArrayList<>();

    private String reason;
    private String description;
    private Severity sev;
    private PunishmentType type;

    public Reason(String reason, String description, Severity sev, PunishmentType type) {
        this.reason = reason;
        this.description = description;
        this.sev = sev;
        this.type = type;
        reasonList.add(this);
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

    public static List<Reason> getReasonsByParams(PunishmentType type, Severity severity) {
        List<Reason> reasons = new ArrayList<>();
        Reason.reasonList.stream().filter(reason1 -> reason1.getType().equals(type)).filter(reason1 -> reason1.getSev().equals(severity)).forEach(reasons::add);
        return reasons;
    }

    public static void populate() {
        new Reason("General Disrespect", "Disrespect of other players in chat/messages", Severity.SECOND, PunishmentType.WARN);
        new Reason("Discord Advertisement", "Discord Advertisement", Severity.SEVERITY_1, PunishmentType.MUTE);
        new Reason("Over Capitalization", "Excessive use of caps in chat", Severity.SEVERITY_1, PunishmentType.MUTE);
        new Reason("Abusing Bugs", "Purposefully abusing known bugs", Severity.SEVERITY_1, PunishmentType.BAN);
        new Reason("Client Modification", "Advantageous client modification", Severity.PERMANENT, PunishmentType.BAN);
        new Reason("DDoS Threats", "Threatening to DDoS server or players", Severity.PERMANENT, PunishmentType.BAN);
    }
}
