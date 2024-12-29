package com.muxiu1997.sharewhereiam.network;

import static com.muxiu1997.sharewhereiam.network.NetworkHandler.network;

import com.muxiu1997.sharewhereiam.integration.Mods;
import com.muxiu1997.sharewhereiam.util.WaypointUtil;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class MessageShareWaypoint implements IMessage {

    public String playerName;
    public String waypointJson;
    public String additionalInformation;

    public MessageShareWaypoint() {}

    // Client-side constructor
    @SideOnly(Side.CLIENT)
    public MessageShareWaypoint(WaypointUtil.PlayerWaypoint playerWaypoint) {
        this(playerWaypoint, "");
    }

    // Client-side constructor with additional information
    @SideOnly(Side.CLIENT)
    public MessageShareWaypoint(WaypointUtil.PlayerWaypoint playerWaypoint, String additionalInformation) {
        this.playerName = playerWaypoint.player.getDisplayName();
        this.waypointJson = playerWaypoint.waypoint.toString();
        this.additionalInformation = additionalInformation;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerName = ByteBufUtils.readUTF8String(buf);
        waypointJson = ByteBufUtils.readUTF8String(buf);
        additionalInformation = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerName);
        ByteBufUtils.writeUTF8String(buf, waypointJson);
        ByteBufUtils.writeUTF8String(buf, additionalInformation);
    }

    public static class Handler implements IMessageHandler<MessageShareWaypoint, IMessage> {

        @Override
        public IMessage onMessage(MessageShareWaypoint message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                handleClientSideMessage(message);
            } else {
                handleServerSideMessage(message);
            }
            return null;
        }

        // Client-side handler for displaying the waypoint information
        @SideOnly(Side.CLIENT)
        private static void handleClientSideMessage(MessageShareWaypoint message) {
            if (Mods.JourneyMap.isLoaded()) {
                WaypointUtil.addShareWaypointChat(
                        message.playerName,
                        WaypointUtil.waypointFromString(message.waypointJson), // Dereferences JourneyMap class import
                                                                               // to stop crashes on clients that don't
                                                                               // have JourneyMap installed.
                        message.additionalInformation);
            }
        }

        // Server-side handler for sending the message to all clients
        private static void handleServerSideMessage(MessageShareWaypoint message) {
            network.sendToAll(message);
        }
    }
}
