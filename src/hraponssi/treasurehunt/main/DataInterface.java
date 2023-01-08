package hraponssi.treasurehunt.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

public class DataInterface {

	Main plugin;
	ConfigManager configManager;
	Utils utils;
	
	public DataInterface(Main plugin, ConfigManager cManager) {
		this.plugin = plugin;
		this.configManager = cManager;
		this.utils = new Utils();
	}
	
	public void saveData() {
		List<String> pf = configManager.getData().getStringList("playerFinds");
		configManager.getData().set("playerFinds", pf);
		pf.clear();
		for(String uuid : plugin.finds.keySet()) {
			ArrayList<Integer> list = plugin.finds.get(uuid);
			for(int number : list) {
				pf.add(uuid +  ":" + number);
			}
		}
		List<String> tl = configManager.getData().getStringList("treasureLocs");
		configManager.getData().set("treasureLocs", tl);
		tl.clear();
		for(Location loc : plugin.locations.keySet()) {
			int id = plugin.locations.get(loc);
			tl.add(id +  ":" +  loc.getBlockX() + "~"  + loc.getBlockY() +"~"+  loc.getBlockZ() +"~"+  loc.getWorld().getName());
		}
		configManager.saveData();
	}
	
	public void loadData() {
		List<String> pf = configManager.getData().getStringList("playerFinds");
		HashMap<String, ArrayList<Integer>> temp = new HashMap<>();
		for(String key: pf) {
			String[] splitted = key.split(":");
			if(temp.containsKey(splitted[0])) {
				temp.get(splitted[0]).add(utils.toInt(splitted[1]));
			}else {
				temp.put(splitted[0], new ArrayList<Integer>());
				temp.get(splitted[0]).add(utils.toInt(splitted[1]));
			}
		}
		plugin.finds = temp;
		List<String> tl = configManager.getData().getStringList("treasureLocs");
		for(String key: tl) {
			String[] splitted = key.split(":");
			String[] locsplitted = splitted[1].split("~");
			plugin.addTreasure(utils.toInt(splitted[0]), utils.newLocation(utils.toInt(locsplitted[0]), utils.toInt(locsplitted[1]), utils.toInt(locsplitted[2]), utils.getWorld(locsplitted[3])));
		}
	}
	
}
