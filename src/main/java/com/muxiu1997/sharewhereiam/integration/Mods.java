package com.muxiu1997.sharewhereiam.integration;

import com.muxiu1997.sharewhereiam.integration.journeymap.JourneyMapVersion;

import cpw.mods.fml.common.Loader;

public enum Mods {

    VisualProspecting("visualprospecting"),
    JourneyMap("journeymap"),
    Navigator("navigator"),

    ;

    public final String modid;
    private Boolean loaded;

    Mods(String modid) {
        this.modid = modid;
    }

    public boolean isLoaded() {
        if (loaded == null) {
            loaded = Loader.isModLoaded(modid)
                    && (this != JourneyMap || JourneyMapVersion.get() != JourneyMapVersion.NONE);
        }
        return loaded;
    }

    public static boolean isEnabled() {
        return !Loader.isModLoaded("journeymap") || JourneyMapVersion.get() != JourneyMapVersion.NONE;
    }
}
