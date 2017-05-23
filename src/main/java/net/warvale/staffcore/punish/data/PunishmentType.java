package net.warvale.staffcore.punish.data;

public enum PunishmentType {
    BAN("Banned"), MUTE("Muted"), WARN("Warned");

    public String display;

    PunishmentType(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
