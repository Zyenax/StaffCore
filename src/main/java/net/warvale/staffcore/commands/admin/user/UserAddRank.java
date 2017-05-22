package net.warvale.staffcore.commands.admin.user;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.InsufficientArgumentTypeException;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import net.warvale.staffcore.users.User;
import net.warvale.staffcore.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UserAddRank extends SubCommand {

    public UserAddRank(AbstractCommand command) {
        super("addrank", command, "Add user to group", "<User> <Rank>");
    }

    // Adds a user to a permission group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, RankNotFoundException {

        if (args.size() == 2) {

            Player player = Bukkit.getPlayer(args.get(0));

            User user = UserManager.getUser(player);
            Rank rank = RankManager.getRank(args.get(1));

            // Ensures the user exists
            if (user != null) {

                // Ensures the group exists
                if (rank != null) {

                    // If its the default group, informs the user that everyone is always in the default group
                    if (rank.isDefault()) {
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cUsers are always in the default group.");
                        return;
                    }

                    // Ensures that the user is not already part of the group
                    if (!user.getRanks().contains(rank)) {
                        user.getRanks().add(rank);
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aAdded §n" + user.getName() + "§a to" + (rank.isConditional() ? " the conditional rank" : " the rank") + " §n" + rank.getName() + "§a!");

                        // Saves the user
                        final User finalUser = user;
                        StaffCore.doAsync(() -> UserManager.saveUser(user));

                    } else {
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§c§n" + user.getName() + "§c is already part of §n" + rank.getName() + "§c.");
                    }

                } else {
                    throw new RankNotFoundException(args.get(1));
                }

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
