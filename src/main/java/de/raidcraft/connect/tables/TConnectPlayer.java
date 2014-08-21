package de.raidcraft.connect.tables;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.com.google.common.base.Joiner;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author Silthus
 */
@Getter
@Setter
@Entity
@Table(name = "connect_player")
public class TConnectPlayer {

    private final static String splitter = "|~|";
    @Id
    private int id;
    private UUID player;
    private String newServer;
    private String oldServer;
    private String cause;
    private String args;

    public String[] getEncodedArgs() {

        return args.split(splitter);
    }

    public void setEncodedArags(String[] args) {

        this.args = Joiner.on(splitter).join(args);
    }
}
