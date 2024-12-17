package dev.jsinco.brewery.objects;

import com.dre.brewery.api.addons.AddonConfigFile;
import com.dre.brewery.configuration.annotation.OkaeriConfigFileOptions;
import com.dre.brewery.depend.okaeri.configs.annotation.Comment;
import com.dre.brewery.utility.Logging;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@OkaeriConfigFileOptions("plants.yml")
public class GardenManager extends AddonConfigFile {

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
    public GardenPlant getByLocation(Location l) {
        if (l == null) return null;
        for (GardenPlant plant : gardenPlants) {
            if (plant.getRegion().contains(l.getX(), l.getY(), l.getZ())) {
                return plant;
            }
        }
        return null;
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
