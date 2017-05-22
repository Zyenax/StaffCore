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

public class RankInherit extends SubCommand {

    public RankInherit(AbstractCommand command) {
        super("inherit", command, "Toggle group inheritance", "<Rank> <Rank to Inherit>");
    }

    // Allows a group to inherit permissions of another group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, RankNotFoundException {

        if (args.size() == 2) {

            Rank rank = RankManager.getRank(args.get(0));
            Rank toInherit = RankManager.getRank(args.get(1));

            // Ensures both groups exist

            if (rank != null) {

                if (toInherit != null) {

                    // Ensures that the group to inherit isn't itself (infinite loop...)
                    if (toInherit.getName().equals(rank.getName())) {
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§n" + rank.getName() + "§c can't inherit itself.");
                        return;
                    }

                    // Ensures that the group to inherit isn't already inherited by an inheritance of the group to inherit. (Another infinite loop...)
                    for (Rank subinheritance : toInherit.getInheritance()) {
                        if (subinheritance.getName().equals(rank.getName())) {
                            sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§n" + rank.getName() + "§c can't inherit from a group that inherits it.");
                            return;
                        }
                    }

                    // Adds or removes the inehrited group
                    if (rank.getInheritance().contains(toInherit)) {
                        rank.getInheritance().remove(toInherit);
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§n" + rank.getName() + "§a will no longer inherit permissions from §n" + toInherit.getName() + "§a.");
                    } else {
                        rank.getInheritance().add(toInherit);
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) +  "§a§n" + rank.getName() + "§a will now inherit permissions from §n" + toInherit.getName() + "§a.");
                    }

                    // Saves the group to datastore
                    final Rank finalRank = rank;
                    StaffCore.doAsync(() -> RankManager.saveRank(finalRank));

                } else {
                    throw new RankNotFoundException(args.get(1));
                }
            } else {
                throw new RankNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }

}
