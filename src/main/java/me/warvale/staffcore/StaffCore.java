package me.warvale.staffcore;

import me.warvale.staffcore.commands.TPCommand;
import org.bukkit.plugin.java.JavaPlugin;
import me.warvale.staffcore.commands.GMCommand;
import me.warvale.staffcore.commands.StaffChat;
import me.warvale.staffcore.commands.punishments.BanCommand;
import me.warvale.staffcore.commands.punishments.IPCommand;
import me.warvale.staffcore.commands.punishments.KickCommand;
import me.warvale.staffcore.commands.punishments.WarnCommand;
import me.warvale.staffcore.listeners.FilterListener;

public class StaffCore extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("s").setExecutor(new StaffChat());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("gmute").setExecutor(new GMCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("ip").setExecutor(new IPCommand());
        getCommand("tp").setExecutor(new TPCommand());

        getServer().getPluginManager().registerEvents(new FilterListener(), this);
        getServer().getPluginManager().registerEvents(new GMCommand(), this);
    }
}
