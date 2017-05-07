package staff.core.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import staff.core.plugin.commands.GMCommand;
import staff.core.plugin.commands.StaffChat;
import staff.core.plugin.listeners.FilterListener;
import staff.core.plugin.listeners.GMListener;

public class StaffCore extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("s").setExecutor(new StaffChat());
        getCommand("globalmute").setExecutor(new GMCommand());

        getServer().getPluginManager().registerEvents(new FilterListener(), this);
        getServer().getPluginManager().registerEvents(new GMListener(), this);
    }

    @Override
    public void onDisable() {
    }
}
