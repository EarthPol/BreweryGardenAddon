package dev.jsinco.brewery.serdes;

import com.dre.brewery.depend.okaeri.configs.schema.GenericsPair;
import com.dre.brewery.depend.okaeri.configs.serdes.BidirectionalTransformer;
import com.dre.brewery.depend.okaeri.configs.serdes.SerdesContext;
import dev.jsinco.brewery.constants.PlantType;
import lombok.NonNull;

public class PlantTypeTransformer extends BidirectionalTransformer<String, PlantType> {
    @Override
    public GenericsPair<String, PlantType> getPair() {
        return this.genericsPair(String.class, PlantType.class);
    }

    @Override
    public PlantType leftToRight(@NonNull String data, @NonNull SerdesContext serdesContext) {
        return PlantType.valueOf(data);
    }

    @Override
    public String rightToLeft(@NonNull PlantType data, @NonNull SerdesContext serdesContext) {
        return data.toString();
    }
}
