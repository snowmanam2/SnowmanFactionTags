package com.gmail.snowmanam2.factiontags;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultUtil {
	private static Economy economy = null;
	private static Permission permission = null;
	private static Chat chat = null;
	
	public static void init(JavaPlugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rspEconomy = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rspEconomy != null) economy = rspEconomy.getProvider();
        
        RegisteredServiceProvider<Permission> rspPermission = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if (rspPermission != null) permission = rspPermission.getProvider();
        
        RegisteredServiceProvider<Chat> rspChat = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        if (rspChat != null) chat = rspChat.getProvider();
	}
	
	public static Economy getEconomy() {
		return economy;
	}
	
	public static Permission getPermission() {
		return permission;
	}
	
	public static Chat getChat() {
		return chat;
	}
	
}
