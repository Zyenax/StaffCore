package net.warvale.staffcore.punish.data;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Draem on 5/21/2017.
 */
public class Punishment {

    private UUID uuid;
    private PunishmentType type;
    private Date date;
    private Date expires;
    private UUID staff;
    private Reason reason;
    private boolean active;

    public Punishment(UUID uuid, Date date, Date expires, UUID staff, Reason reason) {
        this.uuid = uuid;
        this.type = reason.getType();
        this.date = date;
        this.expires = expires;
        this.staff = staff;
        this.reason = reason;
        this.active = false;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public PunishmentType getType() {
        return this.type;
    }

    public Date getDate() {
        return this.date;
    }

    public Date getExpires() {
        return this.expires;
    }

    public UUID getStaff() {
        return this.staff;
    }

    public Reason getReason() {
        return this.reason;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void apply() {

    }
}

