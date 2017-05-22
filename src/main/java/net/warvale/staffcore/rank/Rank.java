package net.warvale.staffcore.rank;

import net.warvale.staffcore.permissions.Privilege;
import net.warvale.staffcore.users.User;
import net.warvale.staffcore.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class Rank implements Comparable {

    // The unique name of the rank
    // Used for reference purposes and data purposes
    private String name;

    // The priority of the rank
    // The lowest priority group is the "default" group
    // Higher priority groups will override the permission decisions of lower priority ones
    private int priority;

    // A list of defined permissions for this group
    private List<Privilege> privileges = new ArrayList<>();

    // A list of ranks to inherit permissions from
    private List<Rank> inheritance = new ArrayList<>();

    // A list of worlds where this group is valid.
    // If this list is empty, there are no world restrictions
    private List<World> worlds = new ArrayList<>();

    // The prefix and suffix to be used by other plugins
    private String metaPrefix = null;
    private String metaSuffix = null;

    public Rank(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public boolean isDefault() {
        return RankManager.getDefaultRank() != null && RankManager.getDefaultRank().getName().equals(getName());
    }

    // Allows easily ordering groups by priority
    @Override
    public int compareTo(Object o) {

        if (o != null && (o instanceof Rank)) {
            Rank rank = (Rank) o;
            if (rank.getPriority() > getPriority()) {
                return -1;
            } else if (rank.getPriority() < getPriority()) {
                return 1;
            }
        }

        return 0;
    }

    // Safely deletes a group
    public void delete() {
        // Edits the cache to remove this rank as an inheritance for any that have it
        RankManager.getRanks().stream().filter(r -> r.getInheritance().contains(this)).forEach(r -> {
            r.getInheritance().remove(this);
            RankManager.saveRank(r);
        });

        // Removes the rank from any online users that have it.
        // Offline users will be updated next time they connect
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = UserManager.getUser(player);
            if (user.getRanks().contains(this)) {
                user.getRanks().remove(this);
                UserManager.saveUser(user);
            }
        }
    }

    // Returns all privileges for a rank, including privileges of groups it inherits
    // If a world is provided, it will only return privileges supporting that world
    // If a world is null, then all privileges will be returned (assuming they support the server)
    public List<Privilege> getAllPrivileges(World world) {
        List<Privilege> privileges = new ArrayList<>();
        privileges.addAll(getInheritedPrivileges(world).keySet());
        ploop:
        for (Privilege privilege : getPrivileges()) {
            if ((world != null && !privilege.isSupportingWorld(world)))
                continue;
            for (Privilege p : privileges) {
                if (p.matches(privilege)) {
                    p.setNegated(privilege.isNegated());
                    continue ploop;
                }
            }
            privileges.add(privilege.copy());
        }
        return privileges;
    }

    // Returns a map of permissions for inherited ranks, and the rank the permission is from
    // If a world is provided, only permissions that support that world will be returned
    // If a world is null, then all permissions will be returned (assuming they support the server)
    public Map<Privilege, Rank> getInheritedPrivileges(World world) {
        Map<Privilege, Rank> privileges = new HashMap<>();

        List<Rank> ranks = new ArrayList<>();
        processInheritanceTree(world, ranks, this);

        Collections.sort(ranks);

        for (Rank rank : ranks) {
            if ((world != null && !rank.isSupportingWorld(world)))
                continue;
            for (Privilege privilege : rank.getPrivileges()) {
                for (Privilege p : privileges.keySet()) {
                    if (p.matches(privilege)) {
                        privilege.setNegated(p.isNegated());
                    }
                }
                privileges.put(privilege.copy(), rank);
            }
        }

        return privileges;
    }

    // Gets a list of every single rank to take inheritance from
    // If a world is supplied, only ranks which support the world will be considered
    // If a world is null, all groups will be returned (assuming they support the server)
    private void processInheritanceTree(World world, List<Rank> ranks, Rank rank) {
        for (Rank i : rank.getInheritance()) {
            if ((world != null && !i.isSupportingWorld(world)))
                continue;
            ranks.add(i);
            if (i.getInheritance().size() > 0) {
                processInheritanceTree(world, ranks, i);
            }
        }
    }

    public String getMetaPrefix() {
        return metaPrefix;
    }

    public void setMetaPrefix(String metaPrefix) {
        this.metaPrefix = metaPrefix;
    }

    public String getMetaSuffix() {
        return metaSuffix;
    }

    public void setMetaSuffix(String metaSuffix) {
        this.metaSuffix = metaSuffix;
    }

    // Returns true if there are world or server restrictions
    public boolean isConditional() {
        return getWorlds().size() > 0;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // Returns true if the world list is empty, or contains the provided world
    public boolean isSupportingWorld(World world) {
        if (getWorlds().size() == 0) return true;
        for (World w : getWorlds()) {
            if (world.getName().equalsIgnoreCase(w.getName())) {
                return true;
            }
        }
        return false;
    }

    // Returns the PRIVILEGE if a permission node is defined
    // Returns null if it isn't
    public Privilege getPrivilege(String node) {
        for (Privilege privilege : getPrivileges()) {
            if (privilege.getNode().equalsIgnoreCase(node)) {
                return privilege;
            }
        }
        return null;
    }

    public boolean hasPermission(String permission) {

        Privilege privilege = getPrivilege(permission);

        return privilege != null;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public List<Rank> getInheritance() {
        return inheritance;
    }

    public List<World> getWorlds() {
        return worlds;
    }

}
