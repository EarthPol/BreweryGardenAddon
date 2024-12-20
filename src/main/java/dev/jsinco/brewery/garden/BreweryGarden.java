package dev.jsinco.brewery.garden;

import com.dre.brewery.api.addons.AddonInfo;
import com.dre.brewery.api.addons.BreweryAddon;
import com.dre.brewery.recipe.PluginItem;
import dev.jsinco.brewery.garden.commands.AddonCommandManager;
import dev.jsinco.brewery.garden.constants.PlantType;
import dev.jsinco.brewery.garden.constants.PlantTypeSeeds;
import dev.jsinco.brewery.garden.events.EventListeners;
import dev.jsinco.brewery.garden.integration.BreweryGardenPluginItem;
import dev.jsinco.brewery.garden.objects.GardenPlant;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AddonInfo(name = "BreweryGarden", version = "BX3.4.5-SNAPSHOT", author = "Jsinco", description = "Adds plants to BreweryX, lightweight ExoticGarden.")
public final class BreweryGarden extends BreweryAddon {

    // TODO:
    //  I'd like to swap to a schematic based system for plants in this addon eventually.
    //  Having fruit trees would be a really nice feature. Additionally, I want to expand the config
    //  of this addon eventually.

    @Getter
    private static BreweryGarden instance;
    @Getter
    private static GardenManager gardenManager;

    @Override
    public void onAddonPreEnable() {
        instance = this;
        PluginItem.registerForConfig("garden", BreweryGardenPluginItem::new);
    }

    @Override
    public void onAddonEnable() {
        if (!this.isPaper()) {
            getAddonLogger().severe("This addon can only be run on Paper 1.21.3+");
            getAddonManager().unloadAddon(this);
            return;
        }

        gardenManager = new GardenManager(getDataManager());
        registerListener(new EventListeners(gardenManager));
        registerCommand("garden", new AddonCommandManager());
        getScheduler().runTaskTimer(new PlantGrowthRunnable(gardenManager), 1L, 6000L); // 5 minutes
        this.registerPlantRecipes();
    }

    @Override
    public void onAddonDisable() {
    }

    @Override
    public void onBreweryReload() {

    }


    private void registerPlantRecipes() {
        for (PlantTypeSeeds plantTypeSeeds : PlantTypeSeeds.values()) {
            PlantType plantType = plantTypeSeeds.getParent();
            NamespacedKey namespacedKey = new NamespacedKey(getBreweryPlugin(), plantType.name());
            if (Bukkit.getRecipe(namespacedKey) != null) {
                Bukkit.removeRecipe(namespacedKey);
            }

            ShapelessRecipe recipe = new ShapelessRecipe(namespacedKey, plantTypeSeeds.getItemStack(4));
            recipe.addIngredient(plantType.getItemStack(1));
            Bukkit.addRecipe(recipe);
        }
    }


    public static class PlantGrowthRunnable implements Runnable {

        private final GardenManager gardenManager;
        private final Random random = new Random();

        public PlantGrowthRunnable(GardenManager gardenManager) {
            this.gardenManager = gardenManager;
        }

        @Override
        public void run() {
            List<GardenPlant> toRemove = new ArrayList<>(); // dont concurrently modify
            gardenManager.getGardenPlants().forEach(gardenPlant -> {
                if (!gardenPlant.isValid()) {
                    toRemove.add(gardenPlant);
                } else if (random.nextInt(100) > 20) {
                    gardenPlant.incrementGrowthStage(1);
                }

                if (gardenPlant.isFullyGrown()) {
                    if (gardenPlant.isValid()) {
                        gardenPlant.place();
                    } else {
                        toRemove.add(gardenPlant);
                    }
                }
            });
            toRemove.forEach(gardenManager::removePlant);
        }
    }
}