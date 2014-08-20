package de.raidcraft.connect.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.connect.ConnectPlugin;
import de.raidcraft.connect.tables.TDungeonPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Dragonfire
 */
public class DungeonConnect {

    private final ConnectPlugin plugin;

    public DungeonConnect(ConnectPlugin module) {

        this.plugin = module;
    }

    @Command(
            aliases = {"test"},
            desc = "test"
    )
    @CommandPermissions("connect.dungeon.test")
    public void test(CommandContext context, CommandSender sender) throws CommandException {

        Player player = (Player) sender;
        TDungeonPlayer tplayer = plugin.getDatabase().find(TDungeonPlayer.class)
                .where().eq("player_id", player.getUniqueId().toString()).findUnique();
        if (tplayer != null) {
            sender.sendMessage("Player already in an instance");
            return;
        }
        tplayer = new TDungeonPlayer();
        tplayer.init(player);
        tplayer.setLastInstance("w");
        plugin.getDatabase().save(tplayer);
        plugin.teleport((Player) sender, "dungeon");
    }
}
