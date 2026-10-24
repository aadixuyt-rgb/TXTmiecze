package pl.txtmiecze;

import java.util.UUID;

/** Jedna reguła: miecz o danej (nadanej np. na kowadle) nazwie -> wybrana skórka. */
public final class Rule {
    public String id = UUID.randomUUID().toString();
    public String name = "";
    /** Skin.id */
    public String skin = null;

    public Skin skinEnum() {
        return Skin.byId(skin);
    }
}
