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

public class RankSet extends SubCommand {

    public RankSet(AbstractCommand command) {
        super("set", command, "Set group permission", "<Rank> <Permission> [Negated]");
    }

    // Defines a permission for a group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, RankNotFoundException, InsufficientArgumentTypeException {

        if (args.size() >= 2) {

            Rank rank = RankManager.getRank(args.get(0));
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

            // Ensures the group exists
            if (rank != null) {

                // Ensures the permission isn't already defined for this scope
                Privilege privilege = rank.getPrivilege(permission);
                if (privilege != null) {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§c§n" + permission + "§c is already set for §n" + rank.getName() + "§c.");
                    return;
                }

                // Creates a new privilege
                privilege = new Privilege(permission, negated);
                rank.getPrivileges().add(privilege);

                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aSet §n" + permission + "§a for §n" + rank.getName() + "§a to §n" + (!privilege.isNegated()) + "§a.");

                // Saves the rank
                final Rank finalRank = rank;
                StaffCore.doAsync(() -> RankManager.saveRank(finalRank));

            } else {
                throw new RankNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
