package net.warvale.staffcore.commands.staff;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.CommandException;
import net.warvale.staffcore.punish.data.PunishmentType;
import net.warvale.staffcore.punish.data.Severity;
import net.warvale.staffcore.punish.inventories.PunishmentMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Draem on 5/27/2017.
 */
public class PunishCommand extends AbstractCommand {

    static {
        new PunishmentMenu(PunishmentType.BAN, Severity.SEVERITY_1);
        new PunishmentMenu(PunishmentType.BAN, Severity.PERMANENT);
        new PunishmentMenu(PunishmentType.MUTE, Severity.SEVERITY_1);
        new PunishmentMenu(PunishmentType.MUTE, Severity.PERMANENT);
        new PunishmentMenu(PunishmentType.WARN, Severity.SECOND);
    }

    public PunishCommand() {
        super("pun", "<player>");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {
        ((Player) sender).openInventory(PunishmentMenu.menus.get(1).getInventory());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
