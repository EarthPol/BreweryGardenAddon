package dev.jsinco.brewery.garden.events;

import com.dre.brewery.utility.Logging;
import dev.jsinco.brewery.garden.BreweryGarden;
import dev.jsinco.brewery.garden.GardenManager;
import dev.jsinco.brewery.garden.configuration.BreweryGardenConfig;
import dev.jsinco.brewery.garden.constants.PlantPart;
import dev.jsinco.brewery.garden.constants.PlantType;
import dev.jsinco.brewery.garden.constants.PlantTypeSeeds;
import dev.jsinco.brewery.garden.objects.GardenPlant;
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
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class EventListeners implements Listener {

    private static final Random RANDOM = new Random();
    
    
    private final BreweryGardenConfig config = BreweryGarden.getInstance().getAddonConfigManager().getConfig(BreweryGardenConfig.class);
    private final GardenManager gardenManager;

    public EventListeners(GardenManager gardenManager) {
        this.gardenManager = gardenManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onLeafDecay(LeavesDecayEvent event) {
        GardenPlant gardenPlant = gardenManager.getByLocation(event.getBlock());
        if (gardenPlant != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (config.getValidSeedDropBlocks().contains(block.getType()) && RANDOM.nextInt(100) <= config.getSeedSpawnChance()) {
            List<PlantTypeSeeds> seedsList = PlantTypeSeeds.values();
            // Drop a seed
            block.getWorld().dropItemNaturally(block.getLocation(),
                    seedsList.get(RANDOM.nextInt(seedsList.size())).getItemStack(1));
        }

        GardenPlant gardenPlant = gardenManager.getByLocation(block);
        if (gardenPlant == null) {
            return;
        }

        Logging.debugLog("Found a GardenPlant at Location for BlockBreak: " + block.getLocation());
        // GardenPlant#isValid won't work here. Block's material type won't update until after this event has finished firing.
        Material itemMaterial = event.getPlayer().getInventory().getItemInMainHand().getType();
        if (block.getType() == Material.PLAYER_HEAD) { // Just gonna do this for now
            if (itemMaterial != Material.SHEARS) {
                Logging.msg(event.getPlayer(), "&rThis plant needs to be interacted with &6shears &rto be obtained.");
            }
            event.setCancelled(true);
        } else {
            gardenManager.removePlant(gardenPlant);
        }
    }

    // TODO: I'm not going to put debug statements, gonna need IJ debugger for this test
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        handlePlantShearing(event.getItem(), block);
        if (event.getBlockFace() == BlockFace.UP && event.getAction().isRightClick() && config.getPlantableBlocks().contains(block.getType())) {
            event.setCancelled(handleSeedPlacement(event.getItem(), block));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (PlantType.isPlant(event.getItemInHand())) {
            event.setCancelled(true);
        }
    }


    private void handlePlantShearing(ItemStack itemInHand, Block clickedBlock) {
        if (itemInHand == null || itemInHand.getType() != Material.SHEARS) {
            return;
        }

        Location clickedLocation = clickedBlock.getLocation();

        GardenPlant gardenPlant = gardenManager.getByLocation(clickedBlock);
        if (gardenPlant == null || gardenPlant.getPlantPart(clickedLocation) != PlantPart.TOP || !gardenPlant.isValid()) {
            return;
        }
        gardenPlant.resetGrowthStage(true);
        clickedLocation.getWorld().playSound(clickedLocation, Sound.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
    }

    private boolean handleSeedPlacement(ItemStack itemInHand, Block clickedBlock) {
        if (!PlantTypeSeeds.isSeeds(itemInHand)) {
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

        itemInHand.setAmount(itemInHand.getAmount() - 1);
        location.getWorld().playSound(location, Sound.BLOCK_GRASS_PLACE, 1.0f, 1.0f);
        return true;
    }
}
