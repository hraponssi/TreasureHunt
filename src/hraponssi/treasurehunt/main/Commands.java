package hraponssi.treasurehunt.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	Main plugin;
	Utils utils;
	Menus menus;
	
	public Commands(Main plugin, Menus menus) {
		this.plugin = plugin;
		this.utils = new Utils();
		this.menus = menus;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if(command.equalsIgnoreCase("thunt") || command.equalsIgnoreCase("treasurehunt")){
			if(!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Treasure hunt commands can only be run by a player!");
				return true;
			}
			Player p = (Player) sender;
			if(args.length>0) {
				if(args[0].equalsIgnoreCase("set")) { 
					if(p.hasPermission("thunt.admin")){
						if(args.length>1) {
							int id = utils.toInt(args[1]);
							plugin.seting.put(p.getUniqueId().toString(), id);
							p.sendMessage(ChatColor.GREEN + "Now setting treasure location " + id);
						}else if (plugin.seting.containsKey(p.getUniqueId().toString())) {
							plugin.seting.remove(p.getUniqueId().toString());
							p.sendMessage(ChatColor.GREEN + "Removed from treasure seters");
						}else {
							p.sendMessage(ChatColor.RED + "Use 'set <id number>'");
						}
					} else p.sendMessage(ChatColor.RED + "You dont have permission");
				} else if(args[0].equalsIgnoreCase("remove")) { 
					if(p.hasPermission("thunt.admin")){
						if(args.length>1) {
							int id = utils.toInt(args[1]);
							if(plugin.getTreasureLoc(id) == null) {
								p.sendMessage(ChatColor.RED + "That treasure id doesnt exist.");
								return true;
							}
							plugin.locations.remove(plugin.getTreasureLoc(id));
							p.sendMessage(ChatColor.GREEN + "Removed treasure location " + id);
						}else p.sendMessage(ChatColor.RED + "Use 'remove <id number>'");
					} else p.sendMessage(ChatColor.RED + "You dont have permission");
				} else if(args[0].equalsIgnoreCase("resetfinds")) { 
					if(p.hasPermission("thunt.admin")){
						if(plugin.finds.containsKey(p.getUniqueId().toString())) {
							plugin.finds.remove(p.getUniqueId().toString());
							p.sendMessage(ChatColor.GREEN + "Your finds list has been removed!");
						}else{
							p.sendMessage(ChatColor.RED + "Couldn't find a finds list for you!");
						}
					} else p.sendMessage(ChatColor.RED + "You dont have permission");
				} else if(args[0].equalsIgnoreCase("drop")) { 
					if(p.hasPermission("thunt.user")){
						if(plugin.carrying.containsKey(p.getUniqueId().toString())) {
							int id = plugin.carrying.get(p.getUniqueId().toString());
							plugin.dropTreasure(p);
							p.sendMessage(ChatColor.GREEN + "Dropped treasure " + id);
						}else p.sendMessage(ChatColor.RED + "You aren't carrying treasure");
					} else p.sendMessage(ChatColor.RED + "You dont have permission");
				}else {
					p.sendMessage(ChatColor.RED + "Invalid arguments");
				}
			} else {
				menus.openMenu(p);
			}
			return true;
		}
		return false;
	}

}
