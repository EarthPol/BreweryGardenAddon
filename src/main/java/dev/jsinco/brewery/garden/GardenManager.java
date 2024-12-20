package dev.jsinco.brewery.garden;

import com.dre.brewery.storage.DataManager;
import com.dre.brewery.utility.Logging;
import dev.jsinco.brewery.garden.objects.GardenPlant;
import dev.jsinco.brewery.garden.persist.AutoSavableGardenPlant;
import dev.jsinco.brewery.garden.persist.SerializableGardenPlant;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GardenManager {

    private List<GardenPlant> gardenPlants = new ArrayList<>();
    private AutoSavableGardenPlant autoSavableInstance;

    public GardenManager(DataManager dataManager) {
        autoSavableInstance = new AutoSavableGardenPlant(this);


        List<SerializableGardenPlant> serializableGardenPlants = dataManager.getAllGeneric("gardenplants", SerializableGardenPlant.class);
        for (SerializableGardenPlant serializableGardenPlant : serializableGardenPlants) {
            gardenPlants.add(serializableGardenPlant.toGardenPlant());
        }
        dataManager.registerAutoSavable(autoSavableInstance);
    }


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
