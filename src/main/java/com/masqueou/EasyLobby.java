package com.masqueou;

import com.masqueou.commands.LobbyCommand;
import com.masqueou.listeners.TransferFallbackListener;
import com.masqueou.utils.ProxyUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.MultiLineChart;
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
        getLogger().info("--------------------------------------------");
        getLogger().info("EasyLobby by MasqueOu");
        getLogger().info("--------------------------------------------");

        this.initStats();
        this.proxyUtils = new ProxyUtils(this);

        Bukkit.getPluginManager().registerEvents(new TransferFallbackListener(this), this);

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

        metrics.addCustomChart(new MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));
    }

    @Override
    public void onDisable() {
        this.saveConfig();
        super.onDisable();
    }

    public void reloadConfiguration() {
        this.reloadConfig();
        this.fileConfiguration = this.getConfig();
    }

    public boolean getBoolean(String path) {
        if (this.fileConfiguration.contains(path)) {
            return this.fileConfiguration.getBoolean(path);
        }

        getLogger().warning("Missing configuration boolean at path: " + path);
        return false;
    }

    public String getString(String path) {
        if (this.fileConfiguration.getString(path) != null) {
            return Objects.requireNonNull(this.fileConfiguration.getString(path));
        }

        return "Missing configuration string at path: " + path;
    }

    public String getColoredMessage(String path) {
        return this.getString(path).replace("&", "§");
    }

    public ProxyUtils getProxyUtils() {
        return this.proxyUtils;
    }
}
