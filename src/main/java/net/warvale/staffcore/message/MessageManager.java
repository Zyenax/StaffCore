package net.warvale.staffcore.message;

import net.md_5.bungee.api.ChatColor;
import net.warvale.staffcore.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import net.warvale.staffcore.config.ConfigManager;

import java.util.HashMap;
import java.util.logging.Level;

public class MessageManager {

    private static MessageManager instance;
    private static HashMap<PrefixType, String> prefix = new HashMap<>();

    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
        }
        return instance;
    }

    public void setup() {
        FileConfiguration f = ConfigManager.getMessages();
        prefix.put(PrefixType.MAIN, ChatColor.translateAlternateColorCodes('&', f.getString("prefix.main")));
        prefix.put(PrefixType.ALERT, ChatColor.translateAlternateColorCodes('&', f.getString("prefix.alert")));
        prefix.put(PrefixType.STAFF, ChatColor.translateAlternateColorCodes('&', f.getString("prefix.staff")));
    }

    /**
     * Broadcasts a message to everyone online.
     *
     * @param type the prefix to use
     * @param message the message.
     */
    public static void broadcast(PrefixType type, String message) {
        broadcast(type, message, null);
    }

    public static void broadcast(PrefixType type, String message, String permission) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (permission != null && !online.hasPermission(permission)) {
                continue;
            }

            online.sendMessage(getPrefix(type) + message);
        }

        message = message.replaceAll("§l", "");
        message = message.replaceAll("§o", "");
        message = message.replaceAll("§r", "§f");
        message = message.replaceAll("§m", "");
        message = message.replaceAll("§n", "");

        StaffCore.get().getLogger().log(Level.INFO, message);
    }

    public static String getPrefix(PrefixType type) {
        return prefix.get(type) + " " + ChatColor.RESET;
    }

}
