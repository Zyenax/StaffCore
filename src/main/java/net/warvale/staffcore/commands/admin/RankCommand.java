package net.warvale.staffcore.commands.admin;

import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.commands.SubCommand;
import net.warvale.staffcore.commands.admin.rank.RankCreate;
import net.warvale.staffcore.exceptions.CommandException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankCommand extends AbstractCommand {

    public RankCommand() {
        super("rank", "");

        //register subcommands
        getCommands().add(new RankCreate(this));

    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {

        if (!(sender instanceof Player)) {
            throw new CommandException("Only players can execute this command.");
        }

        if (args.length == 0) {
            if (getCommands().size() > 0) {
                sender.sendMessage("§a§l[WarvalePerms]§a " + getName() + " Commands");
                for (SubCommand sc : getCommands()) {
                    sender.sendMessage("§b§l-> §b/" + getName() + " " + sc.getName() + " " + (sc.getUsage() != null ? sc.getUsage() : ""));
                }
            } else {
                sender.sendMessage("§c§l[WarvalePerms]§c No register commands for provided scope.");
            }
        } else {

            // Match the second argument to a subcommand
            SubCommand subCmd = null;
            for (SubCommand sc : getCommands()) {
                if (sc.getName().equalsIgnoreCase(args[0])) {
                    subCmd = sc;
                    break;
                }
            }

            // Check if the command exists
            if (subCmd != null) {

                // Pass on any remaining arguments and execute the subcommand
                List<String> a = new ArrayList<>();
                a.addAll(Arrays.asList(args).subList(1, args.length));

                subCmd.run(sender, a);

            } else {
                sender.sendMessage("§c§l[WarvalePerms] §cUnrecognized command. Try \"/ " + getName() + "\"?");
            }

        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

}
