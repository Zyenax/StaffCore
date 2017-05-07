package me.warvale.staffcore.commands.punishments;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
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
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                        "&cYou have been kicked from this server!\n\n&7Reason: &f" + reason));
            }

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    "&4[PUNISH] &b" + sender.getName() + " &7kicked &b" + args[0] + " &7for &c" + reason));
            Bukkit.broadcastMessage(" ");
        }
        return true;
    }
}
