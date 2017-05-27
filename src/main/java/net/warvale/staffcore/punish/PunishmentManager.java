package net.warvale.staffcore.punish;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.punish.data.Punishment;
import net.warvale.staffcore.punish.data.PunishmentType;
import net.warvale.staffcore.punish.data.Reason;
import net.warvale.staffcore.utils.sql.SQLConnection;
import net.warvale.staffcore.utils.sql.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by Draem on 5/21/2017.
 */
public class PunishmentManager implements Listener {

    public static HashMap<UUID, UUID> punishing = new HashMap<>();

    private static List<Punishment> mutes = new ArrayList<>();
    private static List<Punishment> bans = new ArrayList<>();

    public static Punishment registerPunishment(Punishment punishment) {
        SQLConnection connection = StaffCore.getDB();
        HashMap<String, Object> punish = new HashMap<>();
        punish.put("uuid", punishment.getUuid().toString());
        punish.put("type", punishment.getType().toString());
        punish.put("date", punishment.getDate());
        punish.put("expiration", punishment.getExpires());
        punish.put("punisher_uuid", punishment.getStaff().toString());
        punish.put("reason", punishment.getReason().toString());
        punish.put("active", true);

        try {
            SQLUtil.execute(connection, "punishments", punish);
            SQLUtil.update(connection, "users", "punishments", (SQLUtil.query(connection, "users", "punishments", new SQLUtil.Where("`uuid` = " + punishment.getUuid())).getInt("punishments") + 1), new SQLUtil.Where("`uuid` = " + punishment.getUuid()));
        } catch (ClassNotFoundException | SQLException e) {
            StaffCore.get().getLogger().log(Level.SEVERE, "UNABLE TO REGISTER PUNISHMENT TO DATABASE", e);
            return null;
        }

        punishment.setActive(true);
        return punishment;
    }

    public static Punishment revertPunishment(Punishment punishment) {
        SQLConnection connection = StaffCore.getDB();
        StringBuilder builder = new StringBuilder();
        builder.append("`uuid` = ").append(punishment.getUuid().toString());
        builder.append(" AND `type` = ").append(punishment.getType().toString());
        builder.append(" AND `punisher_uuid` = ").append(punishment.getStaff().toString());
        builder.append(" AND `reason` = ").append(punishment.getReason().toString());
        builder.append(" AND `active` = ").append(true);

        try {
            SQLUtil.update(connection, "punishments", "active", false, new SQLUtil.Where(builder.toString()));
            SQLUtil.update(connection, "users", "punishments", (SQLUtil.query(connection, "users", "punishments", new SQLUtil.Where("`uuid` = " + punishment.getUuid())).getInt("punishments") - 1), new SQLUtil.Where("`uuid` = " + punishment.getUuid()));
        } catch (ClassNotFoundException | SQLException e) {
            StaffCore.get().getLogger().log(Level.SEVERE, "UNABLE TO REVERT A PUNISHMENT IN DATABASE", e);
            return null;
        }

        punishment.setActive(false);
        return punishment;
    }

    public static boolean pastMute(Player player, PunishmentType type) {
        SQLConnection connection = StaffCore.getDB();
        StringBuilder builder = new StringBuilder();
        builder.append("`uuid` = ").append(player.getUniqueId().toString());
        builder.append(" AND `type` = ").append(type.toString());

        try {
            return (SQLUtil.query(connection, "punishments", "*", new SQLUtil.Where(builder.toString())).next());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updatePunishments() throws SQLException, ClassNotFoundException {
        mutes.clear();
        bans.clear();
        ResultSet results = SQLUtil.query(StaffCore.getDB(), "punishments", "*", new SQLUtil.Where("`active` = " + String.valueOf(true)));

        while (results.next()) {
            Date date = results.getDate("expiration");
            Date current = new Date();

            if (date.after(current) || date.equals(current)) {
                String builder = "`uuid` = " + results.getString("uuid") +
                        " AND `type` = " + results.getString("type") +
                        " AND `punisher_uuid` = " + results.getString("punisher_uuid") +
                        " AND `reason` = " + results.getString("reason") +
                        " AND `active` = " + true;
                SQLUtil.update(StaffCore.getDB(), "punishments", "active", false, new SQLUtil.Where(builder));
            } else if (date.before(current)) {
                switch (results.getString("type")) {
                    case "mute":
                        mutes.add(new Punishment(UUID.fromString(results.getString("uuid")),
                                results.getDate("date"),
                                results.getDate("expiration"),
                                UUID.fromString(results.getString("punisher_uuid")),
                                Reason.reasons.stream().filter(reason -> {
                                    try {
                                        return equals(reason.getReason(), results.getString("reason"));
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    return false;
                                }).findFirst().get()
                        ));
                        break;
                    case "ban":
                        bans.add(new Punishment(UUID.fromString(results.getString("uuid")),
                                results.getDate("date"),
                                results.getDate("expiration"),
                                UUID.fromString(results.getString("punisher_uuid")),
                                Reason.reasons.stream().filter(reason -> {
                                    try {
                                        return equals(reason.getReason(), results.getString("reason"));
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    return false;
                                }).findFirst().get()
                        ));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static boolean equals(String str, String str1) {
        return str.equals(str1);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerLoginEvent event) {
        if (bans.stream().anyMatch(punishment -> Bukkit.getOfflinePlayer(punishment.getUuid()).getPlayer().equals(event.getPlayer()))) {
            Punishment pun = bans.stream().filter(punishment -> punishment.getUuid().equals(event.getPlayer().getUniqueId())).findFirst().get();
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                    ChatColor.translateAlternateColorCodes('&', "&cYou have been banned from Warvale Network.\n&f" +
                    pun.getReason().getReason() + "\n\n" + "&8&lExpires: &r&7" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(pun.getExpires())));
        }
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (mutes.stream().anyMatch(punishment -> Bukkit.getOfflinePlayer(punishment.getUuid()).getPlayer().equals(event.getPlayer()))) {
            Punishment pun = bans.stream().filter(punishment -> punishment.getUuid().equals(event.getPlayer().getUniqueId())).findFirst().get();
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have been muted until &e" +
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(pun.getExpires()) + " &cby &e" + Bukkit.getOfflinePlayer(pun.getStaff()).getName() + " &cfor &e" +
            pun.getReason().getReason()));
        }
    }

}
