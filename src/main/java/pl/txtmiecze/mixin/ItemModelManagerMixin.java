package pl.txtmiecze.mixin;

import net.minecraft.client.item.ItemModelManager;
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
 * Podmienia komponent ITEM_MODEL na kopii stacka, zanim gra zbuduje stan renderu -
 * to jest jeden, wspólny punkt wejścia używany przez KAŻDY kontekst (ręka gracza,
 * ręka innego gracza, ekwipunek/GUI, przedmiot na ziemi), więc w przeciwieństwie
 * do mixinowania ItemRenderer.renderItem (które łapało tylko rękę), to działa
 * wszędzie. Nazwa, lore itd. zostają nietknięte - kopiujemy tylko do renderu.
 */
@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {
    private static final String UPDATE =
            "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V";

    @ModifyVariable(method = UPDATE, at = @At("HEAD"), argsOnly = true)
    private ItemStack txtmiecze$skin(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.isOf(Items.IRON_SWORD)) return stack;
        // tak jak w easysnup: jesli przedmiot ma wlasna (nadana np. na kowadle) nazwe,
        // nie ruszamy go - skorka dziala tylko na "zwykly" zelazny miecz bez customowej nazwy
        if (stack.contains(DataComponentTypes.CUSTOM_NAME)) return stack;
        Skin skin = Config.activeSkin();
        if (skin == null) return stack;

        ItemStack disguised = stack.copy();
        String suffix = Config.d.swordPose ? "_sword" : "_flat";
        disguised.set(DataComponentTypes.ITEM_MODEL, Identifier.of("txtmiecze", "skin_" + skin.id + suffix));
        return disguised;
    }
}
