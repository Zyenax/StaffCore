package staff.core.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import staff.core.plugin.commands.GMCommand;
import staff.core.plugin.commands.StaffChat;
import staff.core.plugin.commands.punishments.BanCommand;
import staff.core.plugin.commands.punishments.KickCommand;
import staff.core.plugin.commands.punishments.WarnCommand;
import staff.core.plugin.listeners.FilterListener;

public class StaffCore extends JavaPlugin {

    @Override
    public void onEnable() {

        getCommand("s").setExecutor(new StaffChat());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("gmute").setExecutor(new GMCommand());
        getCommand("warn").setExecutor(new WarnCommand());

        getServer().getPluginManager().registerEvents(new FilterListener(), this);
        getServer().getPluginManager().registerEvents(new GMCommand(), this);
    }
}
