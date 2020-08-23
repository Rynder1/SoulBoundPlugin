package me.rynder.soulbound.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import me.rynder.soulbound.Main;

public class SaveLoadManager {

	private Main plugin;
	private static String folderName = ("/Items");
	public static String fileDirectory = Main.getPlugin().getDataFolder() + folderName + "/";

	public SaveLoadManager(Main plugin) {
		this.plugin = plugin;

	}

	// maybe have to save a number and then all the objects and load them in 1 by 1

	public void SaveBoundList(UUID id) throws FileNotFoundException, IOException {

		File folder = new File(fileDirectory);

		if (!(folder.exists())) {
			folder.mkdir();
		}

		File file = new File(fileDirectory + id.toString() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		Integer length = plugin.boundItems.get(id).size();

		config.set("Length", length);

		Integer counter = 0;

		for (ItemStack item : plugin.boundItems.get(id).keySet()) {
			counter++;

			config.set(counter.toString(), item);
			config.set(counter.toString() + "n", plugin.boundItems.get(id).get(item));

		}

		config.save(file);

	}

	public void LoadBoundList(UUID id) throws FileNotFoundException, IOException, ClassNotFoundException {

		File folder = new File(fileDirectory);

		if (!(folder.exists())) {
			folder.mkdir();
		}

		File file = new File(fileDirectory + id.toString() + ".yml");

		if (file.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);

			Integer length = config.getInt("Length");

			HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();
			for (Integer i = 1; i <= length; i++) {
				items.put(config.getItemStack(i.toString()), config.getInt(i.toString() + "n"));
			}
			plugin.boundItems.put(id, items);
			file.delete();

		}
	}

}
