package dev.jsinco.brewery;

import com.dre.brewery.api.addons.AddonInfo;
import com.dre.brewery.api.addons.BreweryAddon;
import com.dre.brewery.recipe.PluginItem;
import dev.jsinco.brewery.commands.AddonCommandManager;
import dev.jsinco.brewery.constants.PlantType;
import dev.jsinco.brewery.constants.PlantTypeSeeds;
import dev.jsinco.brewery.events.EventListeners;
import dev.jsinco.brewery.integration.BreweryGardenPluginItem;
import dev.jsinco.brewery.objects.GardenManager;
import dev.jsinco.brewery.objects.GardenPlant;
import dev.jsinco.brewery.serdes.BreweryGardenSerdesPack;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AddonInfo(name = "BreweryGarden", version = "BX3.4.5-SNAPSHOT", author = "Jsinco", description = "Adds plants to BreweryX, lightweight ExoticGarden.")
public final class BreweryGarden extends BreweryAddon {

    // TODO: Update BreweryX storage system for generic object/sector support

    @Getter
    private static BreweryGarden instance;
    @Getter
    private static GardenManager gardenManager;

    @Override
    public void onAddonPreEnable() {
        instance = this;
    }

    @Override
    public void onAddonEnable() {
        if (!this.isPaper()) {
            getAddonLogger().severe("This addon can only be run on Paper 1.21.3+");
            getAddonManager().unloadAddon(this);
            return;
        }

        getAddonConfigManager().addSerdesPacks(new BreweryGardenSerdesPack());

        gardenManager = getAddonConfigManager().getConfig(GardenManager.class);
        registerListener(new EventListeners(gardenManager));
        registerCommand("garden", new AddonCommandManager());
        getScheduler().runTaskTimer(new PlantGrowthRunnable(gardenManager), 1L, 6000L); // 5 minutes

        PluginItem.registerForConfig("garden", BreweryGardenPluginItem::new);
        this.registerPlantRecipes();
    }

    @Override
    public void onAddonDisable() {
        GardenManager gardenManager = getAddonConfigManager().getConfig(GardenManager.class);
        gardenManager.save();
    }

    @Override
    public void onBreweryReload() {
        gardenManager.reload();
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
                if (random.nextInt(100) > 20) {
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
            gardenManager.saveAsync();
        }
    }
}