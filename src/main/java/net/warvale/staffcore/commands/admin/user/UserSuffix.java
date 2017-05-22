package net.warvale.staffcore.commands.admin.user;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.users.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserSuffix extends SubCommand {

    public UserSuffix(AbstractCommand command) {
        super("suffix", command, "Changes user suffix", "<Rank> <Suffix|Remove>");
    }

    // Sets the user suffix to be used by other plugins (Such as chat plugins). User suffixes override group suffixes.
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() >= 2) {

            User user = UserManager.getUser(args.get(0));
            if (user != null) {

                if (!args.get(1).equalsIgnoreCase("remove")) {
                    String prefix = "";
                    for (int i = 1; i < args.size(); i++) {
                        prefix = prefix + " " + args.get(i);
                    }
                    prefix = prefix.substring(1);
                    user.setMetaSuffix(prefix);
                    sender.sendMessage("§a§l[MicroPerms] §aSuffix for §n" + user.getName() + "§a set: §f" + user.getMetaSuffix());
                } else {
                    user.setMetaSuffix(null);
                    sender.sendMessage("§a§l[MicroPerms] §aSuffix for §n" + user.getName() + "§a removed.");
                }

                //saves the users data
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
