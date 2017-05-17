package me.warvale.staffcore.commands.punishments;

import me.warvale.staffcore.StaffCore;
import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BanCommand implements CommandExecutor, Listener {
	private StaffCore plugin;
	
	public BanCommand(StaffCore plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(! sender.hasPermission("warvale.punish")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to access this command.");
			return true;
		}
		
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments!");
			return true;
		} else {
			String reason = args.length > 1 ? StringUtils.join(args, ' ', 1, args.length) : "N/A";
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = null;
			try {
				date = formatter.parse("31-December-9999");
			} catch(ParseException e) {
				e.printStackTrace();
			}
			Player player = Bukkit.getPlayer(args[0]);
			long mills = date.getTime();
			/*PlayerManager playerManager = new PlayerManager(plugin);
			
			playerManager.load(player);
			playerManager.get().set("punishment.ban.reason", reason);
			playerManager.get().set("punishment.ban.banned", true);
			playerManager.get().set("punishment.ban.expires", new Date(mills));
			playerManager.get().set("punishment.ban.staff", sender.getName());
			playerManager.save();*/
			
			
			Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(),
					ChatColor.translateAlternateColorCodes('&', "&c&lYou have been permanently banned by " + player.getName() + "!\n&f" + reason),
					null,
					sender.getName());
			
			if(player != null) {
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
						"&cYou have been permanently banned from this server!\n\n&7Reason: &f" + reason));
			}
			
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
					"&4[PUNISH] &b" + sender.getName() + " &7banned &b" + args[0] + " &7for &c" + reason));
			Bukkit.broadcastMessage(" ");
		}
		return true;
	}
}
