package pl.txtmiecze;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import pl.txtmiecze.gui.SkinPickerScreen;

public final class TxtMieczeMod implements ClientModInitializer {
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        Config.load();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.txtmiecze.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "key.category.txtmiecze"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new SkinPickerScreen(null));
                }
            }
        });
    }
}
