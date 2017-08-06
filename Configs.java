import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Configs {
	private FileConfiguration config = null;
	private File file = null;
	private String filename = null;
	private Plugin plugin;
	private String subfolder = "";
	private boolean inPlugin;

	public Configs(Plugin plugin, String filename, boolean internalFile, String... subfolder) {
		this.plugin = plugin;
		this.filename = filename;
		this.subfolder = subfolder.length > 0 ? "plugins/" + plugin.getName() + "/" + subfolder[0] : "plugins/" + plugin.getName();
		inPlugin = internalFile;
		get().options().copyDefaults(true);
		save();
	}

	public FileConfiguration get() {
		if (config == null) {
			reload();
		}
		return config;
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			plugin.getLogger().log(Level.WARNING, "Failed to save File " + file.getName(), e);
		}
	}

	public void reload() {
		file = new File(subfolder, filename);
		config = YamlConfiguration.loadConfiguration(file);
		if(inPlugin) {
			InputStream dataStream = plugin.getResource(filename);
			if (dataStream != null) {
				config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(dataStream)));
			}
		}
	}
}
