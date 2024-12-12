package com.muxiu1997.sharewhereiam.mixins;

public enum TargetedMod {

    VANILLA("Minecraft", null),
    JOURNEYMAP("JourneyMap", null, "journeymap"),
    XAEROMINIMAP("Xaero's Minimap", "xaero.common.core.XaeroMinimapPlugin", "XaeroMinimap"),
    XAEROWORLDMAP("Xaero's World Map", "xaero.map.core.XaeroWorldMapPlugin", "XaeroWorldMap"),
    NAVIGATOR("Navigator","com.gtnewhorizons.navigator.core","navigator");

    /** The "name" in the @Mod annotation */
    public final String modName;
    /** Class that implements the IFMLLoadingPlugin interface */
    public final String coreModClass;
    /** The "modid" in the @Mod annotation */
    public final String modId;

    TargetedMod(String modName, String coreModClass) {
        this(modName, coreModClass, null);
    }

    TargetedMod(String modName, String coreModClass, String modId) {
        this.modName = modName;
        this.coreModClass = coreModClass;
        this.modId = modId;
    }

    @Override
    public String toString() {
        return "TargetedMod{modName='" + modName + "', coreModClass='" + coreModClass + "', modId='" + modId + "'}";
    }
}
