package com.masqueou;

import com.masqueou.commands.LobbyCommand;
import com.masqueou.utils.Metrics;
import com.masqueou.utils.ProxyUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EasyLobby extends JavaPlugin {

    private FileConfiguration fileConfiguration;
    private ProxyUtils proxyUtils;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("--------------------------------------------");
        Bukkit.getLogger().info("EasyLobby by MasqueOu");
        Bukkit.getLogger().info("Hi ! EasyLobby is free and opensource.");
        Bukkit.getLogger().info("Thanks for using my plugin.");
        Bukkit.getLogger().info("--------------------------------------------");

        this.initStats();
        this.proxyUtils = new ProxyUtils(this);

        Objects.requireNonNull(getCommand("lobby")).setExecutor(new LobbyCommand(this));
        Objects.requireNonNull(getCommand("hub")).setExecutor(new LobbyCommand(this));

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfiguration();

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

    public void reloadConfiguration() {
        reloadConfig();
        fileConfiguration = getConfig();
    }

    public String getMessage(String path) {
        if(fileConfiguration.getString(path) != null) {
            return Objects.requireNonNull(fileConfiguration.getString(path)).replace("&", "§");
        } else {
            return "Hello, i found a problem in the configuration file. Reset the configuration and contact me if the problem persists. Error code: "+path;
        }
    }

    public ProxyUtils getProxyUtils() {
        return this.proxyUtils;
    }
}
