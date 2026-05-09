package com.masqueou.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.masqueou.EasyLobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProxyUtils {

    private final EasyLobby easyLobby;
    private final Map<UUID, BukkitTask> transferFallbackTasks = new HashMap<>();

    public ProxyUtils(final EasyLobby easyLobby) {
        this.easyLobby = easyLobby;
        Bukkit.getMessenger().registerOutgoingPluginChannel(easyLobby, "BungeeCord");
    }

    public void sendPlayerToServer(final Player player, final String serverName) {
        final UUID playerUniqueId = player.getUniqueId();
        cancelTransferFallback(playerUniqueId);

        if (serverName == null || serverName.trim().isEmpty()) {
            player.sendMessage(this.easyLobby.getColoredMessage("settings.prefix") + " " + this.easyLobby.getColoredMessage("messages.player_teleport_failed"));
            return;
        }

        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("Connect");
        byteArrayDataOutput.writeUTF(serverName);
        player.sendPluginMessage(easyLobby, "BungeeCord", byteArrayDataOutput.toByteArray());

        if (this.easyLobby.getBoolean("settings.message_on_teleport")) {
            player.sendMessage(this.easyLobby.getColoredMessage("settings.prefix") + " " + this.easyLobby.getColoredMessage("messages.player_teleport"));
        }

        if (this.easyLobby.getConfig().getBoolean("settings.transfer-fallback.enabled", true)) {
            BukkitTask fallbackTask = Bukkit.getScheduler().runTaskLater(this.easyLobby, () -> {
                transferFallbackTasks.remove(playerUniqueId);

                Player onlinePlayer = Bukkit.getPlayer(playerUniqueId);
                if (onlinePlayer != null && onlinePlayer.isOnline()) {
                    onlinePlayer.sendMessage(this.easyLobby.getColoredMessage("settings.prefix") + " " + this.easyLobby.getColoredMessage("messages.player_teleport_failed"));
                }
            }, 20L * this.easyLobby.getConfig().getLong("settings.transfer-fallback.delay-seconds", 5L));
            transferFallbackTasks.put(playerUniqueId, fallbackTask);
        }
    }

    public void cancelTransferFallback(final UUID playerUniqueId) {
        BukkitTask existingTask = transferFallbackTasks.remove(playerUniqueId);
        if (existingTask != null) {
            existingTask.cancel();
        }
    }
}
