package net.warvale.staffcore.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class AlertCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("warvale.staff"))
			sender.sendMessage(ChatColor.RED + "You don't have permission to access this!");
		else
			if(args.length == 0)
				sender.sendMessage(ChatColor.RED + "Please include a message!");
			else
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "\n[WARVALE ALERT] " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 0, args.length) + "\n"));
		return false;
	}
}
