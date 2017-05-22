package net.warvale.staffcore.rank.commands;

import net.warvale.staffcore.rank.RankManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by Draem on 5/11/2017.
 */
public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("warvale.management")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7&m---&r &cRank Management &7&m---&r" +
                                "\n&8| &7/rank pull"));
                return true;
            } else {
                if (args[0].equals("pull")) {
                    try {
                        RankManager.prep();
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.WHITE + "Unknown command. Type \"/help\" for help.");
            return true;
        }

        return true;
    }

}
