package com.gmail.snowmanam2.factiontags;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;

public class ObjectivePacketAdapter extends PacketAdapter {

	public ObjectivePacketAdapter(Plugin plugin, PacketType[] types) {
		super(plugin, types);
	}
	
	// Fields based on PaperSpigot 1.8.8
	// Class /net/minecraft/server/PacketPlayOutScoreboardObjective
	@Override
	public void onPacketSending(PacketEvent event) {
		final PacketContainer packetContainer = event.getPacket().deepClone();
		
		try {
			String objectiveName = packetContainer.getStrings().read(0);
			
			if (!objectiveName.equals("Sidebar")) {
				return;
			}
			
			// TODO: Update the Sidebar title here
			String displayName = "";
			packetContainer.getStrings().write(1, displayName);
			
			event.setPacket(packetContainer);
		} catch (final FieldAccessException e) {
			e.printStackTrace();
		}
	}

}
