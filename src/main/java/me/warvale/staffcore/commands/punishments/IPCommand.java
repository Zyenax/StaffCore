package me.warvale.staffcore.commands.punishments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Draem on 5/7/2017.
 */
public class IPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("warvale.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to access this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(args.length > 2 ? ChatColor.RED + "Not enough arguments." : ChatColor.RED + "Too many arguments.");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (!(player == null)) {
            sender.sendMessage(ChatColor.GREEN + player.getName() + "'s IP: " + ChatColor.WHITE + player.getAddress().getAddress().getHostAddress() + " : " + player.getAddress().getPort());
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid player!");
        }
        return true;
    }

}
