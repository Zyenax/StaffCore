package net.warvale.staffcore.listeners;

import net.warvale.staffcore.StaffCore;
import net.warvale.staffcore.message.MessageManager;
import net.warvale.staffcore.message.PrefixType;
import net.warvale.staffcore.rank.Rank;
import net.warvale.staffcore.rank.RankManager;
import net.warvale.staffcore.users.User;
import net.warvale.staffcore.users.UserManager;
import net.warvale.staffcore.utils.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;


public class SessionListener implements Listener {
	private StaffCore plugin;
	
	public SessionListener(StaffCore plugin) {
		this.plugin = plugin;
	}


	// Establishes permissions without interrupting server thread
	// Ensures player will have user data for login
	@EventHandler (priority = EventPriority.HIGH)
	public void onJoin(AsyncPlayerPreLoginEvent event) {

		User user = UserManager.getUser(event.getUniqueId());

		if (user == null) {
			user = new User(event.getUniqueId());
			user.setName(event.getName());
			UserManager.saveUser(user);
			StaffCore.get().getLogger().log(Level.INFO, "Created New User: " + user.getName() + " (" + user.getUuid() + ")");
		}

		// Adds the user to the online users
		UserManager.getUsers().add(user);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		User user = UserManager.getUser(player);

		// If the server was not able to pull data, reject the login
		// The plugin was likely unable to communicate with the datastore
		if (user == null) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§c§l[WarvalePerms] §cNo user data. Please contact an admin.");
			return;
		}

		// Register a permission attachment with bukkit and refresh the permissions
		user.setAttachment(player.addAttachment(StaffCore.get()));

		// We don't have a world yet, we'll refresh again when a player has fully logged in
		user.refreshPermissions(null);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		User user = UserManager.getUser(player);

		// Unload user data if they exist
		if (user != null) {
			if (user.getAttachment() != null) {
				player.removeAttachment(user.getAttachment());
				user.setAttachment(null);
			}
			UserManager.getUsers().remove(user);
		}

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Rank rank = RankManager.getDefaultRank();
		Player player = event.getPlayer();
		// If there is no default rank, notify the OP to create one
		if (rank == null) {
			if (player.isOp()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						player.sendMessage(MessageManager.getPrefix(PrefixType.MAIN) + "§cYou don't have a default group.");
						player.sendMessage(MessageManager.getPrefix(PrefixType.MAIN) + "§c§l[WarvalePerms] §cYou can fix this by simply making a rank. The default rank is automatically the one with the lowest priority.");
					}
				}.runTaskLater(StaffCore.get(), 40L);
			}
		}
		// Rebuild user permissions with their current world
		User user = UserManager.getUser(player);
		if (user != null) {
			user.refreshPermissions(player.getWorld());
		}

	}
}
