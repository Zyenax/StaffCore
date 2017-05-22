package net.warvale.staffcore.commands;

import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
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
		if(! (sender.hasPermission("warvale.staff"))) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to access this!"); return true; }
		else if(args.length == 0) { sender.sendMessage(ChatColor.RED + "Please include a message!"); return true; }
		else {
			Player player = (Player) sender;
			Rank rank = RankManager.getRankForUser(player.getName());
			
			for(Player players : Bukkit.getOnlinePlayers()) {
				if(players.hasPermission("warvale.staff")) {
					assert rank != null;
					players.sendMessage(ChatColor.DARK_RED + "[STAFF] " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', rank.getPrefix() + rank.getNamecolor() + player.getName() + "&7: &f" +
							                    StringUtils.join(args, ' ', 0, args.length)));
				}
			}
			return true;
		}
	}
}
