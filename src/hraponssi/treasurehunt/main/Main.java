package hraponssi.treasurehunt.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {

	DataInterface dataInterface;
	ConfigManager configManager;
	Commands commands;
	EventHandlers eventHandlers;
	Menus menus;
	
	HashMap<Location, Integer> locations = new HashMap<>(); //Location, treasure id
	HashMap<String, ArrayList<Integer>> finds = new HashMap<>(); //UUID STRING, treasure id list
	
	HashMap<String, Integer> carrying = new HashMap<>(); //UUID STRING, treasure id
	
	HashMap<String, Integer> setting = new HashMap<>(); //UUID STRING, treasure id
	
	public void onDisable() {
		dataInterface.saveData();
	}
	
	public void onEnable() {
		this.configManager = new ConfigManager(this);
		this.dataInterface = new DataInterface(this, configManager);
		this.menus = new Menus(this);
		this.commands = new Commands(this, menus);
		this.eventHandlers = new EventHandlers(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(eventHandlers, this);
		getCommand("thunt").setExecutor(commands);
		getCommand("treasurehunt").setExecutor(commands);
		configManager.setup();
		dataInterface.loadData();
	}
	
	public void addTreasure(int id, Location loc) {
		if(!locations.containsKey(loc) && !locations.containsValue(id)) {
			locations.put(loc, id);
		} else getLogger().warning("Tried to set already existing location or id as treasure, id " + id + " location " + loc.toString());
	}
	
	public void removeTreasure(int id) {
		Location loc = getTreasureLoc(id);
		if(loc != null) {
			locations.remove(loc);
		} else getLogger().warning("Tried to remove treasure that doesnt exist, id " + id + " location null");
	}
	
	public boolean isTreasure(Location loc) {
		return locations.containsKey(loc);
	}
	
	public Location getTreasureLoc(int id) {
		for(Location l : locations.keySet()) {
			int lid = locations.get(l);
			if(lid == id) return l;
		}
		return null;
	}
	
	public void pickupTreasure(Player p, int id) {
		String uuid = p.getUniqueId().toString();
		carrying.put(uuid, id);
		Location loc = getTreasureLoc(id);
		ItemStack item = new ItemStack(loc.getBlock().getType());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Treasure " + id);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Return this treasure to spawn");
		meta.setLore(lore);
		item.setItemMeta(meta);
		if(loc.getBlock().getType().equals(Material.PLAYER_HEAD)) {
			Skull skull = (Skull) loc.getBlock().getState();
			SkullMeta headmeta = (SkullMeta) item.getItemMeta();
			headmeta.setOwningPlayer(skull.getOwningPlayer());
			headmeta.setOwnerProfile(skull.getOwnerProfile());
			item.setItemMeta(headmeta);
		}
		p.getInventory().setItem(1, item);
	}
	
	public void dropTreasure(Player p) {
		String uuid = p.getUniqueId().toString();
		int id = carrying.get(uuid);
		p.getInventory().setItem(1, null);
		carrying.remove(uuid);
	}
	
}
