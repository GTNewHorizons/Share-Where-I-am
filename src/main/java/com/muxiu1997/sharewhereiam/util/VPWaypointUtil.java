package com.muxiu1997.sharewhereiam.util;

import java.awt.Color;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.gtnewhorizons.navigator.api.NavigatorApi;
import com.gtnewhorizons.navigator.api.model.SupportedMods;
import com.gtnewhorizons.navigator.api.model.layers.LayerRenderer;
import com.gtnewhorizons.navigator.api.model.layers.UniversalInteractableRenderer;
import com.gtnewhorizons.navigator.api.model.steps.UniversalInteractableStep;
import com.gtnewhorizons.navigator.api.model.waypoints.Waypoint;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class VPWaypointUtil {

    @Nullable
    @Optional.Method(modid = "navigator")
    public static journeymap.client.model.Waypoint getHoveredWaypoint() {
        for (LayerRenderer layer : NavigatorApi.getActiveRenderersFor(SupportedMods.JourneyMap)) {
            if (layer instanceof UniversalInteractableRenderer) {
                final Field FieldHoveredDrawStep = ReflectionHelper
                        .findField(UniversalInteractableRenderer.class, "hoveredRenderStep");
                @Nullable
                UniversalInteractableStep<?> hoveredRenderStep = null;
                try {
                    hoveredRenderStep = (UniversalInteractableStep<?>) FieldHoveredDrawStep.get(layer);
                } catch (Exception ignored) {}
                if (hoveredRenderStep != null) {
                    final Waypoint waypoint = hoveredRenderStep.getLocation().toWaypoint();
                    if (waypoint != null) {
                        return getJMWaypoint(waypoint);
                    }
                }
            }
        }
        return null;
    }

    @Nonnull
    @Optional.Method(modid = "navigator")
    public static journeymap.client.model.Waypoint getJMWaypoint(@Nonnull Waypoint waypoint) {
        return new journeymap.client.model.Waypoint(
                waypoint.label,
                waypoint.blockX,
                waypoint.blockY,
                waypoint.blockZ,
                new Color(waypoint.color),
                journeymap.client.model.Waypoint.Type.Normal,
                waypoint.dimensionId);
    }
}
