package com.masqueou.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.masqueou.EasyLobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProxyUtils {

    private final EasyLobby easyLobby;

    public ProxyUtils(final EasyLobby easyLobby) {
        this.easyLobby = easyLobby;
        Bukkit.getMessenger().registerOutgoingPluginChannel(easyLobby, "BungeeCord");
    }

    public void sendPlayerToServer(final Player player, final String serverName) {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("Connect");
        byteArrayDataOutput.writeUTF(serverName);
        player.sendPluginMessage(easyLobby, "BungeeCord", byteArrayDataOutput.toByteArray());

        Bukkit.getScheduler().runTaskLater(easyLobby, () -> {
            if(player.isOnline()){
                player.sendMessage(easyLobby.getMessage("main.prefix") + " " + easyLobby.getMessage("message.player_teleport_failed"));
            }
        }, 100L);
    }
}
