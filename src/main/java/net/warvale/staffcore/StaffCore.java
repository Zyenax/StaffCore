package net.warvale.staffcore;

import net.warvale.staffcore.bossbar.BarManager;
import net.warvale.staffcore.commands.*;
import net.warvale.staffcore.config.ConfigManager;
import net.warvale.staffcore.listeners.ChatListener;
import net.warvale.staffcore.listeners.SessionListener;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.rank.RankManager;
import net.warvale.staffcore.utils.files.PropertiesFile;
import net.warvale.staffcore.utils.sql.SQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;

public class StaffCore extends JavaPlugin {

    private static boolean shutdown = false;
    private static StaffCore core;

    //sql stuff
    private final File sqlpropertiesfile = new File("connect.properties");
    private PropertiesFile propertiesFile;
    private static SQLConnection db;

    //command handler
    private static CommandHandler cmds;

    @Override
    public void onEnable() {
        core = this;
        Bukkit.getWorld("staffmap3").setFullTime(Bukkit.getWorld("staffmap3").getFullTime() + 24000);

        ConfigManager.getInstance().setup();
        MessageManager.getInstance().setup();

        RankManager.loadRanks();
        
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new SessionListener(this), this);

        cmds = new CommandHandler(this);
        cmds.registerCommands();

        BarManager.getInstance().setup();

    }

    @Override
    public void onLoad() {

        //Loading properties
        getLogger().log(Level.INFO, "Loading SQL Properties");

        if (!sqlpropertiesfile.exists()) {
            try {
                PropertiesFile.generateFresh(sqlpropertiesfile, new String[]{"hostname", "port", "username", "password", "database"}, new String[]{"localhost", "3306", "root", "NONE", "crnetwork"});
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "Could not generate fresh properties file");
            }
        }

        try {
            propertiesFile = new PropertiesFile(sqlpropertiesfile);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Could not load SQL properties file", e);
            endSetup("Exception occurred when loading properties");
        }

        String temp;
        getLogger().log(Level.INFO, "Finding database information...");

        //SQL info
        try {
            db = new SQLConnection(propertiesFile.getString("hostname"), propertiesFile.getNumber("port").intValue(), propertiesFile.getString("database"), propertiesFile.getString("username"), (temp = propertiesFile.getString("password")).equals("NONE") ? null : temp);
        } catch (ParseException ex) {
            getLogger().log(Level.WARNING, "Could not load database information", ex);
            endSetup("Invalid database port");
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Could not load database information", ex);
            endSetup("Invalid configuration");
        }

        //Connecting to MySQL
        getLogger().log(Level.INFO, "Connecting to MySQL...");

        try {
            getDB().openConnection();
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Could not connect to MySQL", e);
            endSetup("Could not establish connection to database");
        }

    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Closing connection to database...");

        try {
            getDB().closeConnection();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Could not close database connection", e);
        }
    }


    public static StaffCore get() {
        return core;
    }

    public static SQLConnection getDB() {
        return db;
    }

    public PropertiesFile getProperties() {
        return propertiesFile;
    }

    public void endSetup(String s) {
        getLogger().log(Level.SEVERE, s);
        if (!shutdown) {
            stop();
            shutdown = true;
        }
        throw new IllegalArgumentException("Disabling... " + s);
    }

    private void stop() {
        Bukkit.getServer().shutdown();
    }

    public static void doAsync(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(get(), runnable);
    }
}
