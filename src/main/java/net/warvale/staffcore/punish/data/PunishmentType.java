package net.warvale.staffcore.punish.data;

public enum PunishmentType {
    BAN("Banned", "ban"), MUTE("Muted", "mute"), WARN("Warned", "warn");

    public String display;

    PunishmentType(String display, String name) {
        this.display = display;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
