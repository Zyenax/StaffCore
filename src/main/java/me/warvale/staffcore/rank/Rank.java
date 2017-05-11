package me.warvale.staffcore.rank;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Draem on 5/10/2017.
 */
public class Rank {

    private String id;
    private String name;
    private String prefix;
    private boolean staff;
    private List<Rank> parents;
    private List<Permission> permissions;
    private String namecolor;

    private List<Player> players;

    public Rank(String name, String prefix, boolean staff, List<Rank> parents, List<Permission> permissions, String namecolor) throws IOException, ParseException {
        this.id = simplify(name);
        this.name = name;
        this.prefix = prefix;
        this.parents = parents;
        this.permissions = permissions;
        this.players = new ArrayList<>();
        this.namecolor = namecolor;

        RankManager.addRank(this);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
        RankManager.updateRank(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        RankManager.updateRank(this);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        RankManager.updateRank(this);
    }

    public boolean isStaff() {
        return this.staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
        RankManager.updateRank(this);
    }

    public List<Rank> getParents() {
        return this.parents;
    }

    public void setParents(List<Rank> parents) {
        this.parents = parents;
        RankManager.updateRank(this);
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
        RankManager.updateRank(this);
    }

    public void addMember(Player player) {
        this.players.add(player);
        RankManager.updateRank(this);
    }

    public String getNamecolor() {
        return this.namecolor;
    }

    public void setNamecolor(String namecolor) {
        this.namecolor = namecolor;
        RankManager.updateRank(this);
    }

    public List<Player> getMembers() {
        return this.players;
    }

    public Object get(String key) {
        switch (key) {
            case "id":
                return this.id;
            case "name":
                return this.name;
            case "prefix":
                return this.prefix;
            case "staff":
                return this.staff;
            case "namecolor":
                return this.namecolor;
            case "permissions":
                return this.permissions;
            case "members":
                return this.players;
            case "parents":
                return this.parents;
            default:
                return null;
        }
    }

    public static String simplify(String str1) {
        return str1.toLowerCase().replaceAll(" ", "-")
                .replaceAll("moderator", "mod")
                .replaceAll("senior", "sr")
                .replaceAll("junior", "jr")
                .replaceAll("administrator", "admin");
    }

}
