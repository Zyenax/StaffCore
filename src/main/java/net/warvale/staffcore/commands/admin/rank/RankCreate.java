package net.warvale.staffcore.commands.admin.rank;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RankCreate extends SubCommand {

    public RankCreate(AbstractCommand command) {
        super("create", command, "Creates a new group", "<Name>");
    }

    // Creates a new permission rank
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException {

        if (args.size() == 1) {

            // Checks to make sure the group doesn't already exist
            Rank rank = RankManager.getRank(args.get(0));
            if (rank == null) {

                // Sets the new groups default priority as one higher than any existing groups
                int priority = 0;
                for (Rank r : RankManager.getRanks()) {
                    if (priority <= r.getPriority()) {
                        priority = r.getPriority() + 1;
                    }
                }

                // Instantiates the new group and adds it to the GroupManager.
                rank = new Rank(args.get(0).replace("_", " "), priority);
                RankManager.getRanks().add(rank);

                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aCreated the §n" + rank.getName() + "§a rank.");
                if (RankManager.getDefaultRank() != null && RankManager.getDefaultRank().getName().equals(rank.getName())) {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§a§n" + rank.getName() + "§a is your new default rank.");
                }

                // Saves the new group
                final Rank finalRank = rank;
                StaffCore.doAsync(() -> RankManager.saveRank(finalRank));

            } else {
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cRank already exists: \"" + args.get(0) + "\"");
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }

}
