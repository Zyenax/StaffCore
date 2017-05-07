package staff.core.plugin.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import staff.core.plugin.StaffCore;

/**
 * Created by Laep for StaffCore.
 */
public class ChatEvent implements Listener {

    private StaffCore plugin;

    public ChatEvent(StaffCore plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (plugin.getConfig().getStringList("blacklist") == null) return;

        for (String word : plugin.getConfig().getStringList("blacklist")) {
            if (e.getMessage().contains(word)) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease don't curse! This may result in a mute."));
                e.setCancelled(true);
            }
        }
    }


}
