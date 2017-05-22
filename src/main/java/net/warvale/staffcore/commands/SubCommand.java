package net.warvale.staffcore.commands;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.exceptions.InsufficientArgumentException;
import net.warvale.staffcore.exceptions.InsufficientArgumentTypeException;
import net.warvale.staffcore.exceptions.RankNotFoundException;
import net.warvale.staffcore.exceptions.UserNotFoundException;
import net.warvale.staffcore.users.User;
import net.warvale.staffcore.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Level;

public abstract class SubCommand {

    // The command name
    private String name;

    // The parent command
    private AbstractCommand masterCommand;

    // A description of the command
    private String description;

    // The recommended usage
    private String usage;


    public SubCommand(String name, AbstractCommand masterCommand, String description, String usage) {
        this.name = name;
        this.masterCommand = masterCommand;
        this.description = description;
        this.usage = usage;
        StaffCore.get().getLogger().log(Level.INFO, "Registered " + masterCommand.getName() + " command: " + getName());
    }

    public void run(CommandSender sender, List<String> args) {

        // All commands are async'd as to not interrupt the main thread
        StaffCore.doAsync(() -> {

            // If the sender is a player, check their permissions
            if ((sender instanceof Player) && getPermission() != null) {
                Player player = (Player) sender;
                User user = UserManager.getUser(player);
                if (!player.hasPermission(getPermission()) && (user == null || !user.isSuperUser())) {
                    sender.sendMessage("§c§l[WarvalePerms] §cYou don't have permission for that.");
                    return;
                }
            }

            // Handle ALL the exceptions
            try {
                execute(sender, args);

                // After running a command, rebuilds online user permissions. This ensures that bukkit is up to date with any changes that happened.
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            User user = UserManager.getUser(player);
                            if (user != null && user.getAttachment() != null) {
                                user.refreshPermissions(player.getWorld());
                            }
                        }
                    }
                }.runTask(StaffCore.get());

            } catch (InsufficientArgumentException e) {
                sender.sendMessage("§c§l[WarvalePerms] §cInsufficient arguments. (Supplied " + args.size() + "/" + e.getRequired() + ")");
                sender.sendMessage("§c§l[WarvalePerms] §cProper Usage: /perms " + getMasterCommand().getName() + " " + getName() + " " + (getUsage() != null ? getUsage() : ""));
            } catch (UserNotFoundException e) {
                sender.sendMessage("§c§l[WarvalePerms] §cSupplied user doesn't exist: \"" + e.getUser() + "\"");
            } catch (InsufficientArgumentTypeException e) {
                sender.sendMessage("§c§l[WarvalePerms] §cInvalid argument(s). " + e.getArgument() + " expected: " + e.getExpected() + "; Received: " + e.getReceived());
            } catch (RankNotFoundException e) {
                sender.sendMessage("§c§l[WarvalePerms] §cSupplied group doesn't exist: \"" + e.getRank() + "\"");
            }
        });


    }

    protected abstract void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, RankNotFoundException;

    public String getName() {
        return name;
    }

    public AbstractCommand getMasterCommand() {
        return masterCommand;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return "/" + masterCommand.getName() + " " + getName() + usage;
    }

    public String getPermission() {
        return "warvale." + masterCommand.getName() + "." + getName();
    }
}
