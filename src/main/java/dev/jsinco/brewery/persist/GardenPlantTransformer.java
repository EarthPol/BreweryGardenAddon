package dev.jsinco.brewery.persist;

import com.dre.brewery.depend.okaeri.configs.schema.GenericsPair;
import com.dre.brewery.depend.okaeri.configs.serdes.BidirectionalTransformer;
import com.dre.brewery.depend.okaeri.configs.serdes.SerdesContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.jsinco.brewery.objects.GardenPlant;
import dev.jsinco.brewery.objects.WorldTiedBoundingBox;
import lombok.NonNull;

public class GardenPlantTransformer extends BidirectionalTransformer<String, GardenPlant> {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(WorldTiedBoundingBox.class, new WorldTiedBoundingBox.TypeAdapter()).create();

    @Override
    public GenericsPair<String, GardenPlant> getPair() {
        return this.genericsPair(String.class, GardenPlant.class);
    }

    @Override
    public GardenPlant leftToRight(@NonNull String data, @NonNull SerdesContext serdesContext) {
        return gson.fromJson(data, GardenPlant.class);
    }

    @Override
    public String rightToLeft(@NonNull GardenPlant data, @NonNull SerdesContext serdesContext) {
        return gson.toJson(data);
    }
}
