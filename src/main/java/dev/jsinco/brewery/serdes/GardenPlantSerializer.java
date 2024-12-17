package dev.jsinco.brewery.serdes;

import com.dre.brewery.depend.okaeri.configs.schema.GenericsDeclaration;
import com.dre.brewery.depend.okaeri.configs.serdes.DeserializationData;
import com.dre.brewery.depend.okaeri.configs.serdes.ObjectSerializer;
import com.dre.brewery.depend.okaeri.configs.serdes.SerializationData;
import dev.jsinco.brewery.constants.PlantType;
import dev.jsinco.brewery.objects.GardenPlant;
import dev.jsinco.brewery.objects.WorldTiedBoundingBox;
import lombok.NonNull;

import java.util.UUID;

public class GardenPlantSerializer implements ObjectSerializer<GardenPlant> {
    @Override
    public boolean supports(@NonNull Class<? super GardenPlant> type) {
        return GardenPlant.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull GardenPlant object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("id", object.getId());
        data.add("type", object.getType());
        data.add("region", object.getRegion());
        data.add("age", object.getAge());
    }

    @Override
    public GardenPlant deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        UUID id = data.get("id", UUID.class);
        PlantType type = data.get("type", PlantType.class);
        WorldTiedBoundingBox region = data.get("region", WorldTiedBoundingBox.class);
        int age = data.get("age", Integer.class);
        return new GardenPlant(id, type, region, age);
    }
}
