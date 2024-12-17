package dev.jsinco.brewery.configuration;

import com.dre.brewery.api.addons.AddonConfigFile;
import com.dre.brewery.configuration.annotation.OkaeriConfigFileOptions;
import com.dre.brewery.depend.okaeri.configs.annotation.Comment;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@OkaeriConfigFileOptions("gardenConfig.yml")
public class BreweryGardenConfig extends AddonConfigFile {

    @Comment({"How likely it is for a seed to spawn from a broken 'validSeedDropBlocks' block.",
    "Use an integer from 1 to 100."})
    private int seedSpawnChance = 15;

    @Comment({"The integer which determines if a plant is fully grown (has a plant sprouted on it).",
    "A plant's growth stage increases by '1' every 5 minutes. Making '4' equal one full Minecraft day, or 20 minutes."})
    private int fullyGrown = 4;

    @Comment("A list of materials which a seed may drop from.")
    private List<Material> validSeedDropBlocks = List.of(Material.SHORT_GRASS, Material.TALL_GRASS);

    @Comment("A list of materials which a seed may be planted on.")
    private List<Material> plantableBlocks = List.of(Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.PODZOL);
}
