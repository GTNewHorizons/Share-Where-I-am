package com.muxiu1997.sharewhereiam.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.gtnewhorizons.navigator.api.NavigatorApi;
import com.gtnewhorizons.navigator.api.model.SupportedMods;
import com.gtnewhorizons.navigator.api.model.layers.LayerRenderer;
import com.gtnewhorizons.navigator.api.model.layers.UniversalInteractableRenderer;
import com.gtnewhorizons.navigator.api.model.steps.UniversalInteractableStep;
import com.gtnewhorizons.navigator.api.model.waypoints.Waypoint;
import com.muxiu1997.sharewhereiam.mixins.late.navigator.UniversalInteractableRendererAccessor;
import com.muxiu1997.sharewhereiam.model.SharedWaypoint;

import cpw.mods.fml.common.Optional;

public class VPWaypointUtil {

    @Nullable
    @Optional.Method(modid = "navigator")
    public static SharedWaypoint getHoveredWaypoint() {
        for (LayerRenderer layer : NavigatorApi.getActiveRenderersFor(SupportedMods.JourneyMap)) {
            if (layer instanceof UniversalInteractableRenderer) {
                UniversalInteractableStep<?> hoveredRenderStep = ((UniversalInteractableRendererAccessor) layer)
                        .getHoveredRenderStep();
                if (hoveredRenderStep != null) {
                    final Waypoint waypoint = hoveredRenderStep.getLocation().toWaypoint();
                    if (waypoint != null) {
                        return fromNavigatorWaypoint(waypoint);
                    }
                }
            }
        }
        return null;
    }

    @Nonnull
    @Optional.Method(modid = "navigator")
    public static SharedWaypoint fromNavigatorWaypoint(@Nonnull Waypoint waypoint) {
        return new SharedWaypoint(
                waypoint.label,
                waypoint.blockX,
                waypoint.blockY,
                waypoint.blockZ,
                waypoint.color,
                waypoint.dimensionId);
    }
}
