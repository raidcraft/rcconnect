package de.raidcraft.connect.listeners;

import de.raidcraft.RaidCraft;
import de.raidcraft.connect.ConnectPlugin;
import de.raidcraft.connect.api.events.RCPlayerChangeServerEvent;
import de.raidcraft.connect.tables.TConnectPlayer;
import de.raidcraft.util.TimeUtil;
import org.bukkit.Bukkit;
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
                .eq("player", player.getUniqueId()).findOne();
        RCPlayerChangeServerEvent switchEvent =
                new RCPlayerChangeServerEvent(player, null, null, null);
        if (tPlayer != null) {
            switchEvent.setArgs(tPlayer.getEncodedArgs());
            switchEvent.setCause(tPlayer.getCause());
            switchEvent.setOldServer(tPlayer.getOldServer());
        }
        try {
            RaidCraft.callEvent(switchEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (switchEvent.isCancelled()) {
            if (switchEvent.getOldServer() == null) {
                plugin.getLogger().warning("Port back server is null of player ("
                        + player.getName() + ") ");
            } else {
                // delay port back on next tick, otherwise it is to fast
                Bukkit.getScheduler().runTaskLater(plugin, () ->
                                plugin.teleport(player, switchEvent.getOldServer()),
                        TimeUtil.secondsToTicks(plugin.getConfig().portBackSeconds));
            }
        }
        if (tPlayer != null) {
            plugin.getDatabase().delete(tPlayer);
        }
    }

}
