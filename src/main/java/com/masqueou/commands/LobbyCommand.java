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
            if (strings.length == 0) {
                final String serverName = easyLobby.getString("settings.server.lobby");
                this.performPlayer(player, serverName);
                return true;
            } else if (strings.length == 1 && strings[0].equalsIgnoreCase("reload")) {
                this.performReload(player);
                return true;
            } else {
                commandSender.sendMessage(easyLobby.getColoredMessage("messages.command_doesnt_exist"));
                return false;
            }
        }
        commandSender.sendMessage(easyLobby.getColoredMessage("messages.sender_is_not_a_player"));
        return false;
    }

    private void performPlayer(final Player player, final String serverName) {
        if (this.easyLobby.getBoolean("settings.player_need_permission_to_teleport")
                && !(player.isOp() || player.hasPermission(this.easyLobby.getString("settings.permission.lobby")))) {
            player.sendMessage(this.easyLobby.getColoredMessage("settings.prefix") + " " + this.easyLobby.getColoredMessage("messages.not_enough_permission"));
            return;
        }

        this.easyLobby.getProxyUtils().sendPlayerToServer(player, serverName);
    }

    private void performReload(final Player player) {
        if (this.easyLobby.getBoolean("settings.permission.reload_plugin")
                && !(player.isOp() || player.hasPermission(this.easyLobby.getString("permission.lobby")))) {
            player.sendMessage(this.easyLobby.getColoredMessage("settings.prefix") + " " + this.easyLobby.getColoredMessage("messages.not_enough_permission"));
            return;
        }

        this.easyLobby.reloadConfiguration();
        player.sendMessage(this.easyLobby.getColoredMessage("settings.prefix") + " " + this.easyLobby.getColoredMessage("messages.config_reloaded"));
    }
}
