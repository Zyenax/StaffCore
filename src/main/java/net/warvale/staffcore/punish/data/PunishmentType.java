package net.warvale.staffcore.punish.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum PunishmentType {
    BAN("Banned", "ban"), MUTE("Muted", "mute"), WARN("Warned", "warn");

    public String display;

    PunishmentType(String display, String name) {
        this.display = display;
    }

    public List<Severity> getSeverities() {
        switch (this) {
            case BAN:
                return Arrays.asList(Severity.PERMANENT, Severity.SEVERITY_1);
            case MUTE:
                return Arrays.asList(Severity.PERMANENT, Severity.SEVERITY_1);
            case WARN:
                return Collections.singletonList(Severity.SECOND);
            default:
                return null;
        }

    }

    @Override
    public String toString() {
        return this.name();
    }
}
