package pl.txtmiecze.mixin;

import net.minecraft.client.render.item.ItemModelManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pl.txtmiecze.Config;
import pl.txtmiecze.Skin;

/**
 * Podmienia tylko MODEL (wygląd) żelaznego miecza, gdy aktywna jest jakaś skórka -
 * nazwa, lore i cała reszta danych itemu zostają bez zmian, bo zmieniamy wyłącznie
 * komponent ITEM_MODEL na kopii stacka użytej do renderu (czysto kosmetyczne,
 * tylko po stronie klienta).
 */
@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {
    private static final String UPDATE =
            "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V";

    @ModifyVariable(method = UPDATE, at = @At("HEAD"), argsOnly = true)
    private ItemStack txtmiecze$skin(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return stack;
        if (!stack.isOf(Items.IRON_SWORD)) return stack;
        Skin skin = Config.activeSkin();
        if (skin == null) return stack;

        ItemStack disguised = stack.copy();
        disguised.set(DataComponentTypes.ITEM_MODEL, Identifier.of("txtmiecze", "skin_" + skin.id));
        return disguised;
    }
}
