package com.gmail.snowmanam2.factiontags;

import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.massivecraft.factions.entity.MPlayer;

public class TagPacketAdapter extends PacketAdapter {

	public TagPacketAdapter(Plugin p, PacketType type) {
		super(p, type);
	}
	
	// The purpose of this magic is to customize the team packets to the individual players.
	// We could have used individual scoreboards, but that may have been a bit of a mess
	// because each scoreboard would need every player added to it.
	// We still use the scoreboard API so we don't have to manage all the other overhead.
	@Override
	public void onPacketSending(PacketEvent event) {
		final PacketContainer packetContainer = event.getPacket().deepClone();
		
		try {
			Integer action = packetContainer.getIntegers().read(1);
			
			if (action.equals(0) || action.equals(2)) {
				
				MPlayer mpObserver = MPlayer.get(event.getPlayer());
				
				// This should be the unique team name
				// Index found from NMS PacketPlayOutScoreboardTeam
				String teamKey = packetContainer.getStrings().read(0);
				
				if (!StringUtils.isNumeric(teamKey)) {
					return;
				}
				
				OfflinePlayer pSeen = TeamManager.get().getTeamPlayer(teamKey);
				
				if (pSeen == null) {
					return;
				}
				if (!pSeen.isOnline()) {
					return;
				}
				MPlayer mpSeen = MPlayer.get((Player)pSeen);
				
				if (mpSeen != null) {
				
					String prefix = mpSeen.getColorTo(mpObserver).toString();
					
					if (mpSeen.getFaction().isDefault()) {
						prefix = "";
					}
					
					// This should be the team prefix
					// Index found from NMS PacketPlayOutScoreboardTeam
					packetContainer.getStrings().write(2, prefix);
				} else {
					this.getPlugin().getLogger().warning("Invalid player with team id '"+teamKey+"'");
				}
			}
			event.setPacket(packetContainer);
			
		} catch (final FieldAccessException e) {
			e.printStackTrace();
		}
	}

}
