package de.raidcraft.connect;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.connect.commands.DungeonConnect;
import de.raidcraft.connect.tables.TDungeon;
import de.raidcraft.connect.tables.TDungeonInstance;
import de.raidcraft.connect.tables.TDungeonInstancePlayer;
import de.raidcraft.connect.tables.TDungeonPlayer;
import de.raidcraft.connect.tables.TDungeonSpawn;
import net.minecraft.util.com.google.common.collect.Iterables;
import net.minecraft.util.com.google.common.io.ByteArrayDataInput;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;
import net.minecraft.util.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.persistence.PersistenceException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dragonfire
 */
public class ConnectPlugin extends BasePlugin implements PluginMessageListener {

    @Override
    public void enable() {

        registerCommands(BaseCommands.class);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    @Override
    public void disable() {
        //TODO: implement
    }

    @Deprecated
    // problem: cannot send oommand to empty server
    public void send(String id, String... args) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Forward"); // So BungeeCord knows to forward it
        out.writeUTF("ALL");
        out.writeUTF("Connect"); // The channel name to check if this your data

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(id); // You can do anything you want with msgout
            for (String arg : args) {
                msgout.writeUTF(arg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        Iterables.getFirst(Bukkit.getOnlinePlayers(), null)
                .sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("Connect")) {
            // Use the code sample in the 'Response' sections below to read
            // the data.
            Bukkit.broadcastMessage("send receive something");
        }
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
        tables.add(TDungeon.class);
        tables.add(TDungeonInstance.class);
        tables.add(TDungeonInstancePlayer.class);
        tables.add(TDungeonPlayer.class);
        tables.add(TDungeonSpawn.class);
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
}
