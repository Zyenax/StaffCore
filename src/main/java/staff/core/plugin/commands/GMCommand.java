package staff.core.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GMCommand implements CommandExecutor {

    public static boolean ismuted = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§You must be a player to execute this command!");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("warvale.staff")) {
            p.sendMessage("§Sorry, but you don't have permission to execute this command!");
            return true;
        }

        if (!ismuted) {
            Bukkit.broadcastMessage("§aGlobal mute has been activated. This may be because we have found an issue.");
            ismuted = true;
        } else {
            Bukkit.broadcastMessage("§cGlobal mute has been deactivated.");
            ismuted = false;
        }

        return false;
    }
}
