package de.raidcraft.connect.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.connect.ConnectPlugin;
import de.raidcraft.util.CommandUtil;
import de.raidcraft.util.MathUtil;
import de.raidcraft.util.PlayerUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dragonfire
 */
public class DungeonConnect {

    private final ConnectPlugin plugin;
    public final static String START_INSTACE = "dungeon.start";


    public DungeonConnect(ConnectPlugin module) {

        this.plugin = module;
    }

    @Command(
            aliases = {"start"},
            desc = "start a dzbgeon",
            min = 2,
            usage = "[dungeon_name] [radius_around_player] (player)"
    )
    @CommandPermissions("connect.dungeon.start")
    public void start(CommandContext context, CommandSender sender) throws CommandException {

        Player player;
        String dungeonName = context.getString(0);
        int radius = context.getInteger(1, 0);
        if (context.argsLength() == 3) {
            player = CommandUtil.grabPlayer(context.getString(2));
        } else {
            player = (Player) sender;
        }
        // 0: dungeon name; 1-2: to identify all players of the group
        String[] args = new String[]{dungeonName, new Date() + "", MathUtil.RANDOM.nextInt() + ""};
        List<Player> uuids = PlayerUtil.getPlayerNearby(player, radius)
                .stream().collect(Collectors.toList());
        plugin.send(plugin.getConfig().dungeonServerName, START_INSTACE, args,
                uuids.toArray(new Player[uuids.size()]));
    }
}
