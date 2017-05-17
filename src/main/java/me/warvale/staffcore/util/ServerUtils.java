package me.warvale.staffcore.util;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Draem on 5/8/2017.
 */
public class ServerUtils {

    public static void ban(Player target, Player player, String reason, Date expires) {
        if (expires == null) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(),
                    ChatColor.translateAlternateColorCodes('&', "&c&lYou have been permanently banned by " + player.getName() + "!\n&f" + reason),
                    expires,
                    player.getName());
        } else {
            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(),
                    ChatColor.translateAlternateColorCodes('&', "&c&lYou have been banned until " + new SimpleDateFormat("yyyy-MM-dd").format(expires) + " by " + player.getName() + "!\n&f" + reason),
                    expires,
                    player.getName());
        }
    }

}

