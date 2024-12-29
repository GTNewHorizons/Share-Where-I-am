package com.muxiu1997.sharewhereiam.network;

import static com.muxiu1997.sharewhereiam.network.NetworkHandler.network;

import com.muxiu1997.sharewhereiam.integration.journeymap.WaypointManager;
import com.muxiu1997.sharewhereiam.util.WaypointUtil;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import journeymap.client.model.Waypoint;

public class MessageMarkWaypoint implements IMessage {

    private String playerName;
    private String waypointJson;

    // Default constructor
    public MessageMarkWaypoint() {}

    // Client-side constructor
    @SideOnly(Side.CLIENT)
    public MessageMarkWaypoint(WaypointUtil.PlayerWaypoint playerWaypoint) {
        this.playerName = playerWaypoint.player.getDisplayName();
        this.waypointJson = playerWaypoint.waypoint.toString();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerName = ByteBufUtils.readUTF8String(buf);
        waypointJson = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerName);
        ByteBufUtils.writeUTF8String(buf, waypointJson);
    }

    public static class Handler implements IMessageHandler<MessageMarkWaypoint, IMessage>,
            NetworkHandler.IClientSideHandler, NetworkHandler.IServerSideHandler {

        @Override
        public IMessage onMessage(MessageMarkWaypoint message, MessageContext ctx) {
            switch (ctx.side) {
                case CLIENT:
                    handleClientSideMessage(message);
                    break;
                case SERVER:
                    handleServerSideMessage(message);
                    break;
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClientSideMessage(MessageMarkWaypoint message) {
            WaypointManager.addTransientBeacon(message.playerName, Waypoint.fromString(message.waypointJson));
        }

        private void handleServerSideMessage(MessageMarkWaypoint message) {
            // Assuming 'network' is properly initialized and refers to the network instance to send the message
            network.sendToAll(message);
        }
    }
}
