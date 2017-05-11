package me.warvale.staffcore.rank;

import me.warvale.staffcore.StaffCore;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Draem on 5/10/2017.
 */
public class RankManager {

    public static String filedir = "\\ranks\\";
    public static String filename = "ranks.json";
    public static String path = getPath();
    public static File rankfile;

    private static List<Rank> ranks = new ArrayList<>();

    public static void addRank(Rank rank) throws IOException, ParseException {
        File rankFile = new File(path + filedir + "\\");

        if (!rankFile.exists()) {
            if (rankFile.mkdirs()) {
                rankfile = new File(rankFile.getPath() + "\\" + filename);
                rankfile.createNewFile();
            }
        }

        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(rankfile));

        JSONArray ranks = (JSONArray) json.get("ranks");

        JSONObject newrank = new JSONObject();
        newrank.put("id", rank.getId());
        newrank.put("name", rank.getName());
        newrank.put("prefix", rank.getPrefix());
        newrank.put("namecolor", rank.getNamecolor());
        newrank.put("staff", rank.isStaff());
        newrank.put("parents", rank.getParents());
        newrank.put("members", rank.getMembers());
        newrank.put("permissions", rank.getPermissions());

        ranks.add(newrank);
    }

    public static void updateRank(Rank rank) {
        File rankFile = new File(path + filedir + "\\");

        if (!rankFile.exists()) {
            if (rankFile.mkdirs()) {
                rankfile = new File(rankFile.getPath() + "\\" + filename);
                try {
                    rankfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        JSONObject json = null;
        try {
            json = (JSONObject) new JSONParser().parse(new FileReader(rankfile));
        } catch (IOException | ParseException e) {
            StaffCore.get().getLogger().log(Level.WARNING, "Unable to update rank \"" + rank.getName() + "\"!");
        }

        assert json != null;
        JSONArray ranks = (JSONArray) json.get("ranks");
        ranks.forEach(o -> {
            JSONObject json_o = (JSONObject) o;
            if (json_o.get("id").equals(rank.getId())) {
                json_o.forEach((o1, o2) -> {
                    String key = (String) o1;

                    json_o.replace(o1, o2, rank.get(key));
                });
            }
        });
    }


    public static String getPath() {
        URL url = RankManager.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = null;
        try {
            jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert jarPath != null;
        return new File(jarPath).getParentFile().getPath();
    }

    public static Rank getRankForUser(Player player) {
        for (Rank rank : ranks) {
            if (rank.getMembers().contains(player)) {
                return rank;
            }
        }
        return null;
    }



}
