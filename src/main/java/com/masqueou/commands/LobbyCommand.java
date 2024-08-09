package com.masqueou.commands;

import com.masqueou.EasyLobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {

    private final EasyLobby easyLobby;

    public LobbyCommand(final EasyLobby easyLobby) {
        this.easyLobby = easyLobby;
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] strings) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            if (strings.length >= 1 && strings[0].equalsIgnoreCase("reload")) {
                this.performReload(player);
            } else {
                final String serverName = easyLobby.getMessage("server.lobby");
                this.performPlayer(player, serverName);
            }
            return true;
        } else {
            commandSender.sendMessage(easyLobby.getMessage("message.sender_is_not_player"));
            return false;
        }
    }

    private void performPlayer(final Player player, final String serverName) {
        if (Boolean.parseBoolean(this.easyLobby.getMessage("main.player_need_permission_to_teleport")) && (!player.hasPermission(this.easyLobby.getMessage("permission.lobby")) || !player.isOp())) {
            player.sendMessage(this.easyLobby.getMessage("main.prefix") + " " + this.easyLobby.getMessage("message.not_permission"));
            return;
        }

        if(Boolean.parseBoolean(this.easyLobby.getMessage("teleport.message_on_teleport"))){
            player.sendMessage(this.easyLobby.getMessage("main.prefix") + " " + this.easyLobby.getMessage("message.player_teleport"));
        }
        this.easyLobby.getProxyUtils().sendPlayerToServer(player, serverName);
    }

    private void performReload(final Player player) {
        if (!player.hasPermission(this.easyLobby.getMessage("permission.reload_plugin")) || !player.isOp()) {
            player.sendMessage(this.easyLobby.getMessage("main.prefix") + " " + this.easyLobby.getMessage("message.not_permission"));
            return;
        }

        this.easyLobby.reloadConfiguration();
        player.sendMessage(this.easyLobby.getMessage("main.prefix") + " " + this.easyLobby.getMessage("message.success_reloading_config"));
    }
}
