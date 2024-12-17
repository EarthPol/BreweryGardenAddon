package dev.jsinco.brewery.constants;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

@Getter
public enum PlantPart {
    BOTTOM(0, -1, 0, Material.OAK_FENCE),
    MIDDLE(0, 0, 0, Material.OAK_LEAVES),
    TOP(0, 1, 0, Material.PLAYER_HEAD);

    private final int offsetCenterX;
    private final int offsetCenterY;
    private final int offsetCenterZ;
    private final Material assigneeMaterial;

    PlantPart(int offsetCenterX, int offsetCenterY, int offsetCenterZ, Material assigneeMaterial) {
        this.offsetCenterX = offsetCenterX;
        this.offsetCenterY = offsetCenterY;
        this.offsetCenterZ = offsetCenterZ;
        this.assigneeMaterial = assigneeMaterial;
    }

    public Location locationFromCenter(Location center) {
        return center.clone().add(offsetCenterX, offsetCenterY, offsetCenterZ);
    }

    public Location locationFromCenter(Vector center, World world) {
        return center.clone().add(new Vector(offsetCenterX, offsetCenterY, offsetCenterZ)).toLocation(world);
    }
}
