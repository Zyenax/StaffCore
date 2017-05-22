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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class UserWorld extends SubCommand {

    public UserWorld(AbstractCommand command) {
        super("world", command, "Sets permission access per-world", "<User> <Permission> <World>");
    }

    // Restricts certain permissions to some worlds
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException {

        if (args.size() == 3) {

            User user = UserManager.getUser(args.get(0));
            String permission = args.get(1).toLowerCase();
            World world = Bukkit.getWorld(args.get(2));

            // Ensures the user exists
            if (user != null) {

                // Ensures the world exists
                if (world != null) {
                    Privilege privilege = user.getPrivilege(permission);

                    // Ensures the permission is defined
                    if (privilege != null) {

                        // Toggles the restricted state of the world
                        if (privilege.getWorlds().contains(world)) {
                            privilege.getWorlds().remove(world);
                        } else {
                            privilege.getWorlds().add(world);
                        }

                        // Informs the user of their new world preferences
                        if (privilege.getWorlds().size() > 0) {
                            List<String> worlds = privilege.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                            sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§a§n" + permission + "§a granted for §n" + user.getName() + "§a in world(s): " + worlds.toString().replace("[", "").replace("]", ""));
                        } else {
                            sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§a§n" + permission + "§a granted for §n" + user.getName() + "§a in all worlds.");
                        }

                        // Saves the user
                        final User finalUser = user;
                        StaffCore.doAsync(() -> UserManager.saveUser(user));

                    } else {
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cUndefined permission. Set using \"/perms user set\", first.");
                    }
                } else {
                    List<String> worlds = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                    throw new InsufficientArgumentTypeException("WORLD", args.get(2), worlds.toString().replace("[", "").replace("]", ""));
                }
            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(3);
        }

    }
}
