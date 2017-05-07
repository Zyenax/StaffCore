package staff.core.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import staff.core.plugin.commands.StaffChat;

public class StaffCore extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("s").setExecutor(new StaffChat());
    }

    @Override
    public void onDisable() {
    }
}