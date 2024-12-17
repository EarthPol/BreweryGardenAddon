package dev.jsinco.brewery.constants;

import com.dre.brewery.BreweryPlugin;
import dev.jsinco.brewery.BreweryGarden;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PlantTypeSeeds {


    public static final PlantTypeSeeds BERRY_SEEDS = new PlantTypeSeeds(PlantType.BERRY, Material.MELON_SEEDS);
    public static final PlantTypeSeeds STRAWBERRY_SEEDS = new PlantTypeSeeds(PlantType.STRAWBERRY, Material.WHEAT_SEEDS);
    public static final PlantTypeSeeds LEMON_SEEDS = new PlantTypeSeeds(PlantType.LEMON, Material.PUMPKIN_SEEDS);
    public static final PlantTypeSeeds LIME_SEEDS = new PlantTypeSeeds(PlantType.LIME, Material.BEETROOT_SEEDS);
    public static final PlantTypeSeeds ORANGE_SEEDS = new PlantTypeSeeds(PlantType.ORANGE, Material.NETHER_WART);
    public static final PlantTypeSeeds GRAPE_SEEDS = new PlantTypeSeeds(PlantType.GRAPE, Material.COCOA_BEANS);
    public static final PlantTypeSeeds APPLE_SEEDS = new PlantTypeSeeds(PlantType.APPLE, Material.SWEET_BERRIES);



    private static final NamespacedKey PERSISTENT_DATA_KEY = new NamespacedKey(BreweryPlugin.getInstance(), "plant_seeds");

    private String FIELD_NAME; // Reflect

    private final PlantType parent;
    private final Component name;
    private final Material seedMaterial;

    public PlantTypeSeeds(PlantType parent, String name, Material seedMaterial) {
        this.parent = parent;
        this.name = MiniMessage.miniMessage().deserialize(name);
        this.seedMaterial = seedMaterial;
    }

    public PlantTypeSeeds(PlantType parent, Material seedMaterial) {
        this.parent = parent;
        this.name = parent.getName().append(Component.text(" Seeds"));
        this.seedMaterial = seedMaterial;
    }


    @SuppressWarnings("UnstableApiUsage")
    public ItemStack getItemStack(int amount) {
        ItemStack item = new ItemStack(this.seedMaterial, amount);
        item.setData(DataComponentTypes.ITEM_NAME, name);

        // TODO: Ask in Paper discord how to use PDC with new ItemMeta API
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(PERSISTENT_DATA_KEY, PersistentDataType.STRING, FIELD_NAME);
        item.setItemMeta(meta);
        return item;
    }

    // Util

    public static boolean isSeeds(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(PERSISTENT_DATA_KEY, PersistentDataType.STRING);
    }

    @Nullable
    public static PlantTypeSeeds getPlantSeedsType(ItemStack item) {
        if (!isSeeds(item)) return null;
        String field_name = item.getItemMeta().getPersistentDataContainer().get(PERSISTENT_DATA_KEY, PersistentDataType.STRING);
        if (field_name == null) return null;
        return valueOf(field_name);
    }


    // Reflect

    private static final Map<String, PlantTypeSeeds> VALUES = new HashMap<>();

    static {
        for (Field field : PlantTypeSeeds.class.getDeclaredFields()) {
            if (field.getType() != PlantTypeSeeds.class) continue;

            try {
                PlantTypeSeeds plantType = (PlantTypeSeeds) field.get(null);
                plantType.FIELD_NAME = field.getName();
                VALUES.put(field.getName(), plantType);
            } catch (IllegalAccessException e) {
                BreweryGarden.getInstance().getAddonLogger().severe("Failed to get field reflectively.", e);
            }
        }
    }

    public static PlantTypeSeeds valueOf(String name) {
        return VALUES.get(name);
    }

    public static List<PlantTypeSeeds> values() {
        return VALUES.values().stream().toList();
    }

    @Override
    public String toString() {
        return FIELD_NAME;
    }
}
