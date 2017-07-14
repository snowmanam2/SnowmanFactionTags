package com.gmail.snowmanam2.factiontags;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

// Team names are limited to 16 characters, but UUIDs are longer than that.
// The main purpose of this class is to convert UUIDs into something that
// is unique while fitting in the team limit.
public class TeamManager {
	private static TeamManager i = null;
	
	private BiMap<UUID, Integer> teamLookup;
	
	public static TeamManager get() {
		if (i == null) {
			i = new TeamManager();
		}
		
		return i;
	}
	
	private TeamManager() {
		teamLookup = HashBiMap.create();
		
		// Teams are saved on the server, so we need to purge the old IDs so we know who is who.
		// We create a new Set because the returned set from Bukkit breaks when teams are unregistered
		Set<Team> teamSet = new LinkedHashSet<Team>(Bukkit.getScoreboardManager().getMainScoreboard().getTeams());
		for (Team team : teamSet) {
			team.unregister();
		}
	}
	
	public String getPlayerTeamKey(UUID id) {
		
		if (!teamLookup.containsKey(id)) {
			
			Integer newTeam = 1;
			
			if (teamLookup.size() > 0) {
				newTeam = Collections.max(teamLookup.values())+1;
			}
			
			teamLookup.put(id, newTeam);
		}
		
		return teamLookup.get(id).toString();
	}
	
	public Team getPlayerTeam(Player p) {
		UUID id = p.getUniqueId();
		
		if (id == null) {
			return null;
		}
		
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		
		String teamKey = getPlayerTeamKey(id);
		
		Team retval = board.getTeam(teamKey);
		
		if (retval == null) {
			retval = board.registerNewTeam(teamKey);
			retval.addEntry(p.getName());
			Bukkit.getPluginManager().getPlugin("FactionTags").getLogger().info("New team for "+p.getName());
		}
		
		return retval;
	}
	
	public OfflinePlayer getTeamPlayer(String teamId) {
		UUID playerId = null;
		try {
			playerId = teamLookup.inverse().get(Integer.valueOf(teamId));
		} catch (IllegalArgumentException e) {
			return null;
		}
		
		if (playerId == null) {
			return null;
		}
			
		return Bukkit.getOfflinePlayer(playerId);
	}
	
}
