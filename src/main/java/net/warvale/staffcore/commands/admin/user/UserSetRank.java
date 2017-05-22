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

import java.lang.reflect.Member;
import java.util.List;

public class UserSetRank extends SubCommand {

    public UserSetRank(AbstractCommand command) {
        super("setrank", command, "Set user to group", "<User> <Rank>");
    }

    // Sets a users group
    // Setting a group removes all other memberships (except the default group)
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, RankNotFoundException {

        if (args.size() == 2) {

            User user = UserManager.getUser(args.get(0));
            Rank rank = RankManager.getRank(args.get(1));

            // Ensures the user exists
            if (user != null) {

                // Ensures the group exists
                if (rank != null) {

                    // Informs the user that users are ALWAYS in the default group
                    if (rank.isDefault()) {
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cUsers are always in the default group.");
                        return;
                    }

                    // Clears the user's groups
                    user.getRanks().clear();

                    // Adds the speciifc group
                    user.getRanks().add(rank);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aSet §n" + user.getName() + "§a to §n" + rank.getName() + "§a.");

                    // Saves the user
                    final User finalUser = user;
                    StaffCore.doAsync(() -> UserManager.saveUser(user));

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
