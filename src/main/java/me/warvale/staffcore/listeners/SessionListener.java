package me.warvale.staffcore.listeners;

import me.warvale.staffcore.StaffCore;
import me.warvale.staffcore.util.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Callum for StaffCore.
 */
public class SessionListener implements Listener {
	private StaffCore plugin;
	
	public SessionListener(StaffCore plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		PlayerManager playerManager = new PlayerManager(plugin);
		
		if(playerManager.get() == null) {
			playerManager.create(event.getPlayer());
			playerManager.save();
		}
	}
}
