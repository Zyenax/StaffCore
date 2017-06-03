package net.warvale.staffcore.commands.staff;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.exceptions.CommandException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import net.warvale.staffcore.users.User;
import net.warvale.staffcore.users.UserManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StaffChatCommand extends AbstractCommand {

    public StaffChatCommand() {
        super("sc", "<message>");
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
        User user = UserManager.getUser(player);

        MessageManager.broadcast(PrefixType.STAFF, ChatColor.translateAlternateColorCodes('&', user.getPrefix() + "&f" + player.getName() + "&7: &f" +
                StringUtils.join(args, ' ', 0, args.length)), "warvale.staffchat");

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

}
