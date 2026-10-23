package pl.txtmiecze.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;

/** Ciemny, nowoczesny panel GUI - ten sam styl co reszta modów. */
public final class UiFx {
    private UiFx() {}

    public static final int ACCENT = 0xFF6FD2FF;
    public static final int ACCENT_DIM = 0x556FD2FF;
    public static final int PANEL_BG = 0xE6141821;
    public static final int PANEL_BORDER = 0xFF2A3344;

    public static void drawPanel(DrawContext ctx, int x, int y, int w, int h) {
        ctx.fill(x - 2, y - 2, x + w + 2, y + h + 2, 0x55000000);
        ctx.fill(x, y, x + w, y + h, PANEL_BG);
        ctx.fill(x, y, x + w, y + 2, PANEL_BORDER);
        ctx.fill(x, y + h - 2, x + w, y + h, PANEL_BORDER);
        ctx.fill(x, y, x + 2, y + h, PANEL_BORDER);
        ctx.fill(x + w - 2, y, x + w, y + h, PANEL_BORDER);
        ctx.fill(x, y, x + w, y + 3, ACCENT_DIM);
    }

    public static void hoverGlow(DrawContext ctx, ClickableWidget w, int mouseX, int mouseY) {
        if (!w.isHovered()) return;
        int x = w.getX(), y = w.getY(), ww = w.getWidth(), hh = w.getHeight();
        ctx.fill(x - 2, y - 2, x + ww + 2, y + hh + 2, 0x33FFFFFF);
    }
}
