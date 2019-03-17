package com.gmail.snowmanam2.factiontags;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

public class SidebarManager {
	private static SidebarManager i = null;
	
	private Objective objective = null;
	
	public static SidebarManager get() {
		if (i == null) {
			i = new SidebarManager();
		}
		
		return i;
	}
	
	private SidebarManager() {
		objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("Sidebar");
		
		if (objective == null) {
			objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("Sidebar", "dummy");
		}
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public String getLineEntry (int index) {
		String rawHex = Integer.toHexString(index);
		String retval = "";
		
		for (char c : rawHex.toCharArray()) {
			retval += "&"+c;
		}
		
		retval += "&r";
		
		return ChatColor.translateAlternateColorCodes('&', retval);
	}
	
	public Team getLineTeam (int index) {
		String teamName = "sb"+index;
		Team retval = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
		
		if (retval == null) {
			retval = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
			String entry = getLineEntry(index);
			retval.addEntry(entry);
			objective.getScore(entry).setScore(16-index);
		}
		
		return retval;
	}
	
	public void updateBoard() {
		
		// Dummy value to be changed in packet listener
		objective.setDisplayName("");
		
		// Dummy value to be changed in packet listener
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (int i = 1; i < 16; i++) {
			
			// Dummy value to be changed in packet listener
			getLineTeam(i).setPrefix("");
		}
	}
}
