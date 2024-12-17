package dev.jsinco.brewery.objects;

import com.dre.brewery.api.addons.AddonConfigFile;
import com.dre.brewery.configuration.annotation.OkaeriConfigFileOptions;
import com.dre.brewery.depend.okaeri.configs.annotation.Comment;
import com.dre.brewery.utility.Logging;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@OkaeriConfigFileOptions("plants.yml")
public class GardenManager extends AddonConfigFile {

    // TODO: This is bad, but I haven't opened up BreweryX's DataManager for supporting generic objects and paths yet,
    //  so it will have to do for now.
    @Comment("Persistent data for BreweryGarden plants. Don't touch.")
    private List<GardenPlant> gardenPlants = new ArrayList<>();


    @Nullable
    public GardenPlant getByID(UUID id) {
        if (id == null) return null;
        for (GardenPlant plant : gardenPlants) {
            if (plant.getId().equals(id)) {
                return plant;
            }
        }
        return null;
    }

    @Nullable
    public GardenPlant getByLocation(Block l) {
        if (l == null) return null;
        for (GardenPlant plant : gardenPlants) {
            if (plant.getRegion().getBlocks().contains(l)) {
                return plant;
            }
        }
        return null;
    }

    public void validatePlants() {
        gardenPlants.forEach(gardenPlant -> {
            if (!gardenPlant.isValid()) {
                removePlant(gardenPlant);
            }
        });
    }

    public void addPlant(GardenPlant plant) {
        Logging.debugLog("Added GardentPlant:" + plant);
        plant.place();
        gardenPlants.add(plant);
    }

    public void removePlant(GardenPlant plant) {
        Logging.debugLog("Removed GardentPlant:" + plant);
        plant.unPlace();
        gardenPlants.remove(plant);
    }
}
