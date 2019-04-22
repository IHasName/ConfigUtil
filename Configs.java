import com.google.common.io.Files;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.channels.Channels;
import java.util.logging.Level;

/*
 *  Project: Utils in Configs
 *     by LikeWhat
 */

public class Configs {

    private FileConfiguration config = null;

    private File file = null;

    private boolean inPlugin;

    private String subfolder;
    private String filename;

    private Plugin plugin;

    public Configs(Plugin plugin, String filename, boolean internalFile, String... subfolder) {
        this.plugin = plugin;
        this.filename = filename;
        this.subfolder = subfolder.length > 0 ? "plugins/" + plugin.getName() + "/" + subfolder[0] : "plugins/" + plugin.getName();
        if (inPlugin = internalFile) {
            try {
                File outFile = new File(this.subfolder, filename);
                Files.createParentDirs(outFile);
                if (!outFile.exists()) {
                    try (InputStream fileInputStream = LobbyJump.getInstance().getResource(filename); FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
                        fileOutputStream.getChannel().transferFrom(Channels.newChannel(fileInputStream), 0, Integer.MAX_VALUE);
                    } catch (FileNotFoundException e) {
                        Bukkit.getLogger().log(Level.WARNING, "Failed to create File " + filename, e);
                    }
                }
            } catch(Exception e) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to create File " + filename, e);
            }
        }
        get().options().copyDefaults(true);
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
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save File " + file.getName(), e);
        }
    }

    public void reload() {
        file = new File(subfolder, filename);
        config = YamlConfiguration.loadConfiguration(file);
        if (inPlugin) {
            InputStream dataStream = plugin.getResource(filename);
            if (dataStream != null) {
                config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(dataStream)));
            }
        }
    }

    public File getFile() {
        if (file == null) {
            reload();
        }
        return file;
    }

}
