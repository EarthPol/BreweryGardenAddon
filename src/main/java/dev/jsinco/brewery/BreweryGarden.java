package dev.jsinco.brewery;

import com.dre.brewery.api.addons.AddonInfo;
import com.dre.brewery.api.addons.BreweryAddon;
import dev.jsinco.brewery.events.EventListeners;
import dev.jsinco.brewery.objects.GardenManager;
import dev.jsinco.brewery.persist.GardenPlantTransformer;
import lombok.Getter;

@AddonInfo(name = "BreweryGarden", version = "BX3.4.5-SNAPSHOT", author = "Jsinco", description = "Adds plants to BreweryX, lightweight ExoticGarden.")
public class BreweryGarden extends BreweryAddon {

    @Getter
    private static BreweryGarden instance;

    @Override
    public void onAddonPreEnable() {
        instance = this;
    }

    @Override
    public void onAddonEnable() {
        getAddonConfigManager().addBiDirectionalTransformers(new GardenPlantTransformer());
        registerListener(new EventListeners(getAddonConfigManager().getConfig(GardenManager.class)));
    }
}