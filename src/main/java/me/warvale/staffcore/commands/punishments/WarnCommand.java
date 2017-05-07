package me.warvale.staffcore.commands.punishments;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Draem on 5/7/2017.
 */
public class WarnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(!sender.hasPermission("warvale.punish")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to access this command.");
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Not enough arguments!");
            return true;
        } else {
            String reason = args.length > 1 ? StringUtils.join(args, ' ', 1, args.length) : "N/A";

            Player player = Bukkit.getPlayer(args[0]);
            if (player != null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have been &ewarned &cby &e" + sender.getName() + "&c for: &f" + reason + "!"));
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid player!");
                return true;
            }

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    "&4[PUNISH] &b" + sender.getName() + " &7warned &b" + args[0] + " &7for &c" + reason));
            Bukkit.broadcastMessage(" ");
        }
        return true;
    }

}
