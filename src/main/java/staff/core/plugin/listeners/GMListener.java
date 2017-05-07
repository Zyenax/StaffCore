package staff.core.plugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import staff.core.plugin.commands.GMCommand;


public class GMListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if(GMCommand.ismuted && !p.hasPermission("warvale.staff")) {
            p.sendMessage("§cThe chat is currently muted! Please wait whilst we resolve the problem, please be patient.");
            e.setCancelled(true);
        }
    }
}
