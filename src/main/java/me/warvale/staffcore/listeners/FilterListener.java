package me.warvale.staffcore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Callum for StaffCore.
 */
public class FilterListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Pattern regex = Pattern.compile("\\bf+(\\W)*(u|v|a|@)+(\\W)*c+(\\W)*k+(\\W)*\\b");
        Matcher matcher = regex.matcher(event.getMessage().toLowerCase());

        if(matcher.matches() || matcher.find()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Please don't swear!");
        }
    }
}
