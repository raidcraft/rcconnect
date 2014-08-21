package de.raidcraft.connect.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.connect.ConnectPlugin;
import de.raidcraft.util.MathUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

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

        String[] args = new String[]{"w", new Date() + "", MathUtil.RANDOM.nextInt() + ""};
        plugin.send("dungeon", "dungeon.start", args, (Player) sender);
    }
}
