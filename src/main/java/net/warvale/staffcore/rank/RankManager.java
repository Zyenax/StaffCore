package net.warvale.staffcore.rank;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.permissions.Privilege;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RankManager {

    // A list of all ranks
    private static List<Rank> ranks = new ArrayList<>();

    // Returns the default rank, defined by the group with the lowest priority
    public static Rank getDefaultRank() {
        Rank rank = null;
        for (Rank r : getRanks()) {
            if (rank == null || rank.getPriority() > r.getPriority()) {
                rank = r;
            }
        }
        return rank;
    }

    // If world is not null
    public static List<Rank> getRanks(World world) {
        List<Rank> ranks = new ArrayList<>();
        for (Rank rank : getRanks()) {
            if (rank.isSupportingWorld(world)) {
                ranks.add(rank);
            }
        }
        return ranks;
    }

    // Returns a group that matches the provided name
    public static Rank getRank(String name) {
        name = name.replace("_", " ");
        for (Rank rank : getRanks()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    public static List<Rank> getRanks() {
        return ranks;
    }

    public static void saveRank(Rank rank) {

        try {

            if (!StaffCore.getDB().checkConnection()) {
                return;
            }

        } catch (Exception ex) {}

        JSONObject obj = new JSONObject();
        obj.put("priority", rank.getPriority());

        JSONArray privileges = rank.getPrivileges().stream().map(Privilege::toString).collect(Collectors.toCollection(JSONArray::new));
        JSONArray inherit = rank.getInheritance().stream().map(Rank::getName).collect(Collectors.toCollection(JSONArray::new));
        JSONArray worlds = rank.getWorlds().stream().map(World::getName).collect(Collectors.toCollection(JSONArray::new));

        obj.put("privileges", privileges);
        obj.put("inherit", inherit);
        obj.put("worlds", worlds);

        obj.put("prefix", rank.getMetaPrefix());
        obj.put("suffix", rank.getMetaSuffix());

        String json = obj.toJSONString();

        try {
            PreparedStatement stmt = StaffCore.getDB().getConnection().prepareStatement("SELECT * FROM ranks WHERE name = ? LIMIT 1;");
            stmt.setString(1, rank.getName());
            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                stmt.close();

                stmt = StaffCore.getDB().getConnection().prepareStatement("UPDATE ranks SET data = ? WHERE name = ?;");
                stmt.setString(1, json);
                stmt.setString(2, rank.getName());
                stmt.execute();
                stmt.close();

            } else {
                stmt.close();
                stmt = StaffCore.getDB().getConnection().prepareStatement("INSERT INTO ranks (name, data) VALUES (?, ?);");
                stmt.setString(1, rank.getName());
                stmt.setString(2, json);
                stmt.execute();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void loadRanks() {
        try {
            List<String> downloadedNames = new ArrayList<>();
            PreparedStatement stmt = StaffCore.getDB().getConnection().prepareStatement("SELECT name FROM ranks;");
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                String name = set.getString("name");
                downloadedNames.add(name);
                if (RankManager.getRank(name) == null) {
                    RankManager.getRanks().add(new Rank(name, 0));
                }
            }
            set.close();
            stmt.close();
            if (downloadedNames.size() > 0) {
                for (int i = 0; i < RankManager.getRanks().size(); i++) {
                    Rank rank = RankManager.getRanks().get(i);
                    if (!downloadedNames.contains(rank.getName())) {
                        rank.delete();
                        RankManager.getRanks().remove(rank); // Group was deleted
                    }
                }
            }
            for (Rank rank : RankManager.getRanks()) {
                stmt = StaffCore.getDB().getConnection().prepareStatement("SELECT * FROM ranks WHERE name = ? LIMIT 1;");
                stmt.setString(1, rank.getName());
                set = stmt.executeQuery();
                if (set.next()) {

                    String json = set.getString("data");
                    JSONParser parser = new JSONParser();

                    Object obj = parser.parse(json);
                    JSONObject data = (JSONObject) obj;

                    rank.setPriority(Integer.valueOf(data.get("priority").toString()));

                    rank.getPrivileges().clear();

                    for (Object p : (JSONArray) data.get("privileges")) {
                        rank.getPrivileges().add(new Privilege(((String) p).split(":")));
                    }

                    rank.getInheritance().clear();

                    for (Object i : (JSONArray) data.get("inherit")) {
                        Rank ih = RankManager.getRank((String) i);
                        if (ih != null) {
                            rank.getInheritance().add(ih);
                        }
                    }

                    rank.getWorlds().clear();

                    for (Object w : (JSONArray) data.get("worlds")) {
                        World world = Bukkit.getWorld((String) w);
                        if (world != null) {
                            rank.getWorlds().add(world);
                        }
                    }

                    rank.setMetaPrefix((String) data.get("prefix"));
                    rank.setMetaSuffix((String) data.get("suffix"));

                }
                set.close();
                stmt.close();
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

    }
}
