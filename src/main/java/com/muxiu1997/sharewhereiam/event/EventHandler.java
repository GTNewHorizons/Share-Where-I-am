package com.muxiu1997.sharewhereiam.event;

import static com.muxiu1997.sharewhereiam.network.NetworkHandler.network;
import static com.muxiu1997.sharewhereiam.util.WaypointUtil.waypointOfLocation;
import static com.muxiu1997.sharewhereiam.util.WaypointUtil.waypointOfRayTrace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import org.lwjgl.input.Keyboard;

import com.muxiu1997.sharewhereiam.client.key.KeyBindings;
import com.muxiu1997.sharewhereiam.integration.Mods;
import com.muxiu1997.sharewhereiam.integration.journeymap.WaypointManager;
import com.muxiu1997.sharewhereiam.integration.journeymap.WaypointMarker;
import com.muxiu1997.sharewhereiam.network.MessageMarkWaypoint;
import com.muxiu1997.sharewhereiam.network.MessageShareWaypoint;
import com.muxiu1997.sharewhereiam.util.WaypointUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void handleEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.world.isRemote && Mods.JourneyMap.isLoaded()) {
            if (event.entity instanceof EntityPlayer) {
                WaypointManager.clearActiveTempBeacon();
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void handleKeyInput(InputEvent.KeyInputEvent event) {
        if (!KeyBindings.WaypointShare.isPressed()) return;

        if (Mods.JourneyMap.isLoaded()) {
            // Determine the waypoint based on Shift key state
            WaypointUtil.PlayerWaypoint playerWaypoint;
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                playerWaypoint = waypointOfLocation();
            } else {
                playerWaypoint = waypointOfRayTrace();
            }

            // Send the message based on Control key state
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                network.sendToServer(new MessageShareWaypoint(playerWaypoint));
            } else {
                network.sendToServer(new MessageMarkWaypoint(playerWaypoint));
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void handleRenderWorldLast(RenderWorldLastEvent event) {
        if (Mods.JourneyMap.isLoaded()) {
            WaypointMarker.render(event);
        }
    }

}
