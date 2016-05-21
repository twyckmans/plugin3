package be.polfredo.pluginBoS;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

/**
 * Created by Polfredo on 21-05-16.
 */

public class EndTask extends BukkitRunnable {

    Main plugin;

    @Override
    public void run() {
        plugin.endGame();
    }
}
