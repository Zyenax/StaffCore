package net.warvale.staffcore.utils;

import net.warvale.staffcore.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Created by Callum for StaffCore.
 */
public class PlayerManager {
	private StaffCore plugin;
	private File folder;
	private File df;
	private File cfile;
	private FileConfiguration config;
	
	public PlayerManager(StaffCore plugin) {
		this.plugin = plugin;
		this.folder = new File(plugin.getDataFolder(), "data" + File.separator);
		this.df = plugin.getDataFolder();
	}
	
	public void create(Player p) {
		cfile = new File(df, "data" + File.separator + p.getUniqueId() + ".yml");
		if (!df.exists()) df.mkdir();
		if (!cfile.exists()) {
			try {
				cfile.createNewFile();
			} catch(Exception e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating " + cfile.getName() + "!");
			}
		}
		config = YamlConfiguration.loadConfiguration(cfile);
	}
	
	public File getFolder() {
		return folder;
	}
	
	public File getFile() {
		return cfile;
	}
	
	public void load(Player p) {
		cfile = new File(df + File.separator + "data" + File.separator + p.getUniqueId() + ".yml");
		config = YamlConfiguration.loadConfiguration(cfile);
	}
	
	public FileConfiguration get() {
		return config;
	}
	
	public void save() {
		try {
			config.save(cfile);
		} catch(Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error saving " + cfile.getName() + "!");
		}
	}
}
