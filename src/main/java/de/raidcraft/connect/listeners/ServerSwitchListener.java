package de.raidcraft.connect.listeners;

import de.raidcraft.RaidCraft;
import de.raidcraft.connect.ConnectPlugin;
import de.raidcraft.connect.api.raidcraftevents.RE_PlayerSwitchServer;
import de.raidcraft.connect.tables.TConnectPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Dragonfire
 */
public class ServerSwitchListener implements Listener {

    private ConnectPlugin plugin;

    public ServerSwitchListener(ConnectPlugin plugin) {

        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void join(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        TConnectPlayer tPlayer = plugin.getDatabase().find(TConnectPlayer.class)
                .where().eq("new_server", plugin.getConfig().serverName)
                .eq("player", player.getUniqueId()).findUnique();
        RE_PlayerSwitchServer switchEvent =
                new RE_PlayerSwitchServer(player, null, null, null);
        if (tPlayer != null) {
            switchEvent.setArgs(tPlayer.getEncodedArgs());
            switchEvent.setCause(tPlayer.getCause());
            switchEvent.setOldServer(tPlayer.getOldServer());
        }
        RaidCraft.callEvent(switchEvent);
        if (switchEvent.isCancelled()) {
            if (switchEvent.getOldServer() == null) {
                plugin.getLogger().warning("Port back server is null of player ("
                        + player.getName() + ") ");
            } else {
                plugin.teleport(player, switchEvent.getOldServer());
            }
        }
        if (tPlayer != null) {
            plugin.getDatabase().delete(tPlayer);
        }
    }

}
