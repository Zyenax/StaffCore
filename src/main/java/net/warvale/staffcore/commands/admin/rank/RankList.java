package net.warvale.staffcore.commands.admin.rank;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankList extends SubCommand {

    public RankList(AbstractCommand command) {
        super("list", command, "Shows group list", "");
    }

    // Outposts a list of all groups, ordered by priority ascending
    @Override
    protected void execute(CommandSender sender, List<String> args) {

        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eThere are " + RankManager.getRanks().size() + " rank(s):");
        List<Rank> ranks = new ArrayList<>();
        ranks.addAll(RankManager.getRanks());
        Collections.sort(ranks);
        for (Rank rank : ranks) {
            sender.sendMessage("§b§l-> §b" + rank.getName() + ", Priority: " + rank.getPriority());
        }

    }
}
