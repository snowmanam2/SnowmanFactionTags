package com.gmail.snowmanam2.factiontags;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.massivecraft.massivecore.util.MUtil;

public class TagUpdateTask implements Runnable {

	private Set<OfflinePlayer> queuedPlayers;
	private long updateInterval;
	private int playersPerTick;
	long updateIndex = 0;
	
	public TagUpdateTask(long updateInterval, int playersPerTick) {
		queuedPlayers = new LinkedHashSet<OfflinePlayer>();
		this.updateInterval = updateInterval;
		this.playersPerTick = playersPerTick;
	}
	
	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}
	
	public void setPlayersPerTick(int playersPerTick) {
		this.playersPerTick = playersPerTick;
	}
	
	// Instead of using faction events to manage this, we use a timed method
	// This way we don't have to worry about all the join/disband/relation stuff
	public void run() {
		
		updateIndex++;
		
		if (updateIndex > updateInterval) {
			updateIndex = 0;
			
			queuedPlayers.addAll(MUtil.getOnlinePlayers());
		}
		
		Iterator <OfflinePlayer> iterator = queuedPlayers.iterator();
		for (int i = 0; i < playersPerTick; i++) {
			if (!iterator.hasNext()) {
				break;
			}
			
			// This forces a team update packet for the given player/team
			// We don't set the real prefix here because it would not be customized to each observer
			OfflinePlayer op = iterator.next();
			if (op.isOnline()) {
				Team team = TeamManager.get().getPlayerTeam((Player)op);
				
				if (team != null) {
					team.setPrefix("");
				}
			}
			
			iterator.remove();
		}
		
	}

}
