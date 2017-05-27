package net.warvale.staffcore.listeners;

import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.users.User;
import net.warvale.staffcore.users.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private static ChatListener instance;

    public static ChatListener getInstance() {
        if (instance == null) {
            instance = new ChatListener();
        }
        return instance;
    }

    private boolean muted;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(isMuted() && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Chat is currently disabled.");
        }

        User user = UserManager.getUser(event.getPlayer());

        event.setFormat(ChatColor.translateAlternateColorCodes('&', user.getPrefix()) + ChatColor.RESET +
                ChatColor.WHITE + user.getName() + ChatColor.RESET + ChatColor.GRAY + ": " + event.getMessage());
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public void toggle() {
        if (isMuted()) {
            setMuted(false);
            MessageManager.broadcast(PrefixType.MAIN, ChatColor.RED + "Global mute has been deactivated.");
        } else {
            setMuted(true);
            MessageManager.broadcast(PrefixType.MAIN, ChatColor.GREEN + "Global mute has been activated. This may be because we have found an issue.");
        }
    }

}
