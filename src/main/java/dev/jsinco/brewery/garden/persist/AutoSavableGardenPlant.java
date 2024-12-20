package dev.jsinco.brewery.garden.persist;

import com.dre.brewery.storage.DataManager;
import com.dre.brewery.storage.interfaces.ExternallyAutoSavable;
import dev.jsinco.brewery.garden.GardenManager;

import java.util.List;

public class AutoSavableGardenPlant implements ExternallyAutoSavable {

    private final GardenManager gardenManager;

    public AutoSavableGardenPlant(GardenManager gardenManager) {
        this.gardenManager = gardenManager;
    }

    @Override
    public String table() {
        return "gardenplants";
    }

    @Override
    public void onAutoSave(DataManager dataManager) {
        List<SerializableGardenPlant> serializableGardenPlants = gardenManager.getGardenPlants()
                .stream().map(SerializableGardenPlant::new).toList();
        dataManager.saveAllGeneric(serializableGardenPlants, this.table(), true, SerializableGardenPlant.class);
    }
}
