package me.warvale.staffcore;

import me.warvale.staffcore.commands.*;
import me.warvale.staffcore.commands.punishments.BanCommand;
import me.warvale.staffcore.commands.punishments.KickCommand;
import me.warvale.staffcore.listeners.FilterListener;
import me.warvale.staffcore.rank.ChatFormatter;
import me.warvale.staffcore.rank.RankManager;
import me.warvale.staffcore.rank.commands.RankCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.IOException;

//import me.warvale.staffcore.commands.punishments.WarnCommand;

public class StaffCore extends JavaPlugin {

    private static StaffCore core;

    @Override
    public void onEnable() {
        Bukkit.getWorld("staffmap3").setFullTime(Bukkit.getWorld("staffmap3").getFullTime() + 24000);
        
        getCommand("alert").setExecutor(new AlertCommand());
        getCommand("s").setExecutor(new StaffChat());
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("gmute").setExecutor(new GlobalMuteCommand());
        //getCommand("warn").setExecutor(new WarnCommand());
        getCommand("ip").setExecutor(new IPCommand());
        getCommand("tp").setExecutor(new TPCommand());
        getCommand("rank").setExecutor(new RankCommand());
        //getCommand("check").setExecutor(new CheckCommand(this));

        getServer().getPluginManager().registerEvents(new FilterListener(), this);
        getServer().getPluginManager().registerEvents(new GlobalMuteCommand(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatter(), this);
        //getServer().getPluginManager().registerEvents(new SessionListener(this), this);

        core = this;

        try {
            RankManager.prep();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //public Rank(String name, String prefix, boolean staff, List<Rank> parents, List<Permission> permissions, String namecolor) throws IOException, ParseException
    }

    public static StaffCore get() {
        return core;
    }
}
