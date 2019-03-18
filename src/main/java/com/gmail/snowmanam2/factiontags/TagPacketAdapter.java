package com.gmail.snowmanam2.factiontags;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
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
	// Fields based on PaperSpigot 1.8.8
	// Class /net/minecraft/server/PacketPlayOutScoreboardTeam
	@Override
	public void onPacketSending(PacketEvent event) {
		final PacketContainer packetContainer = event.getPacket().deepClone();
		
		try {
			Integer action = packetContainer.getIntegers().read(1);
			
			if (action.equals(0) || action.equals(2)) {
				
				MPlayer mpObserver = MPlayer.get(event.getPlayer());
				
				// This should be the unique team name
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
					Player otherPlayer = (Player) pSeen;
					if (otherPlayer.hasPermission("factiontags.showprefix")) {
						prefix = getPlayerPrefix(otherPlayer)+" "+prefix;
					}
					
					// This should be the team prefix
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
	
    private static String getPlayerPrefix(Player player) {
        String prefix = VaultUtil.getChat().getPlayerPrefix(player);
        if (prefix == null || prefix.equals("")) {
            String group = VaultUtil.getPermission().getPrimaryGroup(player);
            prefix = VaultUtil.getChat().getGroupPrefix(player.getWorld().getName(), group);
            if (prefix == null) {
                prefix = "";
            }
        }
        
        if (prefix.contains("[") && prefix.contains("]")) {
        	prefix = StringUtils.substringBetween(prefix, "[", "]");
        }
        
        String[] parts = prefix.split("&");
        
        if (parts.length > 1) {
        	boolean haveText = false;
        	
        	for (int i = parts.length - 1; i >= 0; i--) {
        		if (parts[i].length() > 1 || haveText) {
        			prefix = String.join("&", Arrays.copyOfRange(parts, 0, i+1));
        			
        			break;
        		}
        	}
        }
        
        
        if (prefix.length() > 13) {
        	prefix = prefix.substring(0,  13);
        }
        
        return ChatColor.translateAlternateColorCodes('&', prefix);
}

}
