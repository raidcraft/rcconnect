package de.raidcraft.connect;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.config.Comment;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.api.config.MultiComment;
import de.raidcraft.api.config.Setting;
import de.raidcraft.connect.api.events.RCPlayerChangeServerEvent;
import de.raidcraft.connect.commands.DungeonConnect;
import de.raidcraft.connect.listeners.ServerSwitchListener;
import de.raidcraft.connect.tables.TConnectPlayer;
import lombok.Getter;
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
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        registerCommands(BaseCommands.class);
        if (config.teleportIn) {
            registerEvents(new ServerSwitchListener(this));
        }
    }

    @Override
    public void disable() {
        //TODO: implement
    }

    @Override
    public void reload() {

        config.reload();
    }

    public void send(String newServer, String cause, String[] args, Player... players) {

        for (Player player : players) {
            TConnectPlayer tPlayer = new TConnectPlayer();
            tPlayer.setOldServer(config.serverName);
            tPlayer.setCause(cause);
            tPlayer.setEncodedArags(args);
            tPlayer.setNewServer(newServer);
            tPlayer.setPlayer(player.getUniqueId());
            getDatabase().save(tPlayer);
        }
        for (Player player : players) {
            teleport(player, newServer);
        }
    }

    public void teleportBack(Player player) {

        teleport(player, getConfig().mainServerName);
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
                aliases = {"rccd", "cdungeon", "::d"},
                desc = "rcconnect dungeon"
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
        @Comment("The current servername in the bunggee config")
        @Setting("connect.bungee-servername")
        public String serverName = "Please enter here bungee server name";
        @Comment("The bungee dungeon servername")
        @Setting("connect.dungeon.servername")
        public String dungeonServerName = "dungeon";
        @Comment("The bungee main servername")
        @Setting("connect.main.servername")
        public String mainServerName = "main";
        @Comment("Seconds before player port back, if to fast it maybe ignored")
        @Setting("connect.port-back-seconds")
        public int portBackSeconds = 1;
    }

    public List<TConnectPlayer> getSimilarPlayerIds(RCPlayerChangeServerEvent event) {

        return getDatabase().find(TConnectPlayer.class).where()
                .eq("new_server", getConfig().serverName)
                .eq("cause", event.getCause())
                .eq("args", TConnectPlayer.decode(event.getArgs())).findList();
    }
}
