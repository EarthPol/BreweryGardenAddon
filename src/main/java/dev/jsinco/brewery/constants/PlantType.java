package dev.jsinco.brewery.constants;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.dre.brewery.BreweryPlugin;
import dev.jsinco.brewery.BreweryGarden;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static dev.jsinco.brewery.utility.PlayerSkinUtil.fromHashCode;


@Getter
public class PlantType {

    // TODO: get some plant base64 textures
    public static final PlantType BERRY = new PlantType(
            "<light_purple>Berry",
            fromHashCode("1e4883a1e22c324e753151e2ac424c74f1cc646eec8ea0db3420f1dd1d8b")
    );
    public static final PlantType STRAWBERRY = new PlantType(
            "<dark_red>Strawberry",
            fromHashCode("cbc826aaafb8dbf67881e68944414f13985064a3f8f044d8edfb4443e76ba")
    );
    public static final PlantType LEMON = new PlantType(
            "<yellow>Lemon",
            fromHashCode("957fd56ca15978779324df519354b6639a8d9bc1192c7c3de925a329baef6c")
    );
    public static final PlantType LIME = new PlantType(
            "<green>Lime",
            fromHashCode("5a5153479d9f146a5ee3c9e218f5e7e84c4fa375e4f86d31772ba71f6468")
    );
    public static final PlantType ORANGE = new PlantType(
            "<#FFA500>Orange",
            fromHashCode("65b1db547d1b7956d4511accb1533e21756d7cbc38eb64355a2626412212")
    );
    public static final PlantType GRAPE = new PlantType(
            "<red>Grape",
            fromHashCode("6ee97649bd999955413fcbf0b269c91be4342b10d0755bad7a17e95fcefdab0")
    );
    public static final PlantType APPLE = new PlantType(
            "<red>Apple",
            fromHashCode("cbb311f3ba1c07c3d1147cd210d81fe11fd8ae9e3db212a0fa748946c3633")
    );

    // Forever constant UUID so that all plant ItemStacks are stackable. AKA. Don't change me!
    private static final UUID CONSTANT_UUID = UUID.fromString("f714a407-f7c9-425c-958d-c9914aeac05c");
    private static final NamespacedKey PERSISTENT_DATA_KEY = new NamespacedKey(BreweryPlugin.getInstance(), "plant");
    private static final Random RANDOM = new Random();

    private String FIELD_NAME; // Reflect

    private final Component name;
    private final String base64Texture;

    private PlantType(String name, String skin) {
        this.name = MiniMessage.miniMessage().deserialize(name);
        this.base64Texture = skin;
    }

    public void setSkullTexture(Block block) { // Think you can do this through the DataComponent API, but I'm in a rush to test this.
        Skull skull = (Skull) block.getState();
        skull.setPlayerProfile(this.getPlayerProfile());
        skull.update();
    }

    @SuppressWarnings("UnstableApiUsage")
    public ItemStack getItemStack(int amount) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
        item.setData(DataComponentTypes.ITEM_NAME, name);
        item.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(this.getPlayerProfile()));

        FoodProperties foodProps = FoodProperties.food()
                .nutrition(RANDOM.nextInt(2,5))
                .saturation((float) RANDOM.nextInt(1, 4))
                .build();
        item.setData(DataComponentTypes.FOOD, foodProps);

        // TODO: Ask in Paper discord how to use PDC with new ItemMeta API
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(PERSISTENT_DATA_KEY, PersistentDataType.STRING, FIELD_NAME);
        item.setItemMeta(meta);
        return item;
    }

    private PlayerProfile getPlayerProfile() {
        PlayerProfile profile = Bukkit.createProfile(CONSTANT_UUID);
        profile.getProperties().add(new ProfileProperty("textures", base64Texture));
        return profile;
    }

    // Util

    public static boolean isPlant(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(PERSISTENT_DATA_KEY, PersistentDataType.STRING);
    }

    @Nullable
    public static PlantType getPlantType(ItemStack item) {
        if (!isPlant(item)) return null;
        String field_name = item.getItemMeta().getPersistentDataContainer().get(PERSISTENT_DATA_KEY, PersistentDataType.STRING);
        if (field_name == null) return null;
        return valueOf(field_name);
    }



    // Reflect

    private static final Map<String, PlantType> VALUES = new HashMap<>();

    static {
        for (Field field : PlantType.class.getDeclaredFields()) {
            if (field.getType() != PlantType.class) continue;

            try {
                PlantType plantType = (PlantType) field.get(null);
                plantType.FIELD_NAME = field.getName();
                VALUES.put(field.getName(), plantType);
            } catch (IllegalAccessException e) {
                BreweryGarden.getInstance().getAddonLogger().severe("Failed to get field reflectively.", e);
            }
        }
    }

    public static PlantType valueOf(String name) {
        return VALUES.get(name);
    }

    public static List<PlantType> values() {
        return VALUES.values().stream().toList();
    }

    @Override
    public String toString() {
        return FIELD_NAME;
    }
}
