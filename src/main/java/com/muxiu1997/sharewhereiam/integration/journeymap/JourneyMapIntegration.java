package com.muxiu1997.sharewhereiam.integration.journeymap;

import javax.annotation.Nullable;

import com.muxiu1997.sharewhereiam.integration.journeymap.v5.JourneyMapV5Integration;
import com.muxiu1997.sharewhereiam.integration.journeymap.v6.JourneyMapV6Plugin;
import com.muxiu1997.sharewhereiam.model.SharedWaypoint;

/** Runtime bridge that keeps JourneyMap 5 internals and JourneyMap 6 API calls isolated. */
public final class JourneyMapIntegration {

    private JourneyMapIntegration() {}

    public static SaveResult saveWaypoint(SharedWaypoint waypoint, boolean openEditor) {
        if (JourneyMapVersion.get() == JourneyMapVersion.V6) {
            return JourneyMapV6Plugin.saveWaypoint(waypoint);
        }
        if (JourneyMapVersion.get() == JourneyMapVersion.V5) {
            return JourneyMapV5Integration.saveWaypoint(waypoint, openEditor);
        }
        return SaveResult.UNAVAILABLE;
    }

    public static boolean canOpenWaypointEditor() {
        return JourneyMapVersion.get() == JourneyMapVersion.V5;
    }

    public static void setTemporaryBeacon(@Nullable SharedWaypoint waypoint) {
        if (JourneyMapVersion.get() == JourneyMapVersion.V6) JourneyMapV6Plugin.setTemporaryBeacon(waypoint);
    }

    public static void addTransientBeacon(String playerName, SharedWaypoint waypoint) {
        if (JourneyMapVersion.get() == JourneyMapVersion.V6) {
            JourneyMapV6Plugin.addTransientBeacon(playerName, waypoint);
        }
    }

    public static void clearTransientBeacons() {
        if (JourneyMapVersion.get() == JourneyMapVersion.V6) JourneyMapV6Plugin.clearTransientBeacons();
    }

    public enum SaveResult {
        SAVED,
        EDITOR_OPENED,
        EXISTS,
        UNAVAILABLE
    }
}
