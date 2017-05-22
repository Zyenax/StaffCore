package net.warvale.staffcore.commands.admin.rank;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.permissions.Privilege;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RankView extends SubCommand {

    public RankView(AbstractCommand command) {
        super("view", command, "Shows group information", "<User>");
    }

    // Outposts information about a group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, RankNotFoundException {

        if (args.size() == 1) {

            Rank rank = RankManager.getRank(args.get(0));

            // Ensures the group exists
            if (rank != null) {

                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§e§n" + rank.getName() + " information:");
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§ePriority: " + rank.getPriority() + (rank.isDefault() ? " §7§o(Default)" : ""));
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eInheriting privileges from " + rank.getInheritance().size() + " ranks(s):");
                for (Rank inherited : rank.getInheritance()) {
                    sender.sendMessage("§b§l-> §b" + inherited.getName() + (inherited.isConditional() ? " §6§o(Conditional)" : "") + (inherited.isDefault() ? " §7§o(Default)" : ""));
                }
                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§e" + rank.getPrivileges().size() + " defined privilege(s):");
                for (Privilege privilege : rank.getPrivileges()) {
                    sender.sendMessage("§b§l-> §b" + privilege.getNode() + (privilege.isNegated() ? " §c§o(Negated)" : "") + (privilege.isConditional() ? " §6§o(Conditional)" : ""));
                }
                if (rank.getInheritance().size() > 0) {
                    Map<Privilege, Rank> inherited = rank.getInheritedPrivileges(null);
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eRank inherits " + inherited.size() + " privilege(s):");
                    ploop:
                    for (Privilege privilege : inherited.keySet()) {
                        Rank r = inherited.get(privilege);
                        for (Privilege p : rank.getPrivileges()) {
                            if (p.matches(privilege)) {
                                continue ploop;
                            }
                        }
                        sender.sendMessage("§b§l-> §b" + privilege.getNode() + "§o from " + r.getName() + (privilege.isNegated() ? " §c§o(Negated)" : "") + (privilege.isConditional() ? " §6§o(Conditional)" : ""));
                    }
                }
                if (rank.getWorlds().size() > 0) {
                    List<String> worlds = rank.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eConditional to " + worlds.size() + " world(s): " + worlds.toString().replace("[", "").replace("]", ""));
                } else {
                    sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eRank is not world specific.");
                }

                sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§eMeta:");
                sender.sendMessage("§b§l-> §bPrefix: " + (rank.getMetaPrefix() != null ? rank.getMetaPrefix() : "§c§oUndefined"));
                sender.sendMessage("§b§l-> §bSuffix: " + (rank.getMetaSuffix() != null ? rank.getMetaSuffix() : "§c§oUndefined"));


            } else {
                throw new RankNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
