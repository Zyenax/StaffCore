package net.warvale.staffcore.commands.admin;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.commands.admin.user.*;
import net.warvale.staffcore.exceptions.CommandException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserCommand extends AbstractCommand {

    public UserCommand() {
        super("user", "");

        getCommands().add(new UserAddRank(this));
        getCommands().add(new UserBukkitCheck(this));
        getCommands().add(new UserPrefix(this));
        getCommands().add(new UserRemoveRank(this));
        getCommands().add(new UserSet(this));
        getCommands().add(new UserSetRank(this));
        getCommands().add(new UserSuffix(this));
        getCommands().add(new UserSuper(this));
        getCommands().add(new UserUnset(this));
        getCommands().add(new UserView(this));
        getCommands().add(new UserWorld(this));

    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {

        if (args.length == 0) {
            if (getCommands().size() > 0) {
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§a " + getName() + " Commands");
                for (SubCommand sc : getCommands()) {
                    sender.sendMessage("§b§l-> §b" + sc.getUsage());
                }
            } else {
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§c No register commands for provided scope.");
            }
        } else {

            // Match the second argument to a subcommand
            SubCommand subCmd = null;
            for (SubCommand sc : getCommands()) {
                if (sc.getName().equalsIgnoreCase(args[0])) {
                    subCmd = sc;
                    break;
                }
            }

            // Check if the command exists
            if (subCmd != null) {

                // Pass on any remaining arguments and execute the subcommand
                List<String> a = new ArrayList<>();
                a.addAll(Arrays.asList(args).subList(1, args.length));

                subCmd.run(sender, a);

            } else {
                sender.sendMessage("§c§l[WarvalePerms] §cUnrecognized command. Try \"/ " + getName() + "\"?");
            }

        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

}
