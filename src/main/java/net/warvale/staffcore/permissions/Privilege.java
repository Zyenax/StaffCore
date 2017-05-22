package net.warvale.staffcore.permissions;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Privilege {

    // The permission node
    private String node;

    // Whether or not the permission should be negated
    private boolean negated = false;

    // A list of worlds that this permission is valid in
    // An empty world list has no restrictions
    private List<World> worlds = new ArrayList<>();

    public Privilege(String node) {
        this.node = node;
    }

    public Privilege(String node, boolean negated) {
        this.node = node;
        this.negated = negated;
    }

    // Builds a privilege from string blocks
    public Privilege(String[] data) {
        String node = data[0];
        boolean negated = Boolean.valueOf(data[1]);
        String worlds = data[2];
        if (!worlds.equals("[undefined]")) {
            for (String world : worlds.split(",")) {
                World w = Bukkit.getWorld(world);
                if (w != null) {
                    getWorlds().add(w);
                }
            }
        }
        this.node = node;
        this.negated = negated;
    }

    // Copies privilege data into a new node
    public Privilege copy() {

        Privilege privilege = new Privilege(getNode(), isNegated());
        privilege.getWorlds().addAll(getWorlds());

        return privilege;

    }

    // Converts the privilege to an easy to store string
    @Override
    public String toString() {

        String val = "";

        val += getNode().replace(":", "");
        val += ":" + isNegated();
        val += ":";

        if (getWorlds().size() > 0) {
            String worlds = "";
            for (World world : getWorlds()) {
                worlds += "," + world.getName();
            }
            worlds = worlds.substring(1);
            val += worlds;
        } else {
            val += "[undefined]";
        }
        val += ":";


        return val;

    }

    public boolean matches(Privilege privilege) {
        return privilege.getNode().equals(getNode());
    }

    public boolean isConditional() {
        return getWorlds().size() > 0;
    }

    public void setNegated(boolean negated) {
        this.negated = negated;
    }

    // Returns true if the permission supports the provided world, or the world list is empty
    public boolean isSupportingWorld(World world) {
        if (getWorlds().size() == 0) return true;
        for (World w : getWorlds()) {
            if (world.getName().equalsIgnoreCase(w.getName())) {
                return true;
            }
        }
        return false;
    }

    public List<World> getWorlds() {
        return worlds;
    }

    public String getNode() {
        return node;
    }

    public boolean isNegated() {
        return negated;
    }

}
