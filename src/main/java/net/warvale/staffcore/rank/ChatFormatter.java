package net.warvale.staffcore.rank;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Draem on 5/10/2017.
 */
public class ChatFormatter implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Rank rank = RankManager.getRankForUser(event.getPlayer().getName());

        if (rank != null) {
            event.setFormat(
                    ChatColor.translateAlternateColorCodes(
                            '&', rank.getPrefix() // prefix
                                    + rank.getNamecolor() //name color
                                    + event.getPlayer().getName() //Username
                                    + "&7" //: Color
                                    + ": " + (rank.getId().equalsIgnoreCase("default") ? "&7" : "&f"))
                                    + (rank.isStaff() ? ChatColor.translateAlternateColorCodes('&', event.getMessage()) : event.getMessage())); //Chat and chat color x2

        } else {
            event.setFormat(ChatColor.translateAlternateColorCodes('&', "&7" + event.getPlayer().getName() + "&7: &f" + event.getMessage()));
        }
    }

}
