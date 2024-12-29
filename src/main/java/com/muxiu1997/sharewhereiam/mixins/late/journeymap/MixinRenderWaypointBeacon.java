package com.muxiu1997.sharewhereiam.mixins.late.journeymap;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.muxiu1997.sharewhereiam.integration.journeymap.WaypointManager;

import journeymap.client.model.Waypoint;
import journeymap.client.render.ingame.RenderWaypointBeacon;

@SuppressWarnings("UnusedMixin")
@Mixin(RenderWaypointBeacon.class)
public abstract class MixinRenderWaypointBeacon {

    @Shadow(remap = false)
    static Minecraft mc;

    @Shadow(remap = false)
    static void doRender(Waypoint waypoint) {}

    @Inject(method = "renderAll", at = @At(value = "RETURN", remap = false), remap = false, require = 1)
    private static void inject_renderAll(CallbackInfo callbackInfo) {
        if (WaypointManager.hasActiveTempBeacon()) {
            final Waypoint waypoint = WaypointManager.getTempBeacon();
            assert waypoint != null;
            if (waypoint.getDimensions().contains(mc.thePlayer.dimension)) {
                doRender(waypoint);
            }
        }
        for (Waypoint waypoint : WaypointManager.getTransientBeacons()) {
            doRender(waypoint);
        }
    }
}
