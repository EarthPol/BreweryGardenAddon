package dev.jsinco.brewery.commands.subcomands;

import com.dre.brewery.utility.Logging;
import dev.jsinco.brewery.BreweryGarden;
import dev.jsinco.brewery.commands.AddonSubCommand;
import dev.jsinco.brewery.constants.PlantType;
import dev.jsinco.brewery.constants.PlantTypeSeeds;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiveCommand implements AddonSubCommand {

    private final Map<String, ItemStack> items = new HashMap<>();
    {
        for (var plant : PlantType.values()) {
            items.put(plant.name().toLowerCase(), plant.getItemStack(1));
        }
        for (var seed : PlantTypeSeeds.values()) {
            items.put(seed.name().toLowerCase(), seed.getItemStack(1));
        }
    }

    @Override
    public boolean execute(BreweryGarden addon, CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        Player player = null;
        if (args.length >= 2) {
            player = Bukkit.getPlayerExact(args[1]);
        } else if (sender instanceof Player p) {
            player = p;
        }

        if (player == null) {
            return false;
        }

        ItemStack item = items.get(args[0].toLowerCase());
        if (item != null) {
            player.getInventory().addItem(item);
            Logging.msg(sender, "Gave " + item.getType().name() + " to " + player.getName());
        } else {
            Logging.msg(sender, "Unknown item.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(BreweryGarden addon, CommandSender sender, String label, String[] args) {
        return items.keySet().stream().toList();
    }

    @Override
    public String permission() {
        return "brewery.cmd.garden.give";
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String usage(String label) {
        return "&e/" + label + " give <item!> <player?>";
    }
}
