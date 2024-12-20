package dev.jsinco.brewery.garden.persist;

import com.dre.brewery.storage.interfaces.SerializableThing;
import com.dre.brewery.utility.BUtil;
import dev.jsinco.brewery.garden.constants.PlantType;
import dev.jsinco.brewery.garden.objects.GardenPlant;
import dev.jsinco.brewery.garden.objects.WorldTiedBoundingBox;

public record SerializableGardenPlant(String id, String type, String region, int age) implements SerializableThing {

    public SerializableGardenPlant(GardenPlant gardenPlant) {
        this(gardenPlant.getId().toString(), gardenPlant.getType().name(), gardenPlant.getRegion().toString(), gardenPlant.getAge());
    }


    public GardenPlant toGardenPlant() {
        return new GardenPlant(BUtil.uuidFromString(id), PlantType.valueOf(type), WorldTiedBoundingBox.fromString(region), age);
    }

    @Override
    public String getId() {
        return id;
    }
}
