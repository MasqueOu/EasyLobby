package com.masqueou.listeners;

import com.masqueou.EasyLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class TransferFallbackListener implements Listener {
    private final EasyLobby easyLobby;

    public TransferFallbackListener(final EasyLobby easyLobby) {
        this.easyLobby = easyLobby;
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.easyLobby.getProxyUtils().markPlayerTransferred(event.getPlayer());
    }
}
