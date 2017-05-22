package net.warvale.staffcore.rank;

import com.google.gson.Gson;
import net.warvale.staffcore.StaffCore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Draem on 5/10/2017.
 */
public class RankManager {

    public static String filename = "ranks.json";
    public static File rankfile;

    private static List<Rank> ranks = new ArrayList<>();

    public static void prep() throws IOException, ParseException {
        ranks.clear();
        File tempfile = StaffCore.get().getDataFolder();

        boolean fileexist = true;
        boolean successfuly = false;
        boolean createdfile = false;

        if (!tempfile.exists()) {
            fileexist = false;
            if (tempfile.mkdirs()) {
                successfuly = true;
                try {
                    rankfile = new File(tempfile.getPath() + "/" + filename);
                    if (rankfile.createNewFile()) { createdfile = true; }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            rankfile = new File(tempfile.getPath() + "/" + filename);
        }

        for (Object obj : (JSONArray) new JSONParser().parse(new FileReader(rankfile))) {
            JSONObject jj = (JSONObject) obj;
            //public Rank(String name, String prefix, boolean staff, List<Rank> parents, List<Permission> permissions, String namecolor)


            new Rank(((String) jj.get("name")).replaceAll("\\u0026", "&"),
                    ((String) jj.get("prefix")).replaceAll("\\u0026", "&"),
                    (boolean) jj.get("staff"),
                    (String) jj.get("parents"),
                    convert((JSONArray) jj.get("permissions")),
                    (String) jj.get("namecolor"),
                    false,
                    convert((JSONArray) jj.get("members")));
        }
    }

    public static void addRank(Rank rank) throws IOException, ParseException {
        JSONArray json = (JSONArray) new JSONParser().parse(new FileReader(rankfile));

        JSONObject newrank = new JSONObject();
        newrank.put("id", rank.getId().replaceAll("\\u0026", "&"));
        newrank.put("name", rank.getName().replaceAll("\\u0026", "&"));
        newrank.put("prefix", rank.getPrefix().replaceAll("\\u0026", "&"));
        newrank.put("namecolor", rank.getNamecolor().replaceAll("\\u0026", "&"));
        newrank.put("staff", rank.isStaff());
        newrank.put("parents", rank.getParents().replaceAll("\\u0026", "&"));
        newrank.put("members", new Gson().toJson(rank.getMembers()).replaceAll("\\\\", "").replaceAll("\\u0026", "&"));
        newrank.put("permissions", new Gson().toJson(rank.getPermissions()).replaceAll("\\\\", "").replaceAll("\\u0026", "&"));

        json.add(newrank);


        try (FileWriter file = new FileWriter(rankfile)) {
            file.write(newrank.toJSONString());
        }

        ranks.add(rank);
    }

    public static void updateRank(Rank rank) {

        JSONArray json = null;
        try {
            json = (JSONArray) new JSONParser().parse(new FileReader(rankfile));
        } catch (IOException | ParseException e) {
            StaffCore.get().getLogger().log(Level.WARNING, "Unable to update rank \"" + rank.getName() + "\"!");
        }

        assert json != null;
        JSONArray finalJson = json;
        json.forEach(o -> {
            JSONObject json_o = (JSONObject) o;
            if (json_o.get("id").equals(rank.getId())) {
                ((JSONObject)(finalJson.get(finalJson.indexOf(json_o)))).forEach((o1, o2) -> {
                    String key = (String) o1;

                    ((JSONObject)(finalJson.get(finalJson.indexOf(json_o)))).replace(o1, o2, rank.get(key));
                });
            }
        });

        String obj = new Gson().toJson(json.toArray());

        try {
            try (FileWriter file = new FileWriter(rankfile)) {
                file.write(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Rank getRankForUser(String name) {
        for (Rank rank : ranks) {
            if (rank.getMembers().contains(name)) {
                return rank;
            }
        }
        return null;
    }

    public static List<Rank> getRanks() {
        return ranks;
    }

    public static File getRankFile() {
        return rankfile;
    }

    public static Rank getRankByName(String name) {
        for (Rank rank : getRanks()) {
            if (rank.getName().equalsIgnoreCase(name) || rank.getId().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    public static List<String> convert(JSONArray json) {
        List<String> list = new ArrayList<>();
        if (json != null) {
            for (Object aJson : json) {
                list.add((String) aJson);
            }
        }
        return list;
    }



}
