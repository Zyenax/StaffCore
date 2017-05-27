package net.warvale.staffcore.commands.staff;

import net.warvale.staffcore.punish.data.PunishmentType;
import net.warvale.staffcore.punish.data.Severity;
import net.warvale.staffcore.punish.inventories.PunishmentMenu;

/**
 * Created by Draem on 5/27/2017.
 */
public class PunishCommand {

    static {
        new PunishmentMenu(PunishmentType.BAN, Severity.SEVERITY_1);
        new PunishmentMenu(PunishmentType.BAN, Severity.PERMANENT);
        new PunishmentMenu(PunishmentType.MUTE, Severity.SEVERITY_1);
        new PunishmentMenu(PunishmentType.MUTE, Severity.PERMANENT);
        new PunishmentMenu(PunishmentType.WARN, Severity.SECOND);
    }

}
