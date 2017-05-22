package net.warvale.staffcore.commands;

import java.util.ArrayList;
import java.util.List;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.exceptions.CommandException;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand extends Parser {
    private String name, usage;

    private List<SubCommand> commands = new ArrayList<>();


    /**
     * Constructor for the uhc command super class.
     *
     * @param name The name of the command.
     * @param usage the command usage (after /command)
     */
    public AbstractCommand(String name, String usage, SubCommand... subCommands) {
        this.usage = usage;
        this.name = name;

        for(SubCommand subCommand : subCommands) {
            commands.add(subCommand);
        }
    }

    protected StaffCore plugin;
    
    /**
     * Setup the instances needed.
     * 
     * @param plugin The plugin instance.
     */
    protected void setupInstances(StaffCore plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Get the name of the command used after the /
     *
     * @return The command name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the usage of the command
     * <p>
     * Usage can be /nameofcommand [argurments...]
     *
     * @return The command usage.
     */
    public String getUsage() {
        return "/" + name + " " + usage;
    }

    /**
     * Return the permission of the command
     * <p>
     * The permission will be warvale.[nameofcommand]
     *
     * @return The command permission.
     */
    public String getPermission() {
        return "warvale." + name;
    }

    /**
     * Execute the command.
     *
     * @param sender The sender of the command.
     * @param args The argurments typed after the command.
     * @return True if successful, false otherwise. Returning false will send usage to the sender.
     *
     * @throws CommandException If anything was wrongly typed this is thrown sending the sender a warning.
     */
    public abstract boolean execute(CommandSender sender, String[] args) throws CommandException;

    /**
     * Tab complete the command.
     *
     * @param sender The sender of the command.
     * @param args The argurments typed after the command
     * @return A list of tab completable argurments.
     */
    public abstract List<String> tabComplete(CommandSender sender, String[] args);

    /**
     * Turn a the given boolean into "Enabled" or "Disabled".
     *
     * @param converting The boolean converting.
     * @return The converted boolean.
     */
    public String booleanToString(boolean converting) {
        return converting ? "enabled" : "disabled";
    }

    public List<SubCommand> getCommands() {
        return commands;
    }
}