package me.rynder.soulbound.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.rynder.soulbound.Main;
import net.minecraft.server.v1_15_R1.AttributeModifier;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.NBTTagString;

public class SoulBoundCommand implements CommandExecutor {

	private Main plugin;

	private String SoulBoundText = ChatColor.LIGHT_PURPLE + "Soulbound";

	public SoulBoundCommand(Main plugin) {
		this.plugin = plugin;

		plugin.getCommand("soulbound").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub

		if (!(sender instanceof Player)) {
			sender.sendMessage("Can't use this on console");
			return false;
		}

		Player p = (Player) sender;

		if (p.hasPermission("soulbound.use")) {

			if (args.length == 0) {

				ItemStack soulItem = p.getItemInHand();
				if (soulItem == null || (soulItem.getAmount() > plugin.getConfig().getInt("SoulBound_MaxSize")
						|| soulItem.getAmount() <= 0)) {
					p.sendMessage(ChatColor.RED + "You can not put this enchantment on air!");
					return false;
				}
				if (soulItem.getEnchantments().containsKey(Enchantment.getByKey(Main.soulBoundEnchantment.getKey()))) {
					// remove soulbound

					ItemMeta meta = soulItem.getItemMeta();
					meta.removeEnchant(Main.soulBoundEnchantment);
					List<String> lore = new ArrayList<String>();

					if (meta.getLore() != null) {
						// p.sendMessage("lore not null");
						lore = meta.getLore();
						List<String> removalList = new ArrayList<String>();
						for (String line : lore) {
							if (line.contains(SoulBoundText)) {

								// p.sendMessage("soulbound found");
								removalList.add(line);
							}
						}
						for (String line : removalList) {
							lore.remove(line);
						}
						meta.setLore(lore);
						soulItem.setItemMeta(meta);
					}
					p.setItemInHand(soulItem);

				} else {

					ItemMeta meta = soulItem.getItemMeta();
					List<String> lore = new ArrayList<String>();

					if (meta.getLore() != null) {
						lore = meta.getLore();
					}

					lore.add(SoulBoundText + ChatColor.RESET);
					meta.setLore(lore);
					meta.addEnchant(Main.soulBoundEnchantment, 1, true);
					soulItem.setItemMeta(meta);
					// soulItem.addEnchantment(Main.soulBoundEnchantment, 1);
					p.setItemInHand(soulItem);
					// p.getInventory().addItem(soulItem);

					
					
				}

				return true;
			} else {
				p.sendMessage("Usage: /" + cmd.getName() + " (With item in hand)");
			}
		} else {
			p.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
		}

		return false;
	}

}
