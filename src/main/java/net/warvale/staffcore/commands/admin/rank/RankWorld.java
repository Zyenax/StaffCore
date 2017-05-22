package net.warvale.staffcore.commands.admin.rank;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.InsufficientArgumentTypeException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class RankWorld extends SubCommand {

    public RankWorld(AbstractCommand command) {
        super("world", command, "Sets group access per-world", "<Rank> <World>");
    }

    // Sets world restrictions for groups
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, RankNotFoundException {

        if (args.size() == 2) {

            Rank rank = RankManager.getRank(args.get(0));
            World world = Bukkit.getWorld(args.get(1));

            // Ensures the group exists
            if (rank != null) {

                // Ensures the world is valid
                if (world != null) {

                    if (rank.getWorlds().contains(world)) {
                        rank.getWorlds().remove(world);
                    } else {
                        rank.getWorlds().add(world);
                    }

                    // Informs the user about the new world preferences
                    if (rank.getWorlds().size() > 0) {
                        List<String> worlds = rank.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§a§n" + rank.getName() + "§a now available in world(s): " + worlds.toString().replace("[", "").replace("]", ""));
                    } else {
                        sender.sendMessage(MessageManager.getPrefix(PrefixType.PERMS) + "§a§n" + rank.getName() + " §anow available in all worlds.");
                    }

                    // Saves group to datastore
                    final Rank finalRank = rank;
                    StaffCore.doAsync(() -> RankManager.saveRank(finalRank));

                } else {
                    List<String> worlds = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                    throw new InsufficientArgumentTypeException("WORLD", args.get(1), worlds.toString().replace("[", "").replace("]", ""));
                }
            } else {
                throw new RankNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
