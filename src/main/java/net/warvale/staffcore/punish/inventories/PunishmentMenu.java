package net.warvale.staffcore.punish.inventories;

import net.warvale.staffcore.punish.PunishmentManager;
import net.warvale.staffcore.punish.data.Punishment;
import net.warvale.staffcore.punish.data.PunishmentType;
import net.warvale.staffcore.punish.data.Reason;
import net.warvale.staffcore.punish.data.Severity;
import net.warvale.staffcore.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.inventivetalent.menubuilder.inventory.InventoryMenuBuilder;

import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

/**
 * Created by Draem on 5/24/2017.
 */
public class PunishmentMenu {

    private static ItemStack sevpane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
    private static ItemStack respane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);

    private static List<Integer> sevslots = Arrays.asList(0, 1, 2, 9, 11, 18, 20, 27, 29, 36, 38, 45, 46, 47);
    private static List<Integer> resslots = Arrays.asList(3, 4, 5, 6, 7, 8, 12, 17, 21, 26, 30, 35, 39, 44, 48, 49, 50, 51, 52, 53);
    private static List<Integer> reasonslots = Arrays.asList(13, 14, 15, 16, 22, 23, 24, 25, 31, 32, 33, 34, 40, 41, 42, 43);
    private static List<Integer> severitySlots = Arrays.asList(10, 19, 28, 37);

    public static List<PunishmentMenu> menus = new ArrayList<>();


    static {
        ItemMeta sevmeta = sevpane.getItemMeta();
        ItemMeta resmeta = sevpane.getItemMeta();
        sevmeta.setDisplayName(" ");
        resmeta.setDisplayName(" ");
        sevpane.setItemMeta(sevmeta);
        respane.setItemMeta(sevmeta);
    }

    private Inventory inventory;
    private InventoryMenuBuilder builder;
    private PunishmentType type;
    private Severity severity;

    public PunishmentMenu(PunishmentType type, Severity severity) {
        this.type = type;
        this.severity = severity;

        this.builder = new InventoryMenuBuilder(54);
        resslots.forEach(integer -> builder.withItem(integer, respane));
        sevslots.forEach(integer -> builder.withItem(integer, sevpane));
        this.inventory = null;

        Integer index = 0;
        List<Reason> reasons = Reason.getReasonsByParams(this.type, this.severity);
        reasons.forEach(reason -> System.out.println(this.type.toString() + " ::: registered reason ::: " + reason.getReason()));
        for (Reason reason : reasons) {
            ItemStack reasonItem = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = reasonItem.getItemMeta();
            meta.setDisplayName(ChatUtils.aqua + reason.getReason());
            meta.setLore(Arrays.asList(reason.getDescription().split("(?<=\\\\G............)")));
            reasonItem.setItemMeta(meta);
            builder.withItem(reasonslots.get(index), reasonItem, (player, clickType, itemStack) -> {
                if (reason.getSev().equals(Severity.SEVERITY_1) && reason.getType().equals(PunishmentType.MUTE)) {
                    if (PunishmentManager.pastMute(Bukkit.getPlayer(PunishmentManager.punishing.get(player.getUniqueId())), this.type)) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.YEAR, 100);
                        PunishmentManager.registerPunishment(new Punishment(PunishmentManager.punishing.get(player.getUniqueId()), Date.from(Instant.now()), cal.getTime(), player.getUniqueId(), reason));
                    } else {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.HOUR_OF_DAY, 1);
                        PunishmentManager.registerPunishment(new Punishment(PunishmentManager.punishing.get(player.getUniqueId()), Date.from(Instant.now()), cal.getTime(), player.getUniqueId(), reason));
                    }
                } else if (reason.getType().equals(PunishmentType.MUTE) && reason.getSev().equals(Severity.PERMANENT)) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.YEAR, 100);
                    PunishmentManager.registerPunishment(new Punishment(PunishmentManager.punishing.get(player.getUniqueId()), Date.from(Instant.now()), cal.getTime(), player.getUniqueId(), reason));
                } else if (reason.getType().equals(PunishmentType.BAN) && reason.getSev().equals(Severity.SEVERITY_1)) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    PunishmentManager.registerPunishment(new Punishment(PunishmentManager.punishing.get(player.getUniqueId()), Date.from(Instant.now()), cal.getTime(), player.getUniqueId(), reason));
                } else if (reason.getType().equals(PunishmentType.BAN) && reason.getSev().equals(Severity.PERMANENT)) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.YEAR, 100);
                    PunishmentManager.registerPunishment(new Punishment(PunishmentManager.punishing.get(player.getUniqueId()), Date.from(Instant.now()), cal.getTime(), player.getUniqueId(), reason));
                } else if (reason.getType().equals(PunishmentType.WARN) && reason.getSev().equals(Severity.SECOND)) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.SECOND, 1);
                    PunishmentManager.registerPunishment(new Punishment(PunishmentManager.punishing.get(player.getUniqueId()), Date.from(Instant.now()), cal.getTime(), player.getUniqueId(), reason));
                }
                try {
                    PunishmentManager.updatePunishments();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }, ClickType.LEFT);
            index++;
        }

        Integer index1 = 0;
        List<Severity> severities = this.type.getSeverities();
        assert severities != null;
        for (Severity sev : severities) {
            ItemStack severityItem = new ItemStack(Material.PAPER);
            ItemMeta meta = severityItem.getItemMeta();
            meta.setDisplayName(ChatUtils.yellow + sev.toString());
            severityItem.setItemMeta(meta);

            builder.withItem(severitySlots.get(index1), severityItem, (player, clickType, itemStack) -> {
                player.closeInventory();
                player.openInventory(getInventory(this.type, sev));
            }, ClickType.LEFT);
            index1++;
        }

        menus.add(this);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryMenuBuilder getBuilder() {
        return this.builder;
    }

    public void setBuilder(InventoryMenuBuilder builder) {
        this.builder = builder;
    }

    public PunishmentType getType() {
        return this.type;
    }

    public void setType(PunishmentType type) {
        this.type = type;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    private static Inventory getInventory(PunishmentType type, Severity severity) {
        return menus.stream().filter(punishmentMenu -> punishmentMenu.getSeverity().equals(severity) && punishmentMenu.getType().equals(type)).findFirst().get().getBuilder().build();
    }
}
