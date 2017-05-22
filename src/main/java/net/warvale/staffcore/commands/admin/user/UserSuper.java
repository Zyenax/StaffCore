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
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

public class UserSuper extends SubCommand {

    public UserSuper(AbstractCommand command) {
        super("super", command, "Toggles super user", "<User>");
    }

    // Make a user a super user
    // Super users can control all aspects of MelonPerms, regardless of OP or permission status
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() == 1) {

            User user = UserManager.getUser(args.get(0));

            // Ensures the user exists
            if (user != null) {

                // Ensures that console is the one sending this command
                if ((sender instanceof ConsoleCommandSender)) {

                    // Toggles super user status
                    user.setSuperUser(!user.isSuperUser());
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aSet super user status of §n" + user.getName() + "§a to §n" + user.isSuperUser() + "§a.");

                    // Saves the user
                    final User finalUser = user;
                    StaffCore.doAsync(() -> UserManager.saveUser(user));

                } else {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cSuper user modifications may only be done as console.");
                }

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
