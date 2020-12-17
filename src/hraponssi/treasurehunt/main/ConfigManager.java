package hraponssi.treasurehunt.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	
	public ConfigManager(Main plugin) {
        super();
        this.plugin = plugin;
    }
	
	public FileConfiguration datacfg;
	public File datafile;
	Main plugin;

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		datafile = new File(plugin.getDataFolder(), "data.yml");

		if (!datafile.exists()) {
			try {
				datafile.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The data.yml file has been created");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.RED + "Could not create the data.yml file");
			}
		}

		datacfg = YamlConfiguration.loadConfiguration(datafile);
	}

	public FileConfiguration getData() {
		return datacfg;
	}

	public void saveData() {
		try {
			datacfg.save(datafile);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The data.yml file has been saved");

		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the data.yml file");
		}
	}

	public void reloadGamedata() {
		datacfg = YamlConfiguration.loadConfiguration(datafile);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "The data.yml file has been reload");

	}
	
}
