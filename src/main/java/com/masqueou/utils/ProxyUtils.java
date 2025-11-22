package com.masqueou.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.masqueou.EasyLobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyUtils {

    private static final String TRANSFERRED_METADATA = "transferred";

    private final EasyLobby easyLobby;
    private final Map<UUID, BukkitTask> transferFallbackTasks = new ConcurrentHashMap<>();

    public ProxyUtils(final EasyLobby easyLobby) {
        this.easyLobby = easyLobby;
        Bukkit.getMessenger().registerOutgoingPluginChannel(easyLobby, "BungeeCord");
    }

    public void sendPlayerToServer(final Player player, final String serverName) {
        cancelTransferFallback(player);
        player.removeMetadata(TRANSFERRED_METADATA, easyLobby);

        if (serverName == null || serverName.trim().isEmpty()) {
            player.sendMessage(easyLobby.getColoredMessage("settings.prefix") + " " + easyLobby.getColoredMessage("messages.player_teleport_failed"));
            return;
        }

        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("Connect");
        byteArrayDataOutput.writeUTF(serverName);
        player.sendPluginMessage(easyLobby, "BungeeCord", byteArrayDataOutput.toByteArray());

        if (this.easyLobby.getBoolean("settings.message_on_teleport")){
            player.sendMessage(this.easyLobby.getColoredMessage("settings.prefix") + " " + this.easyLobby.getColoredMessage("messages.player_teleport"));
        }

        if (easyLobby.getConfig().getBoolean("settings.transfer-fallback.enabled", true)) {
            long fallbackDelayTicks = 20L * easyLobby.getConfig().getLong("settings.transfer-fallback.delay-seconds", 5L);
            BukkitTask fallbackTask = Bukkit.getScheduler().runTaskLater(easyLobby, () -> {
                transferFallbackTasks.remove(player.getUniqueId());
                if (player.isOnline() && !player.hasMetadata(TRANSFERRED_METADATA)) {
                    player.sendMessage(easyLobby.getColoredMessage("settings.prefix") + " " + easyLobby.getColoredMessage("messages.player_teleport_failed"));
                }
            }, fallbackDelayTicks);
            transferFallbackTasks.put(player.getUniqueId(), fallbackTask);
        }
    }

    public void markPlayerTransferred(final Player player) {
        player.setMetadata(TRANSFERRED_METADATA, new FixedMetadataValue(easyLobby, true));
        cancelTransferFallback(player);
    }

    public void cancelTransferFallback(final Player player) {
        BukkitTask existingTask = transferFallbackTasks.remove(player.getUniqueId());
        if (existingTask != null) {
            existingTask.cancel();
        }
    }
}
