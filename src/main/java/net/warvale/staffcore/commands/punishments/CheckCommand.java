package net.warvale.staffcore.commands.punishments;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.utils.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Callum for StaffCore.
 */
public class CheckCommand implements CommandExecutor {
	private StaffCore plugin;
	
	public CheckCommand(StaffCore plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(! (sender.hasPermission("warvale.staff"))) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to access this!");
		}
		
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Provide a player's name to check!");
		} else {
			Player target = Bukkit.getPlayerExact(args[0]);
			PlayerManager playerManager = new PlayerManager(plugin);
			playerManager.load(target);
			
			sender.sendMessage("Banned? " + playerManager.get().getBoolean("punish.ban.banned"));
			
			if(playerManager.get().getBoolean("punish.ban.banned")) {
				sender.sendMessage("  Reason: " + playerManager.get().getString("punish.ban.reason"));
				sender.sendMessage("  Expires: " + playerManager.get().getString("punish.ban.expires"));
				sender.sendMessage("  Banned By: " + playerManager.get().getString("punish.ban.staff"));
			}
		}
		return false;
	}
}
