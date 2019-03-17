package com.gmail.snowmanam2.factiontags;

import org.bukkit.ChatColor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class HeaderUpdateTask implements Runnable {

	private String header;
	private String footer;
	
	public HeaderUpdateTask (String header, String footer) {
		this.header = ChatColor.translateAlternateColorCodes('&', header);
		this.footer = ChatColor.translateAlternateColorCodes('&', footer);
	}
	
	@Override
	public void run() {
		PacketContainer packet = ProtocolLibrary.getProtocolManager().
		        createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
		
		packet.getChatComponents().write(0, WrappedChatComponent.fromText(this.header));
		packet.getChatComponents().write(1, WrappedChatComponent.fromText(this.footer));
		
		ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
	}
	

}
