package de.raidcraft.connect.tables;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import lombok.Getter;
import lombok.Setter;

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
@Table(name = "rc_connect_player")
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

        return encode(args);
    }

    public void setEncodedArags(String[] args) {

        this.args = decode(args);
    }

    public static String decode(String[] args) {

        return Joiner.on(splitter).join(args);
    }

    public static String[] encode(String args) {

        return Iterables.toArray(Splitter.on(splitter).split(args), String.class);
    }
}
