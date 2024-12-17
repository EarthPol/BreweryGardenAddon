package dev.jsinco.brewery;

import com.dre.brewery.api.addons.AddonInfo;
import com.dre.brewery.api.addons.BreweryAddon;
import dev.jsinco.brewery.events.EventListeners;
import dev.jsinco.brewery.objects.GardenManager;
import dev.jsinco.brewery.objects.GardenPlant;
import dev.jsinco.brewery.serdes.BreweryGardenSerdesPack;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AddonInfo(name = "BreweryGarden", version = "BX3.4.5-SNAPSHOT", author = "Jsinco", description = "Adds plants to BreweryX, lightweight ExoticGarden.")
public class BreweryGarden extends BreweryAddon {

    // TODO: Brewery plugin item
    // TODO: Runnable for growing plants
    //TODO: Crafting recipe
    // TODO: Update BreweryX storage
    @Getter
    private static BreweryGarden instance;

    @Override
    public void onAddonPreEnable() {
        instance = this;
    }

    @Override
    public void onAddonEnable() {
        getAddonConfigManager().addSerdesPacks(new BreweryGardenSerdesPack());
        GardenManager gardenManager = getAddonConfigManager().getConfig(GardenManager.class);


        registerListener(new EventListeners(gardenManager));

        getScheduler().runTaskTimer(new PlantGrowthRunnable(gardenManager), 1L, 50L);
    }

    @Override
    public void onAddonDisable() {
        GardenManager gardenManager = getAddonConfigManager().getConfig(GardenManager.class);
        gardenManager.save();
    }

    public static class PlantGrowthRunnable implements Runnable {

        private final GardenManager gardenManager;

        public PlantGrowthRunnable(GardenManager gardenManager) {
            this.gardenManager = gardenManager;
        }

        @Override
        public void run() {
            List<GardenPlant> toRemove = new ArrayList<>(); // dont concurrently modify
            gardenManager.getGardenPlants().forEach(gardenPlant -> {
                gardenPlant.incrementGrowthStage(1);
                if (gardenPlant.isFullyGrown()) {
                    if (gardenPlant.isValid()) {
                        gardenPlant.place();
                    } else {
                        toRemove.add(gardenPlant);
                    }
                }
            });
            toRemove.forEach(gardenManager::removePlant);
            gardenManager.saveAsync();
        }
    }
}