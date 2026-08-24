package com.muxiu1997.sharewhereiam.integration.journeymap;

import cpw.mods.fml.common.Loader;

/** Detects the incompatible JourneyMap generations before late mixins are selected. */
public enum JourneyMapVersion {

    NONE,
    V5,
    V6;

    private static final JourneyMapVersion CURRENT = detect();

    public static JourneyMapVersion get() {
        return CURRENT;
    }

    private static JourneyMapVersion detect() {
        if (!Loader.isModLoaded("journeymap")) return NONE;
        ClassLoader classLoader = JourneyMapVersion.class.getClassLoader();
        if (classLoader.getResource("journeymap/api/v2/client/IClientAPI.class") != null) return V6;
        if (classLoader.getResource("journeymap/client/render/map/GridRenderer.class") != null) return V5;
        return NONE;
    }
}
