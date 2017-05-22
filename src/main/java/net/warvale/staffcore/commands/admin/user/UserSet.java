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

public class UserSet extends SubCommand {

    public UserSet(AbstractCommand command) {
        super("set", command, "Set user permission", "<User> <Permission> [Negated]");
    }

    // Defines a privilege for a user
    // User defined privileges override group-defined ones
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException {

        if (args.size() >= 2) {

            User user = UserManager.getUser(args.get(0));
            String permission = args.get(1).toLowerCase();
            boolean negated = false;

            if (args.size() == 3) {
                String b = args.get(2);
                if (b.equalsIgnoreCase("true") || b.equalsIgnoreCase("false")) {
                    negated = b.equalsIgnoreCase("true");
                } else {
                    throw new InsufficientArgumentTypeException("BOOLEAN", b, "true/false");
                }
            }

            // Ensures the user exists
            if (user != null) {

                // Ensures the permission is not already defined
                Privilege privilege = user.getPrivilege(permission);
                if (privilege != null) {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§c§n" + permission + "§c is already set for §n" + user.getName() + "§c.");
                    return;
                }

                // Defines the permission for the user
                privilege = new Privilege(permission, negated);
                user.getPrivileges().add(privilege);

                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aSet §n" + permission + "§a for §n" + user.getName() + "§a to §n" + (!privilege.isNegated()) + "§a.");

                // Saves the user
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
