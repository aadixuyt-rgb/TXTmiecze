package pl.txtmiecze.mixin;

import net.minecraft.client.render.item.ItemRenderer;
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
 * Podmienia tylko argument ItemStack renderItem (NIE anuluje renderu) - gra dalej
 * renderuje całkiem normalnie, tyle że nasz podstawiony stack ma inny komponent
 * ITEM_MODEL, więc dostaje model/teksturę wybranego materiału. Działa to dla
 * ręki gracza (pierwsza i trzecia osoba), innych graczy, ziemi i GUI, bo to
 * dokładnie ten sam, wspólny punkt wejścia renderowania przedmiotów w grze
 * (sprawdzone już w modzie Trident Colors - te same dwa przeciążenia).
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;I)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private ItemStack txtmiecze$skinSimple(ItemStack stack) {
        return txtmiecze$disguise(stack);
    }

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private ItemStack txtmiecze$skinEntity(ItemStack stack) {
        return txtmiecze$disguise(stack);
    }

    private static ItemStack txtmiecze$disguise(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.isOf(Items.IRON_SWORD)) return stack;
        Skin skin = Config.activeSkin();
        if (skin == null) return stack;

        ItemStack disguised = stack.copy();
        disguised.set(DataComponentTypes.ITEM_MODEL, Identifier.of("txtmiecze", "skin_" + skin.id));
        return disguised;
    }
}
