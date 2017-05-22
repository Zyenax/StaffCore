package net.warvale.staffcore.commands.admin.user;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UserBukkitCheck extends SubCommand {

    public UserBukkitCheck(AbstractCommand command) {
        super("bukkitcheck", command, "Check if a user has a permission (according to bukkit)", "<Player> <Permission>");
    }

    // Performs a bukkit hasPermission() check, for permission testing
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() == 2) {

            Player player = Bukkit.getPlayer(args.get(0));

            if (player != null && player.isOnline()) {
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aTest Result: §n" + player.hasPermission(args.get(1)));
            } else {
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cAn online bukkit player must be provided.");
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
