package de.raidcraft.connect.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.connect.ConnectPlugin;
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
        plugin.teleport((Player) sender, "dungeon");
    }
}
