package staff.core.plugin.listeners;

import com.google.common.base.Strings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Callum for StaffCore.
 */
public class FilterListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Pattern regex = Pattern.compile("\\bf+(\\W)*(u|v|a|@)+(\\W)*c+(\\W)*k+(\\W)*\\b");
        Matcher matcher = regex.matcher(event.getMessage());

        if(matcher.find()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Don't swear!");
        }
    }
}
