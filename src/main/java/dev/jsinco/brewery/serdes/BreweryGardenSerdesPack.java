package dev.jsinco.brewery.serdes;

import com.dre.brewery.depend.okaeri.configs.serdes.OkaeriSerdesPack;
import com.dre.brewery.depend.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;

public class BreweryGardenSerdesPack implements OkaeriSerdesPack {
    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new WorldTiedBoundingBoxTransformer());
        registry.register(new PlantTypeTransformer());
        registry.register(new GardenPlantSerializer());
    }
}
