package net.warvale.staffcore.commands.admin.user;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.users.*;
import net.warvale.staffcore.permissions.Privilege;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserView extends SubCommand {

    public UserView(AbstractCommand command) {
        super("view", command, "Shows user information", "<User>");
    }

    // Outputs information about a user
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() == 1) {

            User user = UserManager.getUser(args.get(0));

            // Ensures the user exists
            if (user != null) {

                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§e§n" + user.getName() + "§e information:");
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eUUID: " + user.getUuid().toString());
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eSuper User: " + user.isSuperUser());
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eMember of " + user.getRanks().size() + " ranks(s):");
                for (Rank rank : user.getRanks()) {
                    sender.sendMessage("§b§l-> §b" + rank.getName() + (rank.isConditional() ? " §6§o(Conditional)" : "") + (rank.isDefault() ? " §7§o(Default)" : ""));
                }
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§e" + user.getPrivileges().size() + " user-specific privilege(s):");
                for (Privilege privilege : user.getPrivileges()) {
                    sender.sendMessage("§b§l-> §b" + privilege.getNode() + (privilege.isNegated() ? " §c§o(Negated)" : "") + (privilege.isConditional() ? " §6§o(Conditional)" : ""));
                }
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eMeta:");
                sender.sendMessage("§b§l-> §bPrefix: " + (user.getMetaPrefix() != null ? user.getMetaPrefix() : "§c§oUndefined"));
                sender.sendMessage("§b§l-> §bSuffix: " + (user.getMetaSuffix() != null ? user.getMetaSuffix() : "§c§oUndefined"));


            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
