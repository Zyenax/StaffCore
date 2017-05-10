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

    private List<Player> players;

    public Rank(String name, String prefix, boolean staff, List<Rank> parents, List<Permission> permissions) throws IOException, ParseException {
        this.id = simplify(name);
        this.name = name;
        this.prefix = prefix;
        this.parents = parents;
        this.permissions = permissions;
        this.players = new ArrayList<>();


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public List<Rank> getParents() {
        return parents;
    }

    public void setParents(List<Rank> parents) {
        this.parents = parents;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addMember(Player player) {
        this.players.add(player);
    }

    public List<Player> getMembers() {
        return this.players;
    }

    public static String simplify(String str1) {
        return str1.toLowerCase().replaceAll(" ", "-")
                .replaceAll("moderator", "mod")
                .replaceAll("senior", "sr")
                .replaceAll("junior", "jr")
                .replaceAll("administrator", "admin");
    }

}
