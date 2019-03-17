package com.gmail.snowmanam2.factiontags;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;

public class ObjectiveSlotPacketAdapter extends PacketAdapter {

	public ObjectiveSlotPacketAdapter(Plugin plugin, PacketType[] types) {
		super(plugin, types);
	}
	
	
	// Fields based on PaperSpigot 1.8.8
	// Class /net/minecraft/server/PacketPlayOutScoreboardDisplayObjective
	@Override
	public void onPacketSending(PacketEvent event) {
		final PacketContainer packetContainer = event.getPacket().deepClone();
		
		try {
			String objectiveName = packetContainer.getStrings().read(0);
			
			if (!objectiveName.equals("Sidebar")) {
				return;
			}
			
			// TODO: Make this based on player preference
			boolean displaySidebar = true;
			
			// http://wiki.vg/Protocol#Display_Scoreboard
			// 1: Sidebar
			// 2: Above head
			// Because we use bogus entries for the scores, we can safely
			// hide the bar by showing over non-existent heads
			int displaySlot = displaySidebar ? 1 : 2;
			
			packetContainer.getIntegers().write(0, displaySlot);
			
			event.setPacket(packetContainer);
		} catch (final FieldAccessException e) {
			e.printStackTrace();
		}
	}
}
