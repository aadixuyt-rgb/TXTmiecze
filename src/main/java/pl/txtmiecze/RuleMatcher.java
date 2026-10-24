package pl.txtmiecze;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/** Dopasowuje stack żelaznego miecza do jednej z zapisanych reguł po jego nazwie. */
public final class RuleMatcher {
    private RuleMatcher() {}

    public static Rule match(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.isOf(Items.IRON_SWORD)) return null;
        if (!stack.contains(DataComponentTypes.CUSTOM_NAME)) return null;
        String name = stack.getName().getString();
        for (Rule r : Config.d.rules) {
            if (r.name != null && r.name.equals(name) && r.skinEnum() != null) return r;
        }
        return null;
    }
}
