package net.warvale.staffcore.punish;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.punish.data.Punishment;
import net.warvale.staffcore.utils.sql.SQLConnection;
import net.warvale.staffcore.utils.sql.SQLUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Draem on 5/21/2017.
 */
public class PunishmentManager {

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
        builder.append("`uuid` = " + punishment.getUuid().toString());
        builder.append(" AND `type` = " + punishment.getType().toString());
        builder.append(" AND `punisher_uuid` = " + punishment.getStaff().toString());
        builder.append(" AND `reason` = " + punishment.getReason().toString());
        builder.append(" AND `active` = " + String.valueOf(true));

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

}
