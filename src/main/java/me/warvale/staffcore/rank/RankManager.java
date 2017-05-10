package me.warvale.staffcore.rank;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by Draem on 5/10/2017.
 */
public class RankManager {

    public static String filedir = "\\ranks\\";
    public static String filename = "ranks.json";
    public static String path = getPath();
    public static File rankfile;

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
    }

    public static String getPath() {
        URL url = RankManager.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = null;
        try {
            jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String parentPath = new File(jarPath).getParentFile().getPath();
        return parentPath;
    }



}
