package net.warvale.staffcore.commands.basic;

import net.md_5.bungee.api.ChatColor;
import net.warvale.staffcore.commands.AbstractCommand;
import net.warvale.staffcore.exceptions.CommandException;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.users.User;
import net.warvale.staffcore.users.UserManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ColorCommand extends AbstractCommand {

    public ColorCommand() {
        super("color", "<chatcolor>");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws CommandException {

        if (!(sender instanceof Player)) {
            throw new CommandException("Only players can execute this command.");
        }

        if (args.length == 0) {
            return false;
        }

        Player player = (Player) sender;

        User user = UserManager.getUser(player.getUniqueId());

        if (user != null) {

            try {

                ChatColor nameColor = ChatColor.valueOf(args[0]);
                user.setNameColor(nameColor);

                UserManager.saveUser(user);

                player.sendMessage(MessageManager.getPrefix(PrefixType.MAIN) + "Your chat color preference has been saved.");


            } catch (Exception ex) {
                throw new CommandException("That is not a valid color");
            }

        } else  {
            throw new CommandException("Could not find your account in our database, this may be a temporary issue, please try again soon.");
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> toReturn = new ArrayList<>();

        toReturn.add(ChatColor.BLACK.toString());
        toReturn.add(ChatColor.DARK_BLUE.toString());
        toReturn.add(ChatColor.DARK_GREEN.toString());
        toReturn.add(ChatColor.DARK_AQUA.toString());
        toReturn.add(ChatColor.DARK_RED.toString());
        toReturn.add(ChatColor.DARK_PURPLE.toString());
        toReturn.add(ChatColor.GOLD.toString());
        toReturn.add(ChatColor.GRAY.toString());
        toReturn.add(ChatColor.DARK_GRAY.toString());
        toReturn.add(ChatColor.BLUE.toString());
        toReturn.add(ChatColor.GREEN.toString());
        toReturn.add(ChatColor.AQUA.toString());
        toReturn.add(ChatColor.RED.toString());
        toReturn.add(ChatColor.LIGHT_PURPLE.toString());
        toReturn.add(ChatColor.YELLOW.toString());
        toReturn.add(ChatColor.WHITE.toString());

        return toReturn;
    }
}
