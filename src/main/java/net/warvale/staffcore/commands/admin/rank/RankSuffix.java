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

public class RankSuffix extends SubCommand {

    public RankSuffix(AbstractCommand command) {
        super("suffix", command, "Changes group suffix", "<Rank> <Suffix|Remove>");
    }

    // Sets a group's meta suffix to be used by other plugins (like chat plugins)
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, RankNotFoundException {

        if (args.size() >= 2) {

            Rank rank = RankManager.getRank(args.get(0));
            if (rank != null) {

                if (!args.get(1).equalsIgnoreCase("remove")) {
                    // Builds the suffix
                    String suffix = "";
                    for (int i = 1; i < args.size(); i++) {
                        suffix = suffix + " " + args.get(i);
                    }
                    suffix = suffix.substring(1);
                    rank.setMetaSuffix(suffix);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aSuffix for §n" + rank.getName() + "§a set: §f" + rank.getMetaSuffix());
                } else {
                    rank.setMetaSuffix(null);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aSuffix for §n" + rank.getName() + "§a removed.");
                }

                // Saves the rank to data store
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
