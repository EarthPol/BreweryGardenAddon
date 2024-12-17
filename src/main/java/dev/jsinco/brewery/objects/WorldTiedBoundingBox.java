package dev.jsinco.brewery.objects;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class WorldTiedBoundingBox extends BoundingBox {

    private World world;

    public WorldTiedBoundingBox(World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.world = world;
    }

    public List<Block> getBlocks() {
        List<Block> bL = new LinkedList<>();
        for (int x = (int) this.getMinX(); x <= (int) this.getMaxX(); ++x) {
            for (int y = (int) this.getMinY(); y <= (int) this.getMaxY(); ++y) {
                for (int z = (int) this.getMinZ(); z <= (int) this.getMaxZ(); ++z) {
                    bL.add(this.world.getBlockAt(x, y, z));
                }
            }
        }
        return bL;
    }

    public static WorldTiedBoundingBox of(Location location, Location location2) {
        return new WorldTiedBoundingBox(location.getWorld(), location.getX(), location.getY(), location.getZ(), location2.getX(), location2.getY(), location2.getZ());
    }
    
    public static class TypeAdapter extends com.google.gson.TypeAdapter<WorldTiedBoundingBox> {

        @Override
        public void write(JsonWriter jsonWriter, WorldTiedBoundingBox worldTiedBoundingBox) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("world").value(worldTiedBoundingBox.getWorld().getName());
            jsonWriter.name("minX").value(worldTiedBoundingBox.getMinX());
            jsonWriter.name("minY").value(worldTiedBoundingBox.getMinY());
            jsonWriter.name("minZ").value(worldTiedBoundingBox.getMinZ());
            jsonWriter.name("maxX").value(worldTiedBoundingBox.getMaxX());
            jsonWriter.name("maxY").value(worldTiedBoundingBox.getMaxY());
            jsonWriter.name("maxZ").value(worldTiedBoundingBox.getMaxZ());
            jsonWriter.endObject();
        }

        @Override
        public WorldTiedBoundingBox read(JsonReader jsonReader) throws IOException {
            World world = null;
            double minX = 0;
            double minY = 0;
            double minZ = 0;
            double maxX = 0;
            double maxY = 0;
            double maxZ = 0;

            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                switch (name) {
                    case "world" -> world = org.bukkit.Bukkit.getWorld(jsonReader.nextString());
                    case "minX" -> minX = jsonReader.nextDouble();
                    case "minY" -> minY = jsonReader.nextDouble();
                    case "minZ" -> minZ = jsonReader.nextDouble();
                    case "maxX" -> maxX = jsonReader.nextDouble();
                    case "maxY" -> maxY = jsonReader.nextDouble();
                    case "maxZ" -> maxZ = jsonReader.nextDouble();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            return new WorldTiedBoundingBox(world, minX, minY, minZ, maxX, maxY, maxZ);
        }
    }
}
