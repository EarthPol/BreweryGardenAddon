package dev.jsinco.brewery.serdes;

import com.dre.brewery.depend.okaeri.configs.schema.GenericsPair;
import com.dre.brewery.depend.okaeri.configs.serdes.BidirectionalTransformer;
import com.dre.brewery.depend.okaeri.configs.serdes.SerdesContext;
import dev.jsinco.brewery.objects.WorldTiedBoundingBox;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldTiedBoundingBoxTransformer extends BidirectionalTransformer<String, WorldTiedBoundingBox> {
    @Override
    public GenericsPair<String, WorldTiedBoundingBox> getPair() {
        return this.genericsPair(String.class, WorldTiedBoundingBox.class);
    }

    @Override
    public WorldTiedBoundingBox leftToRight(@NonNull String data, @NonNull SerdesContext serdesContext) {
        String[] split = data.split(",");
        World world = Bukkit.getWorld(split[0]);
        double x1 = Double.parseDouble(split[1]);
        double y1 = Double.parseDouble(split[2]);
        double z1 = Double.parseDouble(split[3]);
        double x2 = Double.parseDouble(split[4]);
        double y2 = Double.parseDouble(split[5]);
        double z2 = Double.parseDouble(split[6]);
        return new WorldTiedBoundingBox(world, x1, y1, z1, x2, y2, z2);
    }

    @Override
    public String rightToLeft(@NonNull WorldTiedBoundingBox data, @NonNull SerdesContext serdesContext) {
        return String.join(",",
            data.getWorld().getName(),
            String.valueOf(data.getMinX()),
            String.valueOf(data.getMinY()),
            String.valueOf(data.getMinZ()),
            String.valueOf(data.getMaxX()),
            String.valueOf(data.getMaxY()),
            String.valueOf(data.getMaxZ())
        );
        
    }
}
