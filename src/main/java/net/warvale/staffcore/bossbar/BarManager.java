package net.warvale.staffcore.bossbar;

import net.md_5.bungee.api.ChatColor;
import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BarManager {

    private static BossBar announceBar;
    private static BukkitRunnable announceBarCallBack = null;

    private static BarManager instance;

    public static BarManager getInstance() {
        if (instance == null) {
            instance = new BarManager();
        }
        return instance;
    }

    public void setup() {
        announceBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        announceBar.setVisible(false);
    }

    /**
     * Broadcasts an announcer bar
     *
     * @param barColor the color of the announcer bar
     * @param string the message
     * @param replace the params to replace in the message
     */
    public static void broadcast(BarColor barColor, String string, Object ... replace){
        announceBar.setTitle(String.format(string, replace));
        if(announceBarCallBack != null){
            announceBarCallBack.cancel();
        }
        announceBar.setProgress(0);
        announceBar.setVisible(true);
        announceBar.setColor(barColor);

        announceBarCallBack = new BukkitRunnable() {
            final double num = 0.025;
            double sofar = 0;
            public void run() {
                if((sofar += num) > 1){
                    cancel();
                    announceBar.setVisible(false);
                    announceBarCallBack = null;
                }
                else announceBar.setProgress(sofar);
            }
        };
        announceBarCallBack.runTaskTimerAsynchronously(StaffCore.get(), 0, 1);
    }

    /**
     * Broadcasts an announcer bar
     *
     * @param  type the prefix to use
     * @param barColor the color of the announcer bar
     * @param string the message
     * @param replace the params to replace in the message
     */
    public static void broadcast(PrefixType type, BarColor barColor, String string, Object ... replace){
        announceBar.setTitle(MessageManager.getPrefix(type) + String.format(string, replace));
        if(announceBarCallBack != null){
            announceBarCallBack.cancel();
        }
        announceBar.setProgress(0);
        announceBar.setVisible(true);
        announceBar.setColor(barColor);

        announceBarCallBack = new BukkitRunnable() {
            final double num = 0.025;
            double sofar = 0;
            public void run() {
                if((sofar += num) > 1){
                    cancel();
                    announceBar.setVisible(false);
                    announceBarCallBack = null;
                }
                else announceBar.setProgress(sofar);
            }
        };
        announceBarCallBack.runTaskTimerAsynchronously(StaffCore.get(), 0, 1);
    }

    /**
     * Broadcasts a sound to all players
     *
     * @param sound the sound to broadcast
     */
    public static void broadcastSound(Sound sound){
        for(Player player: Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), sound, 1f, 1f);
        }
    }

    public static BossBar getAnnounceBar() {
        return announceBar;
    }

    public static BukkitRunnable getAnnounceBarCallBack() {
        return announceBarCallBack;
    }

    public static void setAnnounceBarCallBack(BukkitRunnable callBack) {
        announceBarCallBack = callBack;
    }

}
