package staff.core.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class GMCommand implements CommandExecutor, Listener {
    boolean muted;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("warvale.staff")) {
            p.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to execute this command!");
            return true;
        }
        muted ^= true;

        if (!muted)
            Bukkit.broadcastMessage(ChatColor.RED + "Global mute has been deactivated.");
        else
            Bukkit.broadcastMessage(ChatColor.GREEN + "Global mute has been activated. This may be because we have found an issue.");


        return false;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(muted && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Chat has been disabled by an administrator.");
        }
    }
}
