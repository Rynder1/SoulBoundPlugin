package me.rynder.soulbound.enchants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.rynder.soulbound.Main;
import me.rynder.soulbound.managers.SaveLoadManager;

public class SoulBoundEnchant extends Enchantment implements Listener {

	public SoulBoundEnchant(String namespace) {
		super(new NamespacedKey(Main.getPlugin(), namespace));
		// TODO Auto-generated constructor stub
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getKeepInventory() == false) {

			Player p = e.getEntity();
			
			List<ItemStack> droppedItems = e.getDrops();
			HashMap<ItemStack, Integer> newInventory = new HashMap<ItemStack, Integer>();

			
			for(int i = 1; i <= 40; i++) {
				Bukkit.getConsoleSender().sendMessage(i + "");
				if(p.getInventory().getItem(i) != null) {
					
					if(p.getInventory().getItem(i).getEnchantments().containsKey(Enchantment.getByKey(Main.soulBoundEnchantment.getKey()))) {
						if(droppedItems.contains(p.getInventory().getItem(i))) {							
							newInventory.put(p.getInventory().getItem(i), i);
							e.getDrops().remove(p.getInventory().getItem(i));
						}
					}
				}
			}
				
			Main.getPlugin().boundItems.put(e.getEntity().getUniqueId(), newInventory);
			SaveLoadManager manager = new SaveLoadManager(Main.getPlugin());
			try {
				manager.SaveBoundList(e.getEntity().getUniqueId());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {

		if (Main.getPlugin().boundItems.containsKey(e.getPlayer().getUniqueId())) {
			
			for(ItemStack item : Main.getPlugin().boundItems.get(e.getPlayer().getUniqueId()).keySet()) {
				
				int itemNumber = Main.getPlugin().boundItems.get(e.getPlayer().getUniqueId()).get(item);
				if(e.getPlayer().getInventory().getItem(itemNumber) != null) {
					e.getPlayer().getInventory().addItem(item);
				} else {		
					e.getPlayer().getInventory().setItem(itemNumber, item);
				}
			}
				
			
			Main.getPlugin().boundItems.remove(e.getPlayer().getUniqueId());

			SaveLoadManager manager = new SaveLoadManager(Main.getPlugin());
			File file = new File(manager.fileDirectory + e.getPlayer().getUniqueId().toString() + ".yml");
			if (file.exists()) {
				file.delete();
			}
		}

	}

	@Override
	public boolean canEnchantItem(ItemStack arg0) {// can be put on anything
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean conflictsWith(Enchantment arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxLevel() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SOULBOUND";
	}

	@Override
	public int getStartLevel() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isCursed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTreasure() {
		// TODO Auto-generated method stub
		return false;
	}

}
