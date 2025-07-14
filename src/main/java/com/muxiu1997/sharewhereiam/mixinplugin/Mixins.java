package com.muxiu1997.sharewhereiam.mixinplugin;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {

    // spotless:off
    JOURNEYMAP_INTEGRATION(new MixinBuilder()
        .addRequiredMod(TargetedMod.JOURNEYMAP)
        .setPhase(Phase.LATE)
        .addClientMixins(
            "journeymap.MixinFullscreen",
            "journeymap.MixinRenderWaypointBeacon",
            "journeymap.MixinWaypointManagerItem",
            "journeymap.MixinWaypointStore")),
    NAVIGATOR_INTEGRATION(new MixinBuilder()
        .addRequiredMod(TargetedMod.NAVIGATOR)
        .setPhase(Phase.LATE)
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
