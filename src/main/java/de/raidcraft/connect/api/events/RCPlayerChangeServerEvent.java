package de.raidcraft.connect.api.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Dragonfire
 */
@Setter
@Getter
public class RCPlayerChangeServerEvent extends Event implements Cancellable {

    boolean cancelled = false;
    private Player player;
    private String oldServer;
    private String cause;
    private String[] args;

    public RCPlayerChangeServerEvent(Player player, String oldServer, String cause, String[] args) {

        this.player = player;
        this.cause = cause;
        this.args = args;
    }

    public boolean isInvalid() {

        return cause == null || oldServer == null;
    }

    // Bukkit stuff
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }
}