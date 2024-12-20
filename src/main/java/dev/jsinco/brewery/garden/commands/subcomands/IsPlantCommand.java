package dev.jsinco.brewery.garden.commands.subcomands;

import com.dre.brewery.utility.BUtil;
import com.dre.brewery.utility.Logging;
import dev.jsinco.brewery.garden.BreweryGarden;
import dev.jsinco.brewery.garden.GardenManager;
import dev.jsinco.brewery.garden.commands.AddonSubCommand;
import dev.jsinco.brewery.garden.objects.GardenPlant;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class IsPlantCommand implements AddonSubCommand {
    @Override
    public boolean execute(BreweryGarden addon, CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        GardenManager gardenManager = BreweryGarden.getGardenManager();

        int maxDistance = 30;
        if (args.length > 0) {
            maxDistance = BUtil.parseInt(args[0]);
        }

        GardenPlant gardenPlant = gardenManager.getByLocation(player.getTargetBlockExact(maxDistance));
        if (gardenPlant != null) {
            Logging.msg(player, "Found a GardenPlant: " + gardenPlant);
        } else {
            Logging.msg(player, "No GardenPlant found.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(BreweryGarden addon, CommandSender sender, String label, String[] args) {
        return null;
    }

    @Override
    public String permission() {
        return "brewery.cmd.garden.isplant";
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String usage(String label) {
        return "/" + label + " isplant <distance?>";
    }
}
