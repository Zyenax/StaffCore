package net.warvale.staffcore.users;

import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import net.warvale.staffcore.permissions.Privilege;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class User {

    // A user's unique mojang ID
    private UUID uuid;

    // The last known username of a player
    // If it was changed, it'll be updated next time they login
    private String name;

    //the player
    private Player player;

    // The list of user defined permissions
    private List<Privilege> privileges = new ArrayList<>();

    // The list of ranks a user belong to
    private List<Rank> ranks = new ArrayList<>();

    // Super Users have full access to plugin controls
    // A user can be defined as a super user via console
    private boolean superUser = false;

    // A user's prefix and suffix, to be used by other plugins
    // User-defined prefix/suffixes will override group defined ones.
    private String metaPrefix = null;
    private String metaSuffix = null;

    // Bukkit's permission attachment
    private PermissionAttachment attachment = null;

    public User(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
    }

    // Returns the user's prefix, respecting group priority
    // Returns an empty string if there is none
    public String getPrefix() {
        if (getMetaPrefix() != null) {
            return getMetaPrefix();
        }
        List<Rank> ranks = getRanks();
        Collections.sort(ranks);
        if (ranks.size() > 0) {
            Rank rank = ranks.get(ranks.size() - 1);
            if (rank.getMetaPrefix() != null) {
                return rank.getMetaPrefix();
            }
        }
        return "";
    }

    // Returns the user's suffix, respecting group priority
    // Returns an empty string if there is none
    public String getSuffix() {
        if (getMetaSuffix() != null) {
            return getMetaSuffix();
        }
        List<Rank> ranks = getRanks();
        Collections.sort(ranks);
        if (ranks.size() > 0) {
            Rank rank = ranks.get(ranks.size() - 1);
            if (rank.getMetaSuffix() != null) {
                return rank.getMetaSuffix();
            }
        }
        return "";
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

    // Denied all currently defined permissions by the attachment, then recalculates
    public void refreshPermissions(World world) {
        for (String p : getAttachment().getPermissions().keySet()) {
            getAttachment().setPermission(p, false);
        }
        for (Privilege privilege : getAllPrivileges(world)) {
            if (world == null || privilege.isSupportingWorld(world)) {
                getAttachment().setPermission(privilege.getNode(), !privilege.isNegated());
            }
        }
    }

    public PermissionAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(PermissionAttachment attachment) {
        this.attachment = attachment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    // Matches a permission node to a user-defiend privilege
    // Returns null if there isn't one defined in this scope
    public Privilege getPrivilege(String node) {
        for (Privilege privilege : getPrivileges()) {
            if (privilege.getNode().equalsIgnoreCase(node)) {
                return privilege;
            }
        }
        return null;
    }

    // Returns a list of ALL user permissions, including groups and group inherited permissions, respecting priority
    // If a world is supplied, only permissions supporting the world will be returned
    // If a world is null, all permissions will be returned (assuming they support the current server)
    public List<Privilege> getAllPrivileges(World world) {
        List<Privilege> privileges = new ArrayList<>();
        List<Rank> ranks = getRanks();
        Collections.sort(ranks);
        for (Rank rank : ranks) {
            if (world != null && !rank.isSupportingWorld(world))
                continue;
            ploop:
            for (Privilege privilege : rank.getAllPrivileges(world)) {
                for (Privilege p : privileges) {
                    if (p.matches(privilege)) {
                        p.setNegated(privilege.isNegated());
                        continue ploop;
                    }
                }
                privileges.add(privilege.copy());
            }
        }
        ploop:
        for (Privilege privilege : getPrivileges()) {
            if (world != null && !privilege.isSupportingWorld(world))
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

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public boolean hasPermission(String permission) {

        Privilege privilege = getPrivilege(permission);

        return privilege != null;
    }

    // Gets all ranks for this user that support the provided world
    public List<Rank> getRanks(World world) {
        List<Rank> ranks = new ArrayList<>();
        ranks.addAll(getRanks());

        for (int i = 0; i < ranks.size(); i++) {
            Rank rank = ranks.get(i);

            if (RankManager.getDefaultRank() != null && RankManager.getDefaultRank().getName().equals(rank.getName())) {
                continue;
            }

            if (world != null) {
                if (!rank.isSupportingWorld(world)) {
                    ranks.remove(rank);
                }
            }

        }

        return ranks;
    }

    // Returns a list of user groups, ensuring that the default group is always there.
    public List<Rank> getRanks() {
        if (RankManager.getDefaultRank() != null && !ranks.contains(RankManager.getDefaultRank())) {
            ranks.add(RankManager.getDefaultRank());
        }
        return ranks;
    }

    public boolean isSuperUser() {
        return superUser;
    }

}
