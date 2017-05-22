package net.warvale.staffcore.commands.admin.rank;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RankDelete extends SubCommand {

    public RankDelete(AbstractCommand command) {
        super("delete", command, "Removes a group", "<Name>");
    }

    // Deletes an existing permission group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, RankNotFoundException {

        if (args.size() == 1) {

            // Ensures the group exists
            Rank rank = RankManager.getRank(args.get(0));
            if (rank != null) {

                // Don't allow removing the default group
                if (rank.isDefault()) {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cDefault group can't be deleted.");
                    return;
                }

                rank.delete();

                // Removes the rank form the RankManager
                RankManager.getRanks().remove(rank);

                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aDeleted the §n" + rank.getName() + "§a rank.");

                // Removes the group from the DataStore
                RankManager.deleteRank(rank);

            } else {
                throw new RankNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }

}
