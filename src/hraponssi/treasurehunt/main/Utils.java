package hraponssi.treasurehunt.main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Utils {
	
	public Location newLocation(double x, double y, double z, World world) {
		return new Location(world, x, y, z);
	}
	
	public int toInt(String s) {
		return Integer.parseInt(s);
	}
	
	public World getWorld(String s) {
		return Bukkit.getWorld(s);
	}
	
}
