package com.ccode.wlintegrity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class IntegrityPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		try {
			File Jar = GetLocation();
			String inputFilePath = Jar.getParent() + File.separator + "data.csv";
			List<String> list = GetWhitelist(LoadDataRaw(inputFilePath));
			for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
				Boolean onList = false;
				getLogger().info("Checking name " + player.getName());
				for (String name : list) {
					/*if (name.strip().length() > 0) {
						getLogger().info(Integer.valueOf(name.strip().charAt(0)).toString());
					}*/
					if (name.strip().equals(player.getName()) || ("." + name.strip()).equals(player.getName())) {
						onList = true;
						getLogger().info("Found them on the List");
					}
				}
				if (!onList) {
					getLogger().info("Removing player from whitelist");
					player.setWhitelisted(false);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<List<String>> LoadDataRaw(String Filename) throws IOException {
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(Filename))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        records.add(Arrays.asList(values));
		    }
		}
		return records;
	}
	
	private List<String> GetWhitelist(List<List<String>> RawData) {
		List<String> ret = new ArrayList<>();
		for (List<String> row : RawData) {
			try {
				ret.add(row.get(0).strip());
			} catch (ArrayIndexOutOfBoundsException e) {
				;
			}
		}
		return ret;
	}
	
	private File GetLocation() throws URISyntaxException {
		return new File(IntegrityPlugin.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	}
}
