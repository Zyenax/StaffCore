package net.warvale.staffcore.commands.admin.rank;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RankPrefix extends SubCommand {

    public RankPrefix(AbstractCommand command) {
        super("prefix", command, "Changes rank prefix", "<Rank> <Prefix|Remove>");
    }

    // Sets a ranks's meta prefix to be used by other plugins (like chat plugins)
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, RankNotFoundException {

        if (args.size() >= 2) {

            Rank rank = RankManager.getRank(args.get(0));
            if (rank != null) {

                if (!args.get(1).equalsIgnoreCase("remove")) {

                    // Builds the prefix and adds a space at the end, because chats that don't have spacing between names and prefixes make me sad
                    String prefix = "";
                    for (int i = 1; i < args.size(); i++) {
                        prefix = prefix + args.get(i) + " ";
                    }
                    rank.setMetaPrefix(prefix);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aPrefix for §n" + rank.getName() + "§a set: §f" + rank.getMetaPrefix());
                } else {
                    rank.setMetaPrefix(null);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aPrefix for §n" + rank.getName() + "§a removed.");
                }

                // Saves the group to datastore
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
