package me.warvale.staffcore.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by shallam on 10/05/2017.
 */
public class AlertCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.hasPermission("warvale.admin"))) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to access this!");
        } else {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Please supply a message to announce!");
            } else {
                Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "\n[Warvale Alert] "
                        + ChatColor.WHITE + StringUtils.join(args, ' ', 0, args.length) + "\n");
            }
        }
        return false;
    }
}
