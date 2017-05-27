package net.warvale.staffcore.punish.inventories;

import net.warvale.staffcore.punish.PunishmentManager;
import net.warvale.staffcore.punish.data.Punishment;
import net.warvale.staffcore.punish.data.PunishmentType;
import net.warvale.staffcore.punish.data.Reason;
import net.warvale.staffcore.punish.data.Severity;
import net.warvale.staffcore.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.inventivetalent.menubuilder.inventory.InventoryMenuBuilder;

import java.time.Instant;
import java.util.*;

/**
 * Created by Draem on 5/24/2017.
 */
public class PunishmentMenu {

    private static ItemStack sevpane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
    private static ItemStack respane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);

    private static List<Integer> sevslots = Arrays.asList(1, 2, 3, 10, 12, 19, 21, 28, 30, 37, 39, 46, 47, 48);
    private static List<Integer> resslots = Arrays.asList(4, 5, 6, 7, 8, 9, 13, 18, 22, 27, 31, 36, 40, 45, 49, 50, 51, 52, 53, 54);
    private static List<Integer> reasonslots = Arrays.asList(14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44);
    private static List<Integer> severitySlots = Arrays.asList(11, 20, 29, 38);

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
            });
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
            });
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
        return menus.stream().filter(punishmentMenu -> punishmentMenu.getSeverity().equals(severity) && punishmentMenu.getType().equals(type)).findFirst().get().getInventory();
    }
}
