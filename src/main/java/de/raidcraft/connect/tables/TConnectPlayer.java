package de.raidcraft.connect.tables;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.com.google.common.base.Joiner;
import net.minecraft.util.com.google.common.base.Splitter;
import net.minecraft.util.com.google.common.collect.Iterables;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * @author Silthus
 */
@Getter
@Setter
@Entity
@Table(name = "connect_player")
public class TConnectPlayer {

    @Transient
    private final static String splitter = "|~|";
    @Id
    private int id;
    private UUID player;
    private String newServer;
    private String oldServer;
    private String cause;
    private String args;

    public String[] getEncodedArgs() {

        return Iterables.toArray(Splitter.on(splitter).split(args), String.class);
    }

    public void setEncodedArags(String[] args) {

        this.args = Joiner.on(splitter).join(args);
    }
}