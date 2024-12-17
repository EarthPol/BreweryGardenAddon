package dev.jsinco.brewery.integration;

import com.dre.brewery.recipe.PluginItem;
import dev.jsinco.brewery.constants.PlantType;
import org.bukkit.inventory.ItemStack;

public final class BreweryGardenPluginItem extends PluginItem {
    @Override
    public boolean matches(ItemStack itemStack) {
        PlantType type = PlantType.getPlantType(itemStack);
        if (type == null) {
            return false;
        }

        return type.toString().equalsIgnoreCase(this.getItemId());
    }
}
