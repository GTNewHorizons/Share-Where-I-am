package com.muxiu1997.sharewhereiam.integration.journeymap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;

import com.muxiu1997.sharewhereiam.model.SharedWaypoint;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WaypointManager {

    public static final long TRANSIENT_BEACON_DURATION = 3000L;

    private static SharedWaypoint tempBeacon = null;
    private static final Map<String, TransientBeacon> transientBeaconCache = new HashMap<>();

    @Nullable
    public static SharedWaypoint getTempBeacon() {
        return tempBeacon;
    }

    public static boolean hasActiveTempBeacon() {
        return tempBeacon != null;
    }

    public static void clearActiveTempBeacon() {
        tempBeacon = null;
        JourneyMapIntegration.setTemporaryBeacon(null);
    }

    public static void toggleActiveTempBeacon(SharedWaypoint waypoint) {
        if (waypoint.equals(tempBeacon)) {
            clearActiveTempBeacon();
            return;
        }
        tempBeacon = waypoint;
        JourneyMapIntegration.setTemporaryBeacon(waypoint);
    }

    public static void addTransientBeacon(String playerName, SharedWaypoint waypoint) {
        transientBeaconCache.put(playerName, new TransientBeacon(waypoint, Minecraft.getSystemTime()));
        JourneyMapIntegration.addTransientBeacon(playerName, waypoint);
    }

    public static List<SharedWaypoint> getTransientBeacons() {
        long currentTime = Minecraft.getSystemTime();
        transientBeaconCache.entrySet()
                .removeIf(entry -> currentTime - entry.getValue().start > TRANSIENT_BEACON_DURATION);
        return transientBeaconCache.values().stream().map(transientBeacon -> transientBeacon.waypoint)
                .collect(Collectors.toList());
    }

    public static void clearTransientBeacons() {
        transientBeaconCache.clear();
        JourneyMapIntegration.clearTransientBeacons();
    }

    public static class TransientBeacon {

        public final SharedWaypoint waypoint;
        public final long start;

        public TransientBeacon(SharedWaypoint waypoint, long start) {
            this.waypoint = waypoint;
            this.start = start;
        }
    }
}
