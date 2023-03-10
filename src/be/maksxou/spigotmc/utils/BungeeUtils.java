package be.maksxou.spigotmc.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import be.maksxou.spigotmc.EasyLobby;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BungeeUtils {

    private final EasyLobby easyLobby;

    public BungeeUtils(EasyLobby easyLobby) {
        this.easyLobby = easyLobby;
        Bukkit.getMessenger().registerOutgoingPluginChannel(easyLobby, "BungeeCord");
    }

    @SuppressWarnings("UnstableApiUsage")
    public void sendPlayerToServer(@NotNull final Player player, String serverName) {
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
