package net.warvale.staffcore.commands.basic;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.exceptions.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPCommand extends AbstractCommand {

    public TPCommand() {
        super("tp", "<player>");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {

        if (!(sender instanceof Player)) {
            throw new CommandException("Only players can execute this command.");
        }

        if (args.length == 0) {
            return false;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getServer().getPlayer(args[0]);

        if(target == null) {
            throw new CommandException("That player is currently not online!");
        }

        player.teleport(target.getLocation());
        player.sendMessage(ChatColor.GREEN + "Successfully teleported to: " + target.getName());


        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return allPlayers();
    }

}
