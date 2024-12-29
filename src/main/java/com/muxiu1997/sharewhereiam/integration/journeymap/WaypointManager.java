package com.muxiu1997.sharewhereiam.integration.journeymap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import journeymap.client.model.Waypoint;

@SideOnly(Side.CLIENT)
public class WaypointManager {

    public static Waypoint tempBeacon = null;
    private static final Map<String, TransientBeacon> transientBeaconCache = new HashMap<>();

    @Nullable
    public static Waypoint getTempBeacon() {
        return tempBeacon;
    }

    public static boolean hasActiveTempBeacon() {
        return tempBeacon != null;
    }

    public static void clearActiveTempBeacon() {
        tempBeacon = null;
    }

    public static void toggleActiveTempBeacon(Waypoint waypoint) {
        if (waypoint.equals(tempBeacon)) {
            tempBeacon = null;
            return;
        }
        tempBeacon = waypoint;
    }

    public static void addTransientBeacon(String playerName, Waypoint waypoint) {
        transientBeaconCache.put(playerName, new TransientBeacon(waypoint, Minecraft.getSystemTime()));
    }

    public static List<Waypoint> getTransientBeacons() {
        long currentTime = Minecraft.getSystemTime();
        transientBeaconCache.entrySet().removeIf(entry -> currentTime - entry.getValue().start > 3000);
        return transientBeaconCache.values().stream().map(transientBeacon -> transientBeacon.waypoint)
                .collect(Collectors.toList());
    }

    public static class TransientBeacon {

        public final Waypoint waypoint;
        public final long start;

        public TransientBeacon(Waypoint waypoint, long start) {
            this.waypoint = waypoint;
            this.start = start;
        }
    }
}
