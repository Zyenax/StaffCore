package net.warvale.staffcore.commands.admin.rank;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.InsufficientArgumentTypeException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.permissions.Privilege;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RankUnset extends SubCommand {

    public RankUnset(AbstractCommand command) {
        super("unset", command, "Unset group permission", "<Rank> <Permission>");
    }

    // Removes a defined permission from a group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, RankNotFoundException, InsufficientArgumentTypeException {

        if (args.size() == 2) {

            Rank rank = RankManager.getRank(args.get(0));
            String permission = args.get(1).toLowerCase();

            // Ensures the group exists
            if (rank != null) {

                // Ensures the permission is defined
                Privilege privilege = rank.getPrivilege(permission);
                if (privilege != null) {

                    // Removes the definition
                    rank.getPrivileges().remove(privilege);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aUnset §n" + permission + "§a for §n" + rank.getName() + "§a.");

                    // Saves the group to datastore
                    final Rank finalRank = rank;
                    StaffCore.doAsync(() -> RankManager.saveRank(finalRank));

                } else {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§c§n" + permission + "§c is not currently defined for §n" + rank.getName() + "§c.");
                }
            } else {
                throw new RankNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
