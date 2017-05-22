package net.warvale.staffcore.listeners;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.utils.PlayerManager;
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
