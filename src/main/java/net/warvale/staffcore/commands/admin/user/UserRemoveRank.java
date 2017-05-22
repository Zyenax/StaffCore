package net.warvale.staffcore.commands.admin.user;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.InsufficientArgumentTypeException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import net.warvale.staffcore.users.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserRemoveRank extends SubCommand {

    public UserRemoveRank(AbstractCommand command) {
        super("removerank", command, "Remove user from group", "<User> <Rank>");
    }

    // Removes a user from a permission group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, RankNotFoundException {

        if (args.size() == 2) {

            User user = UserManager.getUser(args.get(0));
            Rank rank = RankManager.getRank(args.get(1));

            // Ensures the user exists
            if (user != null) {

                // Ensures the group exists
                if (rank != null) {

                    // Ensures the user is in the group
                    if (user.getRanks().contains(rank)) {

                        // Prevents removing the default group
                        if (rank.isDefault()) {
                            sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cYou can't remove players from the default group.");
                            return;
                        }

                        user.getRanks().remove(rank);
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aRemoved §n" + user.getName() + "§a from §n" + rank.getName() + "§a!");

                        // Saves the user
                        final User finalUser = user;
                        StaffCore.doAsync(() -> UserManager.saveUser(user));

                    } else {
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§c§n" + user.getName() + "§c isn't part of §n" + rank.getName() + "§c.");
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
