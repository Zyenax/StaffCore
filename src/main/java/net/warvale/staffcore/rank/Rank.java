package net.warvale.staffcore.rank;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.io.IOException;
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
    private String parent;
    private List<String> permissions;
    private String namecolor;

    private List<String> players;

    public Rank(String name, String prefix, boolean staff, String parent, List<String> permissions, String namecolor, boolean add) throws IOException, ParseException {
        if (RankManager.getRankByName(name) != null) {
            return;
        }

        this.id = simplify(name);
        this.name = name;
        this.staff = staff;
        this.prefix = prefix;
        this.parent = parent;
        this.permissions = permissions;
        this.players = new ArrayList<>();
        this.namecolor = namecolor;

        if (add) RankManager.addRank(this);
    }

    public Rank(String name, String prefix, boolean staff, String parent, List<String> permissions, String namecolor, boolean add, List<String> members) throws IOException, ParseException {
        if (RankManager.getRankByName(name) != null) {
            return;
        }

        this.id = simplify(name);
        this.name = name;
        this.staff = staff;
        this.prefix = prefix;
        this.parent = parent;
        this.permissions = permissions;
        this.players = members;
        this.namecolor = namecolor;

        if (add) RankManager.addRank(this); else RankManager.getRanks().add(this);
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

    public String getParents() {
        return this.parent;
    }

    public void setParents(List<Rank> parents) {
        this.parent = parent;
        RankManager.updateRank(this);
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
        RankManager.updateRank(this);
    }

    public void addMember(Player player) {
        this.players.add(player.getName());
        RankManager.updateRank(this);
    }

    public void removeMember(Player player) {
        this.players.remove(player.getName());
        RankManager.updateRank(this);
    }

    public String getNamecolor() {
        return this.namecolor;
    }

    public void setNamecolor(String namecolor) {
        this.namecolor = namecolor;
        RankManager.updateRank(this);
    }

    public List<String> getMembers() {
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
                return new Gson().toJson(this.permissions);
            case "members":
                return new Gson().toJson(this.players);
            case "parents":
                return this.parent;
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
