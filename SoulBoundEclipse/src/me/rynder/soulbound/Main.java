package me.rynder.soulbound;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.rynder.soulbound.commands.SoulBoundCommand;
import me.rynder.soulbound.enchants.SoulBoundEnchant;
import me.rynder.soulbound.managers.SaveLoadManager;

public class Main extends JavaPlugin {

	public HashMap<UUID, HashMap<ItemStack, Integer>> boundItems = new HashMap<UUID, HashMap<ItemStack, Integer>>();

	private static Main plugin;
	public static SoulBoundEnchant soulBoundEnchantment;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		plugin = this;

		Logger logger = this.getLogger();
		 
        new UpdateChecker(this, 76017).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("There is not a new update available.");
            } else {
                logger.info("There is a new update available.");
            }
        });
		
		new SoulBoundCommand(this);

		soulBoundEnchantment = new SoulBoundEnchant("soulbound");

		registerEnchantment(soulBoundEnchantment);

		this.getServer().getPluginManager().registerEvents(soulBoundEnchantment, this);

		SaveLoadManager manager = new SaveLoadManager(this);

		File folder = new File(manager.fileDirectory);

		if (!(folder.exists())) {
			folder.mkdir();
		}
		
		try (Stream<Path> walk = Files.walk(Paths.get(manager.fileDirectory))) {

			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".yml"))
					.collect(Collectors.toList());

			for (String file : result) {

				
				String uuidStr = file.substring(file.lastIndexOf('\\')+1, file.lastIndexOf('.'));
				System.out.println(uuidStr);
				
				UUID playerId = UUID.fromString(uuidStr);
				try {
					System.out.println("Player Found " + Bukkit.getOfflinePlayer(playerId).getName());
					manager.LoadBoundList(playerId);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onDisable() {

		SaveLoadManager manager = new SaveLoadManager(this);
		for (UUID id : boundItems.keySet()) {// for each player, save
			System.out.println("GOing on a looop");
			try {
				manager.SaveBoundList(id);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static Main getPlugin() {
		return plugin;
	}

	public static void registerEnchantment(Enchantment enchantment) {

		boolean registered = true;
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
			Enchantment.registerEnchantment(enchantment);

		} catch (Exception e) {
			registered = false;
			// e.printStackTrace();//annoying error to do with reloads

		}

		if (registered) {

		}

	}

}
