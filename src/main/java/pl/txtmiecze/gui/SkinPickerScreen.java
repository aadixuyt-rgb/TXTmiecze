package pl.txtmiecze.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pl.txtmiecze.Config;
import pl.txtmiecze.Rule;
import pl.txtmiecze.Skin;

/**
 * GUI pod klawiszem M. Działa na mieczu AKTUALNIE trzymanym w ręce (musi mieć
 * własną nazwę, nadaną np. na kowadle): kliknięcie koloru przypisuje/PODMIENIA
 * (nie utyka) regułę dla tej nazwy na wybraną skórkę. Przycisk ✕ obok koloru
 * kasuje regułę, która aktualnie go używa (jeśli jakaś istnieje).
 */
public class SkinPickerScreen extends Screen {
    private final Screen parent;
    private String heldName;

    public SkinPickerScreen(Screen parent) {
        super(Text.literal("TXTmiecze"));
        this.parent = parent;
    }

    private String readHeldName() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return null;
        ItemStack held = mc.player.getMainHandStack();
        if (held.isEmpty() || !held.isOf(Items.IRON_SWORD)) return null;
        if (!held.contains(DataComponentTypes.CUSTOM_NAME)) return null;
        return held.getName().getString();
    }

    @Override
    protected void init() {
        heldName = readHeldName();

        int bw = 260, bh = 20, gap = 5;
        int startX = this.width / 2 - bw / 2 - 14;
        int startY = this.height / 2 - 140;

        Skin[] skins = Skin.values();
        for (int i = 0; i < skins.length; i++) {
            Skin s = skins[i];
            int y = startY + i * (bh + gap);
            Rule current = ruleForSkin(s);
            boolean assigned = current != null;
            String suffix = assigned ? "  -  " + current.name : "";
            Text label = Text.literal(s.label + suffix).formatted(assigned ? Formatting.AQUA : Formatting.WHITE);

            addDrawableChild(ButtonWidget.builder(label, b -> {
                if (heldName == null) return;
                assignHeldSwordTo(s);
                this.client.setScreen(new SkinPickerScreen(parent));
            }).dimensions(startX, y, bw, bh).build());

            addDrawableChild(ButtonWidget.builder(Text.literal("✕").formatted(Formatting.RED), b -> {
                if (current != null) {
                    Config.d.rules.remove(current);
                    Config.save();
                }
                this.client.setScreen(new SkinPickerScreen(parent));
            }).dimensions(startX + bw + 6, y, 24, bh).build());
        }

        int bottomY = startY + skins.length * (bh + gap) + 8;
        addDrawableChild(ButtonWidget.builder(poseText(), b -> {
            Config.d.swordPose = !Config.d.swordPose;
            Config.save();
            b.setMessage(poseText());
        }).dimensions(startX, bottomY, bw + 30, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij").formatted(Formatting.GRAY), b -> close())
                .dimensions(startX, bottomY + 26, bw + 30, 20).build());
    }

    private Rule ruleForSkin(Skin s) {
        for (Rule r : Config.d.rules) {
            if (s.id.equals(r.skin)) return r;
        }
        return null;
    }

    private void assignHeldSwordTo(Skin skin) {
        for (Rule r : Config.d.rules) {
            if (heldName.equals(r.name)) {
                r.skin = skin.id; // PODMIEŃ, nie dodawaj drugiej reguły dla tej samej nazwy
                Config.save();
                return;
            }
        }
        Rule r = new Rule();
        r.name = heldName;
        r.skin = skin.id;
        Config.d.rules.add(r);
        Config.save();
    }

    private Text poseText() {
        return Text.literal("Trzymanie: ").append(Text.literal(Config.d.swordPose ? "jak miecz (w górę)" : "płasko (jak item)")
                .formatted(Config.d.swordPose ? Formatting.AQUA : Formatting.GOLD));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        int panelW = 420, panelH = 320;
        int px = this.width / 2 - panelW / 2, py = this.height / 2 - panelH / 2;
        UiFx.drawPanel(ctx, px, py, panelW, panelH);

        super.render(ctx, mouseX, mouseY, delta);

        ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal("TXTmiecze").formatted(Formatting.AQUA, Formatting.BOLD),
                this.width / 2, py + 10, 0xFFFFFF);

        Text status = heldName != null
                ? Text.literal("Trzymasz: ").append(Text.literal(heldName).formatted(Formatting.GOLD))
                : Text.literal("Trzymaj w ręce nazwany żelazny miecz (nadaj nazwę na kowadle)").formatted(Formatting.RED);
        ctx.drawCenteredTextWithShadow(this.textRenderer, status, this.width / 2, py + 24, 0xFFFFFF);

        for (var child : this.children()) {
            if (child instanceof ButtonWidget bw) UiFx.hoverGlow(ctx, bw, mouseX, mouseY);
        }
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
