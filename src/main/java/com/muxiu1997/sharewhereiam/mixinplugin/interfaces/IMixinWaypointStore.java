package com.muxiu1997.sharewhereiam.mixinplugin.interfaces;

import journeymap.client.model.Waypoint;

public interface IMixinWaypointStore {

    boolean exists(Waypoint waypoint);
}
