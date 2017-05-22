package net.warvale.staffcore.commands.basic;


import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.exceptions.CommandException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AlertCommand extends AbstractCommand {

    public AlertCommand() {
        super("alert", "<message>");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {

        if (!(sender instanceof Player)) {
            throw new CommandException("Only players can execute this command.");
        }

        if (args.length == 0) {
            return false;
        }

        MessageManager.broadcast(PrefixType.ALERT, ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 0, args.length) + "\n"));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

}
