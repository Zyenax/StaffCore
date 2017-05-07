package staff.core.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import main.java.staff.core.plugin.commands.GMCommand;
import main.java.staff.core.plugin.commands.StaffChat;
import main.java.staff.core.plugin.commands.punishments.BanCommand;
import main.java.staff.core.plugin.commands.punishments.IPCommand;
import main.java.staff.core.plugin.commands.punishments.KickCommand;
import main.java.staff.core.plugin.commands.punishments.WarnCommand;
import main.java.staff.core.plugin.listeners.FilterListener;

public class StaffCore extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("s").setExecutor(new StaffChat());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("gmute").setExecutor(new GMCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("ip").setExecutor(new IPCommand());

        getServer().getPluginManager().registerEvents(new FilterListener(), this);
        getServer().getPluginManager().registerEvents(new GMCommand(), this);
    }
}
