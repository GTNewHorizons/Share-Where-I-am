package com.muxiu1997.sharewhereiam.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JourneyMap-independent waypoint data using JourneyMap 5's JSON shape for network compatibility.
 */
public final class SharedWaypoint {

    private static final Gson GSON = new GsonBuilder().setVersion(1).create();

    private String id;
    private String name;
    private String icon;
    private int x;
    private int y;
    private int z;
    private int r;
    private int g;
    private int b;
    private boolean enable;
    private Type type;
    private Origin origin;
    private TreeSet<Integer> dimensions;

    @SuppressWarnings("unused")
    private SharedWaypoint() {}

    public SharedWaypoint(String name, int x, int y, int z, int color, int dimension) {
        this(name, x, y, z, color, dimension, true, Collections.singleton(dimension));
    }

    public SharedWaypoint(String name, int x, int y, int z, int color, int dimension, boolean enabled,
            Collection<Integer> dimensions) {
        this.name = name == null ? x + ", " + z : name;
        this.icon = "waypoint-normal.png";
        this.x = dimension == -1 ? x * 8 : x;
        this.y = y;
        this.z = dimension == -1 ? z * 8 : z;
        this.r = color >> 16 & 0xFF;
        this.g = color >> 8 & 0xFF;
        this.b = color & 0xFF;
        this.enable = enabled;
        this.type = Type.Normal;
        this.origin = Origin.JourneyMap;
        this.dimensions = new TreeSet<>(dimensions);
        this.dimensions.add(dimension);
        updateId();
    }

    public static SharedWaypoint fromString(String json) {
        SharedWaypoint waypoint = GSON.fromJson(json, SharedWaypoint.class);
        if (waypoint == null || waypoint.name == null || waypoint.dimensions == null || waypoint.dimensions.isEmpty()) {
            throw new IllegalArgumentException("Invalid waypoint JSON");
        }
        return waypoint;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return getX(getPrimaryDimension());
    }

    public int getX(int dimension) {
        return dimension == -1 ? x / 8 : x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return getZ(getPrimaryDimension());
    }

    public int getZ(int dimension) {
        return dimension == -1 ? z / 8 : z;
    }

    public int getColor() {
        return r << 16 | g << 8 | b;
    }

    public boolean isEnabled() {
        return enable;
    }

    public TreeSet<Integer> getDimensions() {
        return new TreeSet<>(dimensions);
    }

    public int getPrimaryDimension() {
        return dimensions.first();
    }

    private void updateId() {
        id = String.format("%s_%s,%s,%s", name, x, y, z);
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof SharedWaypoint)) return false;
        SharedWaypoint waypoint = (SharedWaypoint) other;
        return x == waypoint.x && y == waypoint.y
                && z == waypoint.z
                && r == waypoint.r
                && g == waypoint.g
                && b == waypoint.b
                && enable == waypoint.enable
                && Objects.equals(id, waypoint.id)
                && Objects.equals(name, waypoint.name)
                && Objects.equals(icon, waypoint.icon)
                && type == waypoint.type
                && origin == waypoint.origin
                && Objects.equals(dimensions, waypoint.dimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, z, dimensions);
    }

    private enum Origin {
        JourneyMap,
        ReiMinimap,
        VoxelMap
    }

    private enum Type {
        Normal,
        Death
    }
}
