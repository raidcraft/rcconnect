package de.raidcraft.connect;

import de.raidcraft.api.BasePlugin;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;
import net.minecraft.util.org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Dragonfire
 */
public class ConnectPlugin extends BasePlugin {

    @Override
    public void enable() {

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void disable() {
        //TODO: implement
    }

    public void send() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Forward"); // So BungeeCord knows to forward it
        out.writeUTF("ALL");
        out.writeUTF("Dungeon"); // The channel name to check if this your data

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF("Some kind of data here"); // You can do anything you want with msgout
            msgout.writeShort(123);
        } catch (IOException e) {
            e.printStackTrace();
        }


        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
    }
}
