package com.gmail.snowmanam2.factiontags;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;

public class FactionTags extends JavaPlugin implements Listener {
	
	private TagUpdateTask updateTask;
	private HeaderUpdateTask headerTask;
	
	@Override
	public void onEnable() {
		
		FileConfiguration config = getConfig();
		
		config.addDefault("updateInterval", 20L);
		config.addDefault("playersPerTick", 20);
		config.addDefault("header", "&bHeader");
		config.addDefault("footer", "&cFooter");
		config.addDefault("healthSymbol", "&c❤");
		config.options().copyDefaults(true);
		saveConfig();
		
		VaultUtil.init(this);
		
		updateTask = new TagUpdateTask(config.getLong("updateInterval"), config.getInt("playersPerTick"));
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, updateTask, 0L, 1L);
		
		headerTask = new HeaderUpdateTask(config.getString("header"), config.getString("footer"));
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, headerTask, 0L, 1L);
		
		registerTeamPacketListener();
		TeamManager.get();
		
		registerHealthBar();
		
		getServer().getPluginManager().registerEvents(this, this);
		
	}
	
	@Override
	public void onDisable() {
		unregisterHealthBar();
	}
	
	@EventHandler
	public void onPlayerLogin (PlayerLoginEvent e) {
		final Player p = e.getPlayer();
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			public void run() {
				if (p != null) {
					p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
				}
			}
		}, 10L);
	}
		
	public void registerTeamPacketListener() {
		ProtocolLibrary.getProtocolManager().addPacketListener(new TagPacketAdapter(this, PacketType.Play.Server.SCOREBOARD_TEAM));
	}
	
	private void registerHealthBar() {
		Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
		
		if (sb.getObjective("health") != null) {
			sb.getObjective("health").unregister();
		}
		
		Objective o = sb.registerNewObjective("health", "health");

		String displayName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("healthSymbol"));
		
		o.setDisplayName(displayName);
		o.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}
	
	private void unregisterHealthBar() {
		Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
		if (sb.getObjective("health") != null) {
			sb.getObjective("health").unregister();
		}
	}
	
};
