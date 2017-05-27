package net.warvale.staffcore.commands.admin.user;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.users.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserPrefix extends SubCommand {

    public UserPrefix(AbstractCommand command) {
        super("prefix", command, "Changes user prefix", "<Rank> <Prefix|Remove>");
    }

    // Sets a user's prefix to be used by other plugins (like chat plugins). User prefixes will override group prefixes.
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() >= 2) {

            User user = UserManager.getUser(args.get(0));
            // Ensure the user exists
            if (user != null) {

                if (!args.get(1).equalsIgnoreCase("remove")) {
                    // Builds the prefix
                    String prefix = "";
                    for (int i = 1; i < args.size(); i++) {
                        prefix = prefix + args.get(i) + " ";
                    }
                    user.setMetaPrefix(prefix);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + " §aPrefix for §n" + user.getName() + "§a set: §f" + user.getMetaPrefix());
                } else {
                    user.setMetaPrefix(null);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + " §aPrefix for §n" + user.getName() + "§a removed.");
                }

                // Saves the suer
                final User finalUser = user;
                StaffCore.doAsync(() -> UserManager.saveUser(user));

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
