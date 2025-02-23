package dev.jsinco.brewery.garden.commands.subcomands;

import com.dre.brewery.utility.BUtil;
import com.dre.brewery.utility.Logging;
import dev.jsinco.brewery.garden.BreweryGarden;
import dev.jsinco.brewery.garden.GardenManager;
import dev.jsinco.brewery.garden.commands.AddonSubCommand;
import dev.jsinco.brewery.garden.configuration.BreweryGardenConfig;
import dev.jsinco.brewery.garden.objects.GardenPlant;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GrowthStageCommand implements AddonSubCommand {
    @Override
    public boolean execute(BreweryGarden addon, BreweryGardenConfig config, CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        int newGrowthStage = Math.min(BUtil.parseIntOrZero(args[0]), config.getFullyGrown());

        GardenManager gardenManager = BreweryGarden.getGardenManager();
        GardenPlant gardenPlant = gardenManager.getByLocation(player.getTargetBlockExact(30));

        if (gardenPlant == null) {
            Logging.msg(player, "No GardenPlant found.");
            return true;
        }

        gardenPlant.setAge(newGrowthStage);
        if (gardenPlant.isFullyGrown()) {
            gardenPlant.place();
        }
        return true;
    }

    @Override
    public List<String> tabComplete(BreweryGarden addon, CommandSender sender, String label, String[] args) {
        return List.of("1", "2", "3", "4");
    }

    @Override
    public String permission() {
        return "brewery.cmd.garden.growthstage";
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String usage(String label) {
        return "/" + label + "garden growthstage";
    }
}
