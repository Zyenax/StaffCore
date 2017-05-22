package net.warvale.staffcore.commands.staff;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.exceptions.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IPCommand extends AbstractCommand {

    public IPCommand() {
        super("ip", "<player>");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {

        if (!(sender instanceof Player)) {
            throw new CommandException("Only players can execute this command.");
        }

        if (args.length == 0) {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (!(player == null)) {
            sender.sendMessage(ChatColor.GREEN + player.getName() + "'s IP: " + ChatColor.WHITE + player.getAddress().getAddress().getHostAddress());
        } else {
            throw new CommandException(ChatColor.RED + "Invalid player!");
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return allPlayers();
    }

}
