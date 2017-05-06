package staff.core.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SC implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sc")) {
            sender.sendMessage(ChatColor.GRAY + "StaffCore working properly on " + ChatColor.GREEN + "version 1.0.1");
            return true;
        }
        return false;
    }
   
}
