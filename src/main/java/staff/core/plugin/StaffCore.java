package staff.core.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import staff.core.plugin.commands.StaffChat;
import staff.core.plugin.commands.punishments.BanCommand;
import staff.core.plugin.commands.punishments.KickCommand;
import staff.core.plugin.listeners.FilterListener;

public class StaffCore extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("s").setExecutor(new StaffChat());
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("kick").setExecutor(new KickCommand(this));

        getServer().getPluginManager().registerEvents(new FilterListener(), this);
    }

    @Override
    public void onDisable() {
    }
}
