package pl.txtmiecze;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Config {
    public static final class Data {
        /** id aktywnej skorki (Skin.id) albo null = brak zmiany wygladu */
        public String activeSkin = null;
        /** klawisz otwierajacy GUI, kod GLFW zapisany jako int (domyslnie KEY_M) */
        public int keyCode = 77; // GLFW_KEY_M
    }

    public static Data d = new Data();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Config() {}

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("txtmiecze.json");
    }

    public static void load() {
        try {
            Path p = path();
            if (Files.exists(p)) {
                Data r = GSON.fromJson(Files.readString(p), Data.class);
                if (r != null) d = r;
            }
        } catch (Exception ignored) {
        }
        save();
    }

    public static void save() {
        try {
            Files.writeString(path(), GSON.toJson(d));
        } catch (IOException ignored) {
        }
    }

    public static Skin activeSkin() {
        return Skin.byId(d.activeSkin);
    }

    public static void setActiveSkin(Skin s) {
        d.activeSkin = s == null ? null : s.id;
        save();
    }
}
