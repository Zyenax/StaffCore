package net.warvale.staffcore.commands.staff;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.exceptions.CommandException;
import net.warvale.staffcore.punish.PunishmentManager;
import static net.warvale.staffcore.punish.data.PunishmentType.*;
import static net.warvale.staffcore.punish.data.Severity.*;
import net.warvale.staffcore.punish.inventories.PunishmentMenu;
import net.warvale.staffcore.users.User;
import net.warvale.staffcore.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Draem on 5/27/2017.
 */
public class PunishCommand extends AbstractCommand {

    public PunishCommand() {
        super("pun", "<player>");
        new PunishmentMenu(BAN, SEVERITY_1);
        new PunishmentMenu(BAN, PERMANENT);
        new PunishmentMenu(MUTE, SEVERITY_1);
        new PunishmentMenu(MUTE, PERMANENT);
        new PunishmentMenu(WARN, SECOND);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {
        Player player = (Player) sender;

        User user = UserManager.getUser(args[1]);
        System.out.println(player.getName() + " is attempting to punish " + args[1] + " ::: Does the user exist? " + String.valueOf(user != null));
        if (user != null) {
            PunishmentManager.punishing.put(player.getUniqueId(), user.getUuid());
            switch (args[0]) {
                case "mute":
                    player.openInventory(PunishmentMenu.menus.get(2).getBuilder().build());
                    break;
                case "ban":
                    player.openInventory(PunishmentMenu.menus.get(1).getBuilder().build());
                    break;
                case "warn":
                    player.openInventory(PunishmentMenu.menus.get(4).getBuilder().build());
                    break;
                default:
                    break;
            }
        } else {
            player.sendMessage(ChatColor.RED + "Unknown user.");
            return true;
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
