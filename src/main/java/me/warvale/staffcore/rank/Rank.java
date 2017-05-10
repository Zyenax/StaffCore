package me.warvale.staffcore.rank;

import java.security.Permission;
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

    public Rank(String name, String prefix, boolean staff, List<Rank> parents, List<Permission> permissions) {
        this.id = simplify(name);
        this.name = name;
        this.prefix = prefix;
        this.parents = parents;
        this.permissions = permissions;
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

    public static String simplify(String str1) {
        return str1.toLowerCase().replaceAll(" ", "-")
                .replaceAll("moderator", "mod")
                .replaceAll("senior", "sr")
                .replaceAll("junior", "jr")
                .replaceAll("administrator", "admin");
    }

}
