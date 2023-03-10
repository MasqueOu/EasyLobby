package be.maksxou.spigotmc;

import be.maksxou.spigotmc.commands.LobbyCommand;
import be.maksxou.spigotmc.utils.BungeeUtils;
import be.maksxou.spigotmc.utils.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EasyLobby extends JavaPlugin {

    private FileConfiguration fileConfiguration;

    private BungeeUtils bungeeUtils;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("--------------------------------------------");
        Bukkit.getLogger().info("EasyLobby by MasqueOù");
        Bukkit.getLogger().info("Hi ! EasyLobby is free and opensource.");
        Bukkit.getLogger().info("Thanks for using my plugin.");
        Bukkit.getLogger().info("--------------------------------------------");

        initStats();
        bungeeUtils = new BungeeUtils(this);

        Objects.requireNonNull(getCommand("lobby")).setExecutor(new LobbyCommand(this));
        Objects.requireNonNull(getCommand("hub")).setExecutor(new LobbyCommand(this));

        getConfig().options().copyDefaults(true);
        saveConfig();

        rlConfig();
        super.onEnable();
    }

    private void initStats() {
        int pluginId = 17905;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new Metrics.MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));
    }

    @Override
    public void onDisable() {
        saveConfig();
        super.onDisable();
    }

    public void rlConfig() {
        reloadConfig();
        fileConfiguration = null;

        fileConfiguration = getConfig();
    }

    public String getMessage(String s) {
        if(fileConfiguration.get(s) != null) {
            return Objects.requireNonNull(fileConfiguration.get(s)).toString().replace("&", "§");
        } else {
            return "Hello, i found a problem in the configuration file. Reset the configuration and contact me if the problem persists. Error code: "+s;
        }
    }

    public BungeeUtils getBungeeUtils() {
        return bungeeUtils;
    }
}
