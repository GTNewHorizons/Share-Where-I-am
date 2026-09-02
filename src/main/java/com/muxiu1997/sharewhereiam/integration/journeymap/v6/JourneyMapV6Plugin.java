package com.muxiu1997.sharewhereiam.integration.journeymap.v6;

import static com.muxiu1997.sharewhereiam.network.NetworkHandler.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import org.lwjgl.input.Keyboard;

import com.muxiu1997.sharewhereiam.client.key.KeyBindings;
import com.muxiu1997.sharewhereiam.integration.Mods;
import com.muxiu1997.sharewhereiam.integration.journeymap.JourneyMapIntegration.SaveResult;
import com.muxiu1997.sharewhereiam.integration.journeymap.WaypointManager;
import com.muxiu1997.sharewhereiam.localization.Lang;
import com.muxiu1997.sharewhereiam.model.SharedWaypoint;
import com.muxiu1997.sharewhereiam.network.MessageShareWaypoint;
import com.muxiu1997.sharewhereiam.util.VPWaypointUtil;
import com.muxiu1997.sharewhereiam.util.WaypointUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.client.event.PopupMenuEvent;
import journeymap.api.v2.client.util.UIState;
import journeymap.api.v2.common.Context;
import journeymap.api.v2.common.JourneyMapPlugin;
import journeymap.api.v2.common.event.FullscreenEventRegistry;
import journeymap.api.v2.common.util.BlockPos;
import journeymap.api.v2.common.waypoint.Waypoint;
import journeymap.api.v2.common.waypoint.WaypointFactory;
import journeymap.api.v2.common.waypoint.WaypointGroup;

/** JourneyMap 6 integration implemented exclusively through its v2 plugin API. */
@JourneyMapPlugin(apiVersion = IClientAPI.API_VERSION, dependencies = "sharewhereiam")
public final class JourneyMapV6Plugin implements IClientPlugin {

    private static final String MOD_ID = "sharewhereiam";
    private static final String TEMPORARY_GROUP_NAME = "Share Where I Am temporary";

    private static IClientAPI api;
    private static @Nullable Waypoint temporaryBeacon;
    /** Client thread only: mutated through {@link WaypointManager} and pruned from the client tick. */
    private static final Map<String, TimedWaypoint> transientBeacons = new HashMap<>();

    private boolean shareKeyDown;

    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public void initialize(IClientAPI clientApi) {
        api = clientApi;
        FullscreenEventRegistry.WAYPOINT_POPUP_MENU_EVENT.subscribe(MOD_ID, this::onWaypointPopupMenu);
        FMLCommonHandler.instance().bus().register(this);
    }

    public static SaveResult saveWaypoint(SharedWaypoint waypoint) {
        if (api == null) return SaveResult.UNAVAILABLE;

        Waypoint converted = createWaypoint(waypoint, true, true);
        for (Waypoint existing : api.getAllWaypoints()) {
            if (isDuplicate(existing, converted)) return SaveResult.EXISTS;
        }
        api.addWaypoint(MOD_ID, converted);
        return SaveResult.SAVED;
    }

    public static boolean isFullscreenMapActive() {
        if (api == null) return false;
        UIState state = api.getUIState(Context.UI.Fullscreen);
        return state != null && state.active;
    }

    public static void setTemporaryBeacon(@Nullable SharedWaypoint waypoint) {
        removeWaypoint(temporaryBeacon);
        temporaryBeacon = waypoint == null ? null : createWaypoint(waypoint, false, false);
        if (temporaryBeacon != null) addTemporaryWaypoint(temporaryBeacon);
    }

    public static void addTransientBeacon(String playerName, SharedWaypoint waypoint) {
        TimedWaypoint previous = transientBeacons.remove(playerName);
        if (previous != null) removeWaypoint(previous.waypoint);
        Waypoint converted = createWaypoint(waypoint, false, false);
        transientBeacons.put(playerName, new TimedWaypoint(converted, Minecraft.getSystemTime()));
        addTemporaryWaypoint(converted);
    }

    public static void clearTransientBeacons() {
        for (TimedWaypoint timed : transientBeacons.values()) removeWaypoint(timed.waypoint);
        transientBeacons.clear();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        cleanupTransientBeacons();
        handleNavigatorWaypointShare();
    }

    private void onWaypointPopupMenu(PopupMenuEvent.WaypointPopupMenuEvent event) {
        Waypoint waypoint = event.getWaypoint();
        event.getPopupMenu()
                .addMenuItem(Lang.TEXT_JM_WAYPOINT_BUTTON.translate(), ignored -> shareWaypoint(waypoint, ""));
    }

