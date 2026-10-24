package pl.txtmiecze;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Config {
    public static final class Data {
        /** lista regul: nazwa miecza (na kowadle) -> skorka */
        public List<Rule> rules = new ArrayList<>();
        /** klawisz otwierajacy GUI, kod GLFW zapisany jako int (domyslnie KEY_M) */
        public int keyCode = 77; // GLFW_KEY_M
        /** true = trzymanie jak miecz (trzon w gore), false = plasko jak zwykly item */
        public boolean swordPose = true;
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
        if (d.rules == null) d.rules = new ArrayList<>();
        save();
    }

    public static void save() {
        try {
            Files.writeString(path(), GSON.toJson(d));
        } catch (IOException ignored) {
        }
    }
}
