package dev.jsinco.brewery.objects;

import com.dre.brewery.utility.Logging;
import dev.jsinco.brewery.constants.PlantPart;
import dev.jsinco.brewery.constants.PlantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class GardenPlant {

    // Yes, yes. I know, schematics would be the best way to do this.
    // If you're reading this, maybe submit a PR? ^ :)

    private static final Random RANDOM = new Random();

    private final UUID id;
    private final PlantType type;
    private final WorldTiedBoundingBox region;
    private int age;

    public GardenPlant(PlantType type, Location bottomLocation) {
        this(type, WorldTiedBoundingBox.of(bottomLocation, bottomLocation.clone().add(0, 2, 0)));
    }

    public GardenPlant(PlantType type, WorldTiedBoundingBox region) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.region = region;
        this.age = 0;
    }

    // Plants are:
    // BOTTOM = OAK_FENCE
    // MIDDLE = OAK_LEAVES
    // TOP = PLANT HEAD
    public boolean place() {
        for (var plantPart : PlantPart.values()) {
            if (plantPart == PlantPart.TOP) {
                if (!this.isFullyGrown()) {
                    this.age++;
                    continue;
                }
                this.age = 0;
            }

            Location location = plantPart.locationFromCenter(region.getCenter(), region.getWorld());
            Block block = location.getBlock();
            if (block.getType() != Material.AIR) {
                continue;
            }

            block.setType(plantPart.getAssigneeMaterial());
            if (block.getBlockData() instanceof Leaves leaves) {
                leaves.setPersistent(true); // Either this doesn't work or it has a misleading name
            }
            if (plantPart.getAssigneeMaterial() == Material.PLAYER_HEAD) {
                type.setSkullTexture(block);
            }
        }
        return true;
    }

    @Nullable
    public PlantPart getPlantPart(Location location) {
        for (PlantPart part : PlantPart.values()) {
            Location locationFromCenter = part.locationFromCenter(region.getCenter(), region.getWorld());
            Logging.debugLog("Checking if " + locationFromCenter + " is equal to " + location);
            if (locationFromCenter.equals(location)) {
                return part;
            }
        }
        return null;
    }



    public boolean unPlace() {
        for (Block block : region.getBlocks()) {
            block.setType(Material.AIR);
        }
        return true;
    }


    public boolean isValid() {
        for (PlantPart part : PlantPart.values()) {
            if (part == PlantPart.TOP) {
                continue; // This could be air or a PlayerHead
            }

            Location location = part.locationFromCenter(region.getCenter(), region.getWorld());
            Block block = location.getBlock();
            if (block.getType() != part.getAssigneeMaterial()) {
                return false;
            }
        }
        return true;
    }


    public boolean isFullyGrown() {
        return this.age >= 4;
    }

    public void incrementGrowthStage(int amount) {
        this.age += amount;
    }

    public void resetGrowthStage(boolean dropPlantItem) {
        this.age = 0;
        Location location = PlantPart.TOP.locationFromCenter(region.getCenter(), region.getWorld());
        location.getBlock().setType(Material.AIR);

        if (dropPlantItem) {
            // Drop between 1-3 of the ItemStack
            location.getWorld().dropItemNaturally(location, this.type.getItemStack(RANDOM.nextInt(1, 4)));
        }
    }
}
