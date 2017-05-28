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
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
        punish.put("date", new java.sql.Date(punishment.getDate().getTime()));
        punish.put("expiration", new java.sql.Date(punishment.getExpires().getTime()));
        punish.put("punisher_uuid", punishment.getStaff().toString());
        punish.put("reason", punishment.getReason().toString());
        punish.put("active", 1);

        try {
            SQLUtil.execute(connection, "punishments", punish);
            ResultSet resultSet = SQLUtil.query(connection, "users", "punishments", new SQLUtil.Where("`uuid` = \""+ punishment.getUuid() + "\""));
            Integer punishments = 0;
            if (resultSet.next()) {
                punishments = resultSet.getInt("punishments");
            }
            SQLUtil.update(connection, "users", "punishments", (punishments + 1), new SQLUtil.Where("`uuid` = \"" + punishment.getUuid() + "\""));
        } catch (ClassNotFoundException | SQLException e) {
            StaffCore.get().getLogger().log(Level.SEVERE, "UNABLE TO REGISTER PUNISHMENT TO DATABASE", e);
            return null;
        }

        try {
            updatePunishments();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        punishment.setActive(true);
        return punishment;
    }

    public static Punishment revertPunishment(Punishment punishment) {
        SQLConnection connection = StaffCore.getDB();
        StringBuilder builder = new StringBuilder();
        builder.append("`uuid` = ").append("\"").append(punishment.getUuid().toString()).append("\"");
        builder.append(" AND `type` = ").append("\"").append(punishment.getType().toString()).append("\"");
        builder.append(" AND `punisher_uuid` = ").append("\"").append(punishment.getStaff().toString()).append("\"");
        builder.append(" AND `reason` = ").append("\"").append(punishment.getReason().toString()).append("\"");
        builder.append(" AND `active` = ").append(1);

        try {
            SQLUtil.update(connection, "punishments", "active", 0, new SQLUtil.Where(builder.toString()));
            ResultSet resultSet = SQLUtil.query(connection, "users", "punishments", new SQLUtil.Where("`uuid` = \""+ punishment.getUuid() + "\""));
            Integer punishments = 0;
            if (resultSet.next()) {
                punishments = resultSet.getInt("punishments");
            }
            SQLUtil.update(connection, "users", "punishments", (punishments - 1), new SQLUtil.Where("`uuid` = \"" + punishment.getUuid() + "\""));
        } catch (ClassNotFoundException | SQLException e) {
            StaffCore.get().getLogger().log(Level.SEVERE, "UNABLE TO REVERT A PUNISHMENT IN DATABASE", e);
            return null;
        }
        try {
            updatePunishments();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        punishment.setActive(false);
        return punishment;
    }

    public static boolean pastMute(Player player, PunishmentType type) {
        SQLConnection connection = StaffCore.getDB();
        StringBuilder builder = new StringBuilder();
        builder.append("`uuid` = ").append("\"").append(player.getUniqueId().toString()).append("\"");
        builder.append(" AND `type` = ").append("\"").append(type.toString()).append("\"");

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
        ResultSet results = SQLUtil.query(StaffCore.getDB(), "punishments", "*", new SQLUtil.Where("`active` = " + 1));

        while (results.next()) {
            java.util.Date date = results.getDate("expiration");
            java.util.Date current = new java.util.Date();

            if (date.before(current) || date.equals(current)) {
                String builder = "`uuid` = \"" + results.getString("uuid") +
                        "\" AND `type` = \"" + results.getString("type") +
                        "\" AND `punisher_uuid` = \"" + results.getString("punisher_uuid") +
                        "\" AND `reason` = \"" + results.getString("reason") +
                        "\" AND `active` = " + 1;
                SQLUtil.update(StaffCore.getDB(), "punishments", "active", 0, new SQLUtil.Where(builder));
            } else if (date.before(current)) {
                switch (results.getString("type")) {
                    case "mute":
                        mutes.add(new Punishment(UUID.fromString(results.getString("uuid")),
                                results.getDate("date"),
                                results.getDate("expiration"),
                                UUID.fromString(results.getString("punisher_uuid")),
                                Reason.reasonList.stream().filter(reason -> {
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
                                Reason.reasonList.stream().filter(reason -> {
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

        bans.forEach(pun -> {
            if (Bukkit.getOfflinePlayer(pun.getUuid()).isOnline()) { Bukkit.getOfflinePlayer(UUID.fromString(pun.getUuid().toString())).getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYou have been banned from Warvale Network.\n&f" +
                    pun.getReason().getReason() + "\n\n" + "&8&lExpires: &r&7" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(pun.getExpires()))); }
        });
    }

    private static boolean equals(String str, String str1) {
        return str.equals(str1);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerLoginEvent event) throws SQLException, ClassNotFoundException {
        updatePunishments();
        if (bans.stream().anyMatch(punishment -> Bukkit.getOfflinePlayer(punishment.getUuid()).getPlayer().equals(event.getPlayer()))) {
            Punishment pun = bans.stream().filter(punishment -> punishment.getUuid().equals(event.getPlayer().getUniqueId())).findFirst().get();
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                    ChatColor.translateAlternateColorCodes('&', "&cYou have been banned from Warvale Network.\n&f" +
                    pun.getReason().getReason() + "\n\n" + "&8&lExpires: &r&7" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(pun.getExpires())));
        }
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) throws SQLException, ClassNotFoundException {
        updatePunishments();
        if (mutes.stream().anyMatch(punishment -> Bukkit.getOfflinePlayer(punishment.getUuid()).getPlayer().equals(event.getPlayer()))) {
            Punishment pun = bans.stream().filter(punishment -> punishment.getUuid().equals(event.getPlayer().getUniqueId())).findFirst().get();
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have been muted until &e" +
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(pun.getExpires()) + " &cby &e" + Bukkit.getOfflinePlayer(pun.getStaff()).getName() + " &cfor &e" +
            pun.getReason().getReason()));
        }
    }
}
