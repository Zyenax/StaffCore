package staff.core.plugin.punishments;

import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import staff.core.plugin.StaffCore;

public class BanCommand implements CommandExecutor {
    StaffCore plugin;

    public BanCommand(StaffCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("warvale.punish")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to access this command.");
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Not enough args!");
            return true;
        } else {
            String reason = args.length > 1 ? StringUtils.join(args, ' ', 1, args.length) : "N/A";
            Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, null, sender.getName());

            Player player = Bukkit.getPlayer(args[0]);
            if (player != null) {
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