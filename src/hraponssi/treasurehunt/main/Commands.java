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
        if (command.equalsIgnoreCase("thunt") || command.equalsIgnoreCase("treasurehunt")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Treasure hunt commands can only be run by a player!");
                return true;
            }
            Player p = (Player) sender;

            if (args.length < 1) {
                menus.openMenu(p);
                return true;
            }

            switch (args[0].toLowerCase()) {
            case "set":
                if (!p.hasPermission("thunt.admin")) {
                    p.sendMessage(ChatColor.RED + "You dont have permission");
                    return true;
                }

                if (args.length > 1) {
                    int id = utils.toInt(args[1]);
                    plugin.setting.put(p.getUniqueId().toString(), id);
                    p.sendMessage(ChatColor.GREEN + "Now setting treasure location " + id);
                } else if (plugin.setting.containsKey(p.getUniqueId().toString())) {
                    plugin.setting.remove(p.getUniqueId().toString());
                    p.sendMessage(ChatColor.GREEN + "Removed from treasure seters");
                } else {
                    p.sendMessage(ChatColor.RED + "Use 'set <id number>'");
                }

                break;
            case "remove":
                if (!p.hasPermission("thunt.admin")) {
                    p.sendMessage(ChatColor.RED + "You dont have permission");
                    return true;
                }
                if (args.length < 1) {
                    p.sendMessage(ChatColor.RED + "Use 'remove <id number>'");
                    return true;
                }

                int id = utils.toInt(args[1]);
                if (plugin.getTreasureLoc(id) == null) {
                    p.sendMessage(ChatColor.RED + "That treasure id doesnt exist.");
                    return true;
                }
                plugin.locations.remove(plugin.getTreasureLoc(id));
                p.sendMessage(ChatColor.GREEN + "Removed treasure location " + id);

                break;
            case "resetfinds":
                if (!p.hasPermission("thunt.admin")) {
                    p.sendMessage(ChatColor.RED + "You dont have permission");
                    return true;
                }

                if (plugin.finds.containsKey(p.getUniqueId().toString())) {
                    plugin.finds.remove(p.getUniqueId().toString());
                    p.sendMessage(ChatColor.GREEN + "Your finds list has been removed!");
                } else {
                    p.sendMessage(ChatColor.RED + "Couldn't find a finds list for you!");
                }

                break;
            case "drop":
                if (!p.hasPermission("thunt.user")) {
                    p.sendMessage(ChatColor.RED + "You dont have permission");
                    return true;
                }

                if (!plugin.carrying.containsKey(p.getUniqueId().toString())) {
                    p.sendMessage(ChatColor.RED + "You aren't carrying treasure");
                    return true;
                }

                int cid = plugin.carrying.get(p.getUniqueId().toString());
                plugin.dropTreasure(p);
                p.sendMessage(ChatColor.GREEN + "Dropped treasure " + cid);

                break;
            default:
                if (p.hasPermission("thunt.admin")) {
                    p.sendMessage(ChatColor.RED + "Invalid arguments. Available: set, remove, resetfinds, drop");
                } else {
                    p.sendMessage(ChatColor.RED + "Invalid arguments.");
                }

                break;
            }

            return true;
        }
        return false;
    }

}
