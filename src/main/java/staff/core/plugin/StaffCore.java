package staff.core.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import staff.core.plugin.commands.StaffChat;
import staff.core.plugin.listeners.FilterListener;

public class StaffCore extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("s").setExecutor(new StaffChat());

        getServer().getPluginManager().registerEvents(new FilterListener(), this);
    }

    @Override
    public void onDisable() {
    }
}
