package hraponssi.treasurehunt.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Menus {

	Main plugin;
	Utils utils;
	
	public Menus(Main plugin) {
		this.plugin = plugin;
		this.utils = new Utils();
	}
	
	public void openMenu(Player player) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + "Treasure Hunt");
		int slot = 1;
		String uuid = player.getUniqueId().toString();
		int id = 1;
		while(id <= plugin.locations.size()-1) {
			Location loc = plugin.getTreasureLoc(id);
			Material type = loc.getBlock().getType();
			ItemStack item = new ItemStack(type);
			List<String> lore = new ArrayList<String>();
			if(!plugin.finds.containsKey(uuid)) plugin.finds.put(uuid, new ArrayList<Integer>());
			if(plugin.finds.get(uuid).contains(id)) {
				item.setType(Material.GREEN_WOOL);
				lore.add(ChatColor.GREEN + "Found: " + plugin.finds.get(uuid).contains(id));
			}
			if(!plugin.finds.get(uuid).contains(id)) {
				item.setType(Material.RED_WOOL);
				lore.add(ChatColor.RED + "Found: " + plugin.finds.get(uuid).contains(id));
			}
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Treasure " + id);
			meta.setLore(lore);
			item.setItemMeta(meta);
			if(slot<=7) inv.setItem(slot, item);
			slot++;
			id++;
		}

		player.openInventory(inv);
	}
	
}
