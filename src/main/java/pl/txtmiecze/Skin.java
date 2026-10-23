package pl.txtmiecze;

/**
 * 10 dostępnych "skórek" żelaznego miecza. Każda korzysta z gotowej, oryginalnej
 * tekstury danego materiału (minecraft:item/&lt;vanillaId&gt;), więc mod nie musi
 * dostarczać żadnych własnych obrazków - tylko modele (item/handheld), dzięki czemu
 * trzymanie w ręku zawsze wygląda jak trzymanie miecza (trzonem w górę).
 */
public enum Skin {
    DIAMOND("diamond", "diamond", "Jasny niebieski", 0x55DFFF),
    RABBIT_HIDE("rabbit_hide", "rabbit_hide", "Brązowy", 0xA06A3C),
    AMETHYST_SHARD("amethyst_shard", "amethyst_shard", "Fioletowy", 0x9B59D9),
    GLOWSTONE_DUST("glowstone_dust", "glowstone_dust", "Żółty", 0xFFE34D),
    GLOW_INK_SAC("glow_ink_sac", "glow_ink_sac", "Ciemny niebieski", 0x1A3F7A),
    SLIME_BALL("slime_ball", "slime_ball", "Ciemny zielony", 0x2E7D32),
    WHEAT("wheat", "wheat", "Pomarańczowy", 0xE07B20),
    TURTLE_SCUTE("turtle_scute", "turtle_scute", "Jasny zielony", 0x6FDD6F),
    FERMENTED_SPIDER_EYE("fermented_spider_eye", "fermented_spider_eye", "Czerwony", 0xB22222),
    FLINT("flint", "flint", "Czarny", 0x2B2B2B);

    /** id skórki uzywane w konfigu i w identyfikatorze modelu txtmiecze:skin_<id> */
    public final String id;
    /** id vanilla itemu, z ktorego tekstury korzystamy (minecraft:item/<vanillaId>) */
    public final String vanillaId;
    /** etykieta po polsku pokazywana w GUI */
    public final String label;
    /** kolor próbki (swatch) rysowanej obok nazwy w GUI */
    public final int swatch;

    Skin(String id, String vanillaId, String label, int swatch) {
        this.id = id;
        this.vanillaId = vanillaId;
        this.label = label;
        this.swatch = swatch;
    }

    public static Skin byId(String id) {
        if (id == null) return null;
        for (Skin s : values()) if (s.id.equals(id)) return s;
        return null;
    }
}
