package me.warvale.staffcore.rank;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Draem on 5/10/2017.
 */
public class ChatFormatter implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Rank rank = RankManager.getRankForUser(event.getPlayer());

        event.setFormat(
                ChatColor.translateAlternateColorCodes(
                        '&', rank.getPrefix() // prefix
                                + rank.getNamecolor() //name color
                                + event.getPlayer().getName() //Username
                                + "&f" //: Color
                                + ":" + (rank.getId().equalsIgnoreCase("default") ? "&7" : "&f")) //: and chat color
                                + (rank.isStaff() ? ChatColor.translateAlternateColorCodes('&', event.getMessage()) : event.getMessage()) //Chat and chat color x2
        );
    }

}
