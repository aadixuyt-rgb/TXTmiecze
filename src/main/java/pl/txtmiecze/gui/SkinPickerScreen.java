package pl.txtmiecze.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pl.txtmiecze.Config;
import pl.txtmiecze.Skin;

/**
 * GUI pod klawiszem M: lista 10 skórek żelaznego miecza + przycisk "Brak" (oryginalny
 * wygląd). Wybór jest od razu zapisywany i natychmiast widoczny (sam mixin reaguje na
 * aktualny Config, nie trzeba nic przeładowywać).
 */
public class SkinPickerScreen extends Screen {
    private final Screen parent;

    public SkinPickerScreen(Screen parent) {
        super(Text.literal("TXTmiecze"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cols = 2;
        int bw = 180, bh = 22, gap = 6;
        int totalW = cols * bw + gap;
        int startX = this.width / 2 - totalW / 2;
        int startY = this.height / 2 - 130;

        Skin active = Config.activeSkin();
        Skin[] skins = Skin.values();
        for (int i = 0; i < skins.length; i++) {
            Skin s = skins[i];
            int col = i % cols, row = i / cols;
            int x = startX + col * (bw + gap);
            int y = startY + row * (bh + gap);
            boolean isActive = s == active;
            Text label = Text.literal((isActive ? "● " : "") + s.label)
                    .formatted(isActive ? Formatting.AQUA : Formatting.WHITE);
            addDrawableChild(ButtonWidget.builder(label, b -> {
                Config.setActiveSkin(s);
                this.client.setScreen(new SkinPickerScreen(parent));
            }).dimensions(x, y, bw, bh).build());
        }

        int lastRow = (skins.length - 1) / cols + 1;
        int noneY = startY + lastRow * (bh + gap) + 8;
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Brak (oryginalny miecz)").formatted(active == null ? Formatting.GREEN : Formatting.GRAY),
                b -> {
                    Config.setActiveSkin(null);
                    this.client.setScreen(new SkinPickerScreen(parent));
                }).dimensions(startX, noneY, totalW - gap, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij").formatted(Formatting.GRAY), b -> close())
                .dimensions(startX, noneY + 28, totalW - gap, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        int panelW = 420, panelH = 290;
        int px = this.width / 2 - panelW / 2, py = this.height / 2 - panelH / 2;
        UiFx.drawPanel(ctx, px, py, panelW, panelH);

        super.render(ctx, mouseX, mouseY, delta);

        ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal("TXTmiecze").formatted(Formatting.AQUA, Formatting.BOLD),
                this.width / 2, py + 12, 0xFFFFFF);
        ctx.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Wygląd żelaznego miecza (tylko u Ciebie, nazwa bez zmian)").formatted(Formatting.GRAY),
                this.width / 2, py + 26, 0xFFFFFF);

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
