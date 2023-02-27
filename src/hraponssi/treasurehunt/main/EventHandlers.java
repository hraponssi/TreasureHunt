package hraponssi.treasurehunt.main;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import net.md_5.bungee.api.ChatColor;

public class EventHandlers implements Listener {

	Main plugin;
	Utils utils;
	
	public EventHandlers(Main plugin) {
		this.plugin = plugin;
		this.utils = new Utils();
	}
	
	ArrayList<Player> clicked = new ArrayList<>();
	
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        String name = ChatColor.stripColor(e.getView().getTitle());
        name = name.toLowerCase();
        if(name.equals("treasure hunt")) e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		Player p = event.getPlayer();
		String uuid = p.getUniqueId().toString();
		Block b = event.getClickedBlock();
		Location loc = b.getLocation();
		if(clicked.contains(p)) return;
		if(plugin.setting.containsKey(uuid)) {
			int id = plugin.setting.get(uuid);
			clicked.add(p);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					clicked.remove(p);
				}
			}, 10L);
			plugin.addTreasure(id, loc);
			plugin.setting.remove(uuid);
			p.sendMessage(ChatColor.GREEN + "You set treasure location " + id + " to that " + b.getType().name() + " block at " + b.getLocation().toString());
		}else if(plugin.isTreasure(loc)) {
			int id = plugin.locations.get(loc);
			clicked.add(p);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					clicked.remove(p);
				}
			}, 10L);
			if(id == 0) {
				if(!plugin.carrying.containsKey(uuid)) {
					p.sendMessage(ChatColor.RED + "You are not carrying treasure! Find treasure to drop it off here.");
					return;
				}
				int cid = plugin.carrying.get(uuid);
				p.sendMessage(ChatColor.GREEN + "You have returned treasure "+ cid + "!");
				plugin.dropTreasure(p);
				boolean found = false;
				if(plugin.finds.containsKey(uuid)) {
					if(plugin.finds.get(uuid).contains(cid)) found = true;
					plugin.finds.get(uuid).add(cid);
				}else {
					plugin.finds.put(uuid, new ArrayList<Integer>());
					plugin.finds.get(uuid).add(cid);
				}
				if(!found) {
					Firework fw = (Firework) loc.getWorld().spawnEntity(loc.add(0.5, 0, 0.5), EntityType.FIREWORK);
			        FireworkMeta fwm = fw.getFireworkMeta();
			       
			        fwm.setPower(2);
			        fwm.addEffect(FireworkEffect.builder().withColor(Color.BLUE).flicker(true).build());
			       
			        fw.setFireworkMeta(fwm);
			        fw.detonate();
				}
				event.setCancelled(true); //Cancel so you don't place the treasure
			}else {
				if(plugin.carrying.containsKey(uuid)) {
					p.sendMessage(ChatColor.RED + "You are already carrying treasure! Drop it with /thunt drop");
					return;
				}
				plugin.pickupTreasure(p, id);
				p.sendMessage(ChatColor.BLUE + "You have picked up treasure " + id + ", to drop it use /thunt drop");
				p.sendMessage(ChatColor.GRAY + "Teleporting will make you drop treasures.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player p = event.getPlayer();
		String uuid = p.getUniqueId().toString();
		if(plugin.carrying.containsKey(uuid)) {
			plugin.dropTreasure(p);
			p.sendMessage(ChatColor.RED + "You have dropped your treasure!");
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if(plugin.carrying.containsKey(p.getUniqueId().toString())) {
			plugin.dropTreasure(p);
		}
	}
	
	
}
