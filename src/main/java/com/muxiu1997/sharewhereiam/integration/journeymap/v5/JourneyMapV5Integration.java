package com.muxiu1997.sharewhereiam.integration.journeymap.v5;

import com.muxiu1997.sharewhereiam.integration.journeymap.JourneyMapIntegration.SaveResult;
import com.muxiu1997.sharewhereiam.mixinplugin.interfaces.IMixinWaypointStore;
import com.muxiu1997.sharewhereiam.model.SharedWaypoint;

import journeymap.client.model.Waypoint;
import journeymap.client.ui.UIManager;
import journeymap.client.waypoint.WaypointStore;

/** JourneyMap 5-only implementation backed by its legacy internal classes. */
public final class JourneyMapV5Integration {

    private JourneyMapV5Integration() {}

    public static SaveResult saveWaypoint(SharedWaypoint waypoint, boolean openEditor) {
        Waypoint journeyMapWaypoint = toWaypoint(waypoint);
        if (openEditor) {
            UIManager.getInstance().openWaypointEditor(journeyMapWaypoint, true, null);
            return SaveResult.EDITOR_OPENED;
        }
        if (((IMixinWaypointStore) WaypointStore.instance()).exists(journeyMapWaypoint)) {
            return SaveResult.EXISTS;
        }
        WaypointStore.instance().save(journeyMapWaypoint);
        return SaveResult.SAVED;
    }

    public static Waypoint toWaypoint(SharedWaypoint waypoint) {
        return Waypoint.fromString(waypoint.toString());
    }

    public static SharedWaypoint fromWaypoint(Waypoint waypoint) {
        return SharedWaypoint.fromString(waypoint.toString());
    }
}
