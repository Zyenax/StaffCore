package net.warvale.staffcore.commands.admin.rank;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RankPriority extends SubCommand {

    public RankPriority(AbstractCommand command) {
        super("priority", command, "Adjusts group priority", "<Rank> <Priority>");
    }

    // Adjusts the group priority
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, RankNotFoundException {

        if (args.size() == 2) {

            Rank rank = RankManager.getRank(args.get(0));
            if (rank != null) {

                try {

                    int priority = Integer.parseInt(args.get(1));
                    rank.setPriority(priority);

                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§aAdjusted priority for §n" + rank.getName() + "§a to " + priority + ".");

                    // Informs the player if the change has changed their default group.
                    boolean lowest = true;
                    for (Rank r : RankManager.getRanks()) {
                        if (r.getName().equalsIgnoreCase(rank.getName())) continue;
                        if (r.getPriority() < rank.getPriority()) {
                            lowest = false;
                            break;
                        }
                    }

                    if (lowest) {
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§a§n" + rank.getName() + "§a is your default group.");
                    }

                    // Saves group to datastore
                    final Rank finalRank = rank;
                    StaffCore.doAsync(() -> RankManager.saveRank(finalRank));

                } catch (NumberFormatException ex) {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§cPriority must be numeric.");
                }

            } else {
                throw new RankNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
