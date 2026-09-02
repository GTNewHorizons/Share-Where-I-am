package com.muxiu1997.sharewhereiam.mixinplugin;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;
import com.muxiu1997.sharewhereiam.integration.Mods;
import com.muxiu1997.sharewhereiam.integration.journeymap.JourneyMapVersion;

public enum Mixins implements IMixins {

    // spotless:off
    JOURNEYMAP_V5_INTEGRATION(new MixinBuilder()
        .addRequiredMod(TargetedMod.JOURNEYMAP)
        .setPhase(Phase.LATE)
        .setApplyIf(() -> JourneyMapVersion.get() == JourneyMapVersion.V5)
        .addClientMixins(
            "journeymap.MixinFullscreen",
            "journeymap.MixinRenderWaypointBeacon",
            "journeymap.MixinWaypointManagerItem",
            "journeymap.MixinWaypointStore")),
    NAVIGATOR_INTEGRATION(new MixinBuilder()
        .addRequiredMod(TargetedMod.NAVIGATOR)
        .setPhase(Phase.LATE)
        .setApplyIf(Mods::isEnabled)
        .addClientMixins("navigator.UniversalInteractableRendererAccessor"));
    // spotless:on

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return this.builder;
    }
}
