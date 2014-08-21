package de.raidcraft.connect;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.api.config.MultiComment;
import de.raidcraft.api.config.Setting;
import de.raidcraft.connect.commands.DungeonConnect;
import de.raidcraft.connect.listeners.ServerSwitchListener;
import de.raidcraft.connect.tables.TConnectPlayer;
import lombok.Getter;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dragonfire
 */
public class ConnectPlugin extends BasePlugin {

    @Getter
    private LocalConfiguration config;

    @Override
    public void enable() {

        config = configure(new LocalConfiguration(this));
        registerCommands(BaseCommands.class);
        if (config.teleportIn) {
            registerEvents(new ServerSwitchListener(this));
        }

        //        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void disable() {
        //TODO: implement
    }

    public void teleport(Player player, String server) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {

        List<Class<?>> tables = new ArrayList<>();
        tables.add(TConnectPlayer.class);
        return tables;
    }

    private void setupDatabase() {

        try {
            getDatabase();
        } catch (PersistenceException e) {
            e.printStackTrace();
            getLogger().warning("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    public class BaseCommands {

        @Command(
                aliases = {"rcc", "connect"},
                desc = "rcconnect"
        )
        @NestedCommand(value = DungeonConnect.class)
        public void dungeon(CommandContext args, CommandSender sender) throws CommandException {

        }
    }

    public static class LocalConfiguration extends ConfigurationBase<ConnectPlugin> {


        public LocalConfiguration(ConnectPlugin plugin) {

            super(plugin, "config.yml");
        }

        @MultiComment({"true: server send custom events if players join server",
                "event can be cancelled and players port back to old Server"})
        @Setting("connect.teleport-in")
        public boolean teleportIn = false;
    }
}
