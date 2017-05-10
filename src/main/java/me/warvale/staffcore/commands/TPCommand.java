package me.warvale.staffcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("warvale.punish")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to access this command.");
            return true;
        }

        Player p = (Player) sender;
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Not enough arguments!");
            return true;
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if(target.getName() == null) {
            p.sendMessage(ChatColor.RED + "That player is currently not online!");
        } else {
            p.teleport(target.getLocation());
            p.sendMessage(ChatColor.GREEN + "Successfully teleported to: " + target.getName());
        }

        return false;
    }
}