    private void handleNavigatorWaypointShare() {
        int keyCode = KeyBindings.WaypointShare.getKeyCode();
        boolean down = keyCode >= 0 && keyCode < Keyboard.KEYBOARD_SIZE && Keyboard.isKeyDown(keyCode);
        if (down && !shareKeyDown
                && isFullscreenMapActive()
                && Mods.VisualProspecting.isLoaded()
                && Mods.Navigator.isLoaded()) {
            SharedWaypoint waypoint = VPWaypointUtil.getHoveredWaypoint();
            if (waypoint != null) shareWaypoint(waypoint, Lang.SHARE_WAYPOINT_VP.translate());
        }
        shareKeyDown = down;
    }

    private static void shareWaypoint(Waypoint waypoint, String additionalInformation) {
        int dimension;
        try {
            dimension = Integer.parseInt(waypoint.getPrimaryDimension());
        } catch (NumberFormatException ignored) {
            dimension = Minecraft.getMinecraft().thePlayer.dimension;
        }
        TreeSet<Integer> dimensions = new TreeSet<>();
        for (String value : waypoint.getDimensions()) {
            try {
                dimensions.add(Integer.parseInt(value));
            } catch (NumberFormatException ignored) {}
        }
        BlockPos position = waypoint.getBlockPos();
        shareWaypoint(
                new SharedWaypoint(
                        waypoint.getName(),
                        position.getX(),
                        position.getY(),
                        position.getZ(),
                        waypoint.getColor(),
                        dimension,
                        waypoint.isEnabled(),
                        dimensions),
                additionalInformation);
    }

    private static void shareWaypoint(SharedWaypoint waypoint, String additionalInformation) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            network.sendToServer(
                    new MessageShareWaypoint(new WaypointUtil.PlayerWaypoint(player, waypoint), additionalInformation));
        }
    }

    private static Waypoint createWaypoint(SharedWaypoint waypoint, boolean persistent, boolean showOnMap) {
        Waypoint converted = WaypointFactory.createWaypoint(
                MOD_ID,
                new BlockPos(waypoint.getX(), waypoint.getY(), waypoint.getZ()),
                waypoint.getName(),
                waypoint.getPrimaryDimension(),
                persistent);
        converted.setColor(waypoint.getColor());
        converted.setEnabled(waypoint.isEnabled());
        TreeSet<String> dimensions = new TreeSet<>();
        for (Integer dimension : waypoint.getDimensions()) dimensions.add(String.valueOf(dimension));
        converted.setDimensions(dimensions);
        converted.setShowOnMap(showOnMap);
        converted.setShowInWorld(true);
        converted.setShowBeacon(true);
        return converted;
    }

    /** Mirrors JM6's WaypointStore duplicate check. */
    private static boolean isDuplicate(Waypoint existing, Waypoint candidate) {
        return existing.getX() == candidate.getX() && existing.getY() == candidate.getY()
                && existing.getZ() == candidate.getZ()
                && Objects.equals(existing.getGroupId(), candidate.getGroupId())
                && Objects.equals(existing.getPrimaryDimension(), candidate.getPrimaryDimension());
    }

    private static void cleanupTransientBeacons() {
        long cutoff = Minecraft.getSystemTime() - WaypointManager.TRANSIENT_BEACON_DURATION;
        for (TimedWaypoint timed : transientBeacons.values()) {
            if (timed.start < cutoff) removeWaypoint(timed.waypoint);
        }
        transientBeacons.values().removeIf(timed -> timed.start < cutoff);
    }

    private static void removeWaypoint(@Nullable Waypoint waypoint) {
        if (api != null && waypoint != null) api.removeWaypoint(MOD_ID, waypoint);
    }

    private static void addTemporaryWaypoint(Waypoint waypoint) {
        if (api == null) return;

        WaypointGroup group = api.getWaypointGroupByName(MOD_ID, TEMPORARY_GROUP_NAME);
        if (group == null) {
            WaypointGroup created = WaypointFactory.createWaypointGroup(MOD_ID, TEMPORARY_GROUP_NAME);
            created.setPersistent(false);
            api.addWaypointGroup(created);
            group = api.getWaypointGroup(created.getGuid());
        }

        api.addWaypoint(MOD_ID, waypoint);
        Waypoint added = api.getWaypoint(MOD_ID, waypoint.getGuid());
        if (added != null && group != null) group.addWaypoint(added);
    }

    private static final class TimedWaypoint {

        private final Waypoint waypoint;
        private final long start;

        private TimedWaypoint(Waypoint waypoint, long start) {
            this.waypoint = waypoint;
            this.start = start;
        }
    }
}
