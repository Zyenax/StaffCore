package net.warvale.staffcore.users;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.permissions.Privilege;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // A list of all currently online users
    private static List<User> users = new ArrayList<>();

    public static List<User> getUsers() {
        return users;
    }

    public static User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    // Matches an online user to UUID
    // If none is found, searches DataStore
    public static User getUser(UUID uuid) {
        for (User user : getUsers()) {
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        return getSQLUser(uuid);
    }

    public static User getUser(String name) {
        System.out.println("Trying to get a user from the users arraylist");
        for (User user : getUsers()) {
            if (user.getName().equals(name)) {
                System.out.println("Found user.");
                return user;
            }
        }
        return getSQLUser(name);
    }


    public static User getSQLUser(UUID uuid) {
        try {
            PreparedStatement stmt = StaffCore.getDB().getConnection().prepareStatement("SELECT * FROM users WHERE uuid = ? LIMIT 1;");
            stmt.setString(1, uuid.toString());
            ResultSet set = stmt.executeQuery();
            if (set.next()) {

                String name = set.getString("name");
                String json = set.getString("data");

                JSONParser parser = new JSONParser();

                Object obj = parser.parse(json);
                JSONObject data = (JSONObject) obj;

                String prefix = (String) data.get("prefix");
                String suffix = (String) data.get("suffix");
                boolean superUser = (boolean) data.get("super");

                JSONArray privileges = (JSONArray) data.get("privileges");
                JSONArray ranks = (JSONArray) data.get("ranks");

                User user = new User(uuid);

                if (user.getPlayer() == null) {
                    user.setName(name);
                } else {
                    user.setName(user.getPlayer().getName());
                }

                user.setMetaPrefix(prefix);
                user.setMetaSuffix(suffix);
                user.setSuperUser(superUser);

                for (Object priv : privileges) {
                    user.getPrivileges().add(new Privilege(((String) priv).split(":")));
                }

                for (Object r : ranks) {
                    Rank rank = RankManager.getRank((String) r);
                    if (rank != null) {
                        user.getRanks().add(rank);
                    }
                }

                return user;

            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getSQLUser(String name) {
        System.out.println("Getting user from the database with the name " + name);
        try {
            PreparedStatement stmt = StaffCore.getDB().getConnection().prepareStatement("SELECT * FROM users WHERE name = ? LIMIT 1;");
            stmt.setString(1, name);
            ResultSet set = stmt.executeQuery();
            if (set.next()) {

                System.out.println("A user with the name " + name + " exists in the database, grabbing data now.");

                UUID uuid = UUID.fromString(set.getString("uuid"));
                String json = set.getString("data");

                JSONParser parser = new JSONParser();

                Object obj = parser.parse(json);
                JSONObject data = (JSONObject) obj;

                String prefix = (String) data.get("prefix");
                String suffix = (String) data.get("suffix");
                boolean superUser = (boolean) data.get("super");

                JSONArray privileges = (JSONArray) data.get("privileges");
                JSONArray ranks = (JSONArray) data.get("ranks");

                User user = new User(uuid);

                if (user.getPlayer() == null) {
                    user.setName(name);
                } else {
                    user.setName(user.getPlayer().getName());
                }

                user.setMetaPrefix(prefix);
                user.setMetaSuffix(suffix);
                user.setSuperUser(superUser);

                for (Object priv : privileges) {
                    user.getPrivileges().add(new Privilege(((String) priv).split(":")));
                }

                for (Object r : ranks) {
                    Rank rank = RankManager.getRank((String) r);
                    if (rank != null) {
                        user.getRanks().add(rank);
                    }
                }

                return user;

            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveUser(User user) {

        JSONObject obj = new JSONObject();
        obj.put("prefix", user.getMetaPrefix());
        obj.put("suffix", user.getMetaSuffix());
        obj.put("super", user.isSuperUser());

        JSONArray privileges = user.getPrivileges().stream().map(Privilege::toString).collect(Collectors.toCollection(JSONArray::new));

        obj.put("privileges", privileges);

        JSONArray ranks = new JSONArray();
        for (Rank rank : user.getRanks()) {
            if (rank != null) {
                ranks.add(rank.getName());
            }
        }

        obj.put("ranks", ranks);

        String json = obj.toJSONString();

        try {
            PreparedStatement stmt = StaffCore.getDB().getConnection().prepareStatement("SELECT * FROM users WHERE uuid = ? LIMIT 1;");
            stmt.setString(1, user.getUuid().toString());
            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                stmt.close();
                stmt = StaffCore.getDB().getConnection().prepareStatement("UPDATE users SET name = ?, data = ? WHERE uuid = ?;");
                stmt.setString(1, user.getName());
                stmt.setString(2, json);
                stmt.setString(3, user.getUuid().toString());
                stmt.execute();
                stmt.close();
            } else {
                stmt.close();
                stmt = StaffCore.getDB().getConnection().prepareStatement("INSERT INTO users (uuid, name, data) VALUES (?, ?, ?);");
                stmt.setString(1, user.getUuid().toString());
                stmt.setString(2, user.getName());
                stmt.setString(3, json);
                stmt.execute();
                stmt.close();
            }
            set.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
