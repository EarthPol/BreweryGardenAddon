package dev.jsinco.brewery.events;

import com.dre.brewery.utility.Logging;
import dev.jsinco.brewery.constants.PlantType;
import dev.jsinco.brewery.constants.PlantTypeSeeds;
import dev.jsinco.brewery.objects.GardenManager;
import dev.jsinco.brewery.objects.GardenPlant;
import dev.jsinco.brewery.constants.PlantPart;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class EventListeners implements Listener {

    private static final Random RANDOM = new Random();

    private final GardenManager gardenManager;

    public EventListeners(GardenManager gardenManager) {
        this.gardenManager = gardenManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.SHORT_GRASS) {
            List<PlantTypeSeeds> seedsList = PlantTypeSeeds.values();
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),
                    seedsList.get(RANDOM.nextInt(seedsList.size())).getItemStack(1));
            return;
        }

        GardenPlant gardenPlant = gardenManager.getByLocation(event.getBlock().getLocation());
        if (gardenPlant == null) {
            return;
        }

        Logging.debugLog("Found a GardenPlant at Location: " + event.getBlock().getLocation());

        if (!gardenPlant.isValid()) {
            gardenManager.removePlant(gardenPlant);
        }
    }

    // TODO: I'm not going to put debug statements, gonna need IJ debugger for this test
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        handlePlantShearing(event.getItem(), event.getClickedBlock());
        if (event.getBlockFace() == BlockFace.UP) {
            event.setCancelled(handleSeedPlacement(event.getItem(), event.getClickedBlock()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (PlantType.isPlant(event.getItemInHand())) {
            event.setCancelled(true);
        }
    }


    private void handlePlantShearing(ItemStack itemInHand, Block clickedBlock) {
        if (itemInHand == null || itemInHand.getType() != Material.SHEARS || clickedBlock == null  ) {
            return;
        }

        Location clickedLocation = clickedBlock.getLocation();

        GardenPlant gardenPlant = gardenManager.getByLocation(clickedLocation);
        if (gardenPlant == null || gardenPlant.getPlantPart(clickedLocation) != PlantPart.TOP || !gardenPlant.isValid()) {
            return;
        }
        gardenPlant.resetGrowthStage(true);
    }

    private boolean handleSeedPlacement(ItemStack itemInHand, Block clickedBlock) {
        if (!PlantTypeSeeds.isSeeds(itemInHand) || clickedBlock == null) {
            return false;
        }

        Location location = clickedBlock.getLocation().add(0, 1, 0); // Need the block above

        PlantTypeSeeds seeds = PlantTypeSeeds.getPlantSeedsType(itemInHand);
        if (seeds == null) {
            return false;
        }
        // Create a new GardenPlant at the location
        GardenPlant gardenPlant = new GardenPlant(seeds.getParent(), location);
        gardenManager.addPlant(gardenPlant);

        location.getWorld().playSound(location, Sound.BLOCK_GRASS_PLACE, 1.0f, 1.0f);
        return true;
    }
}
