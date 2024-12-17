package dev.jsinco.brewery.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

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

}
