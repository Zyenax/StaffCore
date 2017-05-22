package net.warvale.staffcore.commands.admin.user;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.InsufficientArgumentTypeException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.users.*;
import net.warvale.staffcore.permissions.Privilege;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserUnset extends SubCommand {

    public UserUnset(AbstractCommand command) {
        super("unset", command, "Unset user permission", "<User> <Permission>");
    }

    // Removes a defined permission for a user
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException {

        if (args.size() == 2) {

            User user = UserManager.getUser(args.get(0));
            String permission = args.get(1).toLowerCase();

            // Ensures the user exists
            if (user != null) {

                // Ensures the permission is defined
                Privilege privilege = user.getPrivilege(permission);
                if (privilege != null) {

                    // Removes the definition
                    user.getPrivileges().remove(privilege);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aUnset §n" + permission + "§a for §n" + user.getName() + "§a.");

                    // Saves the user
                    final User finalUser = user;
                    StaffCore.doAsync(() -> UserManager.saveUser(user));

                } else {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§c§n" + permission + "§c is not currently defined for §n" + user.getName() + "§c.");
                }
            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
