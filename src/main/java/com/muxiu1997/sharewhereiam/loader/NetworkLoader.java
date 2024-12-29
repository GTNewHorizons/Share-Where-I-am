package com.muxiu1997.sharewhereiam.loader;

import static com.muxiu1997.sharewhereiam.network.NetworkHandler.network;

import com.muxiu1997.sharewhereiam.network.MessageMarkWaypoint;
import com.muxiu1997.sharewhereiam.network.MessageShareWaypoint;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.relauncher.Side;

public class NetworkLoader {

    private static int nextID = 0;

    public static void load() {
        register(MessageShareWaypoint.Handler.class, MessageShareWaypoint.class);
        register(MessageMarkWaypoint.Handler.class, MessageMarkWaypoint.class);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void register(
            Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType) {
        network.registerMessage(messageHandler, requestMessageType, nextID++, Side.SERVER);
        network.registerMessage(messageHandler, requestMessageType, nextID++, Side.CLIENT);
    }
}
