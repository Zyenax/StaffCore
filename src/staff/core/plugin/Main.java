package staff.core.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import staff.core.plugin.commands.SC;

public class Main extends JavaPlugin {
	  
	@Override
    public void onEnable() {
    
    	getCommand("sc").setExecutor(new SC());
	}
    @Override
    public void onDisable() {    	
}
}