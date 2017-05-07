package me.warvale.staffcore.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Aerh for StaffCore.
 */
public class StaffChat implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("warvale.staff")))
            sender.sendMessage(ChatColor.RED + "You don't have permission to access this!");
        else
            if(args.length == 0) sender.sendMessage(ChatColor.RED + "Please include a message!");
            else
                for(Player players : Bukkit.getOnlinePlayers()) {
                if(players.hasPermission("warvale.staff"))
                    players.sendMessage(ChatColor.DARK_RED + "[STAFF] " + ChatColor.RESET + sender.getName() + ": " +
                            StringUtils.join(args, ' ', 0, args.length));
                }
        return false;
    }
}
