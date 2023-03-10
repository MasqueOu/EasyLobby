package be.maksxou.spigotmc.commands;

import be.maksxou.spigotmc.EasyLobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {

    private final EasyLobby easyLobby;

    public LobbyCommand(EasyLobby easyLobby) {
        this.easyLobby = easyLobby;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(easyLobby.getMessage("message.sender_is_not_player"));
            return false;
        }

        Player player = (Player) sender;

        if(args.length == 0){
            String bungeeLobbyName = easyLobby.getMessage("server.lobby");
            sendToServer(player, bungeeLobbyName);
            return false;
        } else if(args.length == 1){
            reloadConfiguration(player);
            return false;
        } else {
            player.sendMessage(easyLobby.getMessage("main.prefix") + " " + easyLobby.getMessage("message.command_doesnt_exist"));
            return false;
        }
    }

    private void sendToServer(Player player, String serverName){
        if(Boolean.parseBoolean(easyLobby.getMessage("main.player_need_permission_to_teleport")) && !player.hasPermission(easyLobby.getMessage("permission.lobby")) || !player.isOp()){
            player.sendMessage(easyLobby.getMessage("main.prefix") + " " + easyLobby.getMessage("message.not_permission"));
        } else {
            if(Boolean.parseBoolean(easyLobby.getMessage("teleport.message_on_teleport"))){
                player.sendMessage(easyLobby.getMessage("main.prefix") + " " + easyLobby.getMessage("message.player_teleport"));
            }
            easyLobby.getBungeeUtils().sendPlayerToServer(player, serverName);
        }
    }

    private void reloadConfiguration(Player player) {
        if(player.hasPermission(easyLobby.getMessage("permission.reload_plugin")) || player.isOp()) {
            easyLobby.rlConfig();
            player.sendMessage(easyLobby.getMessage("main.prefix") + " " + easyLobby.getMessage("message.success_reloading_config"));
        } else {
            player.sendMessage(easyLobby.getMessage("main.prefix") + " " + easyLobby.getMessage("message.not_permission"));
        }
    }
}
