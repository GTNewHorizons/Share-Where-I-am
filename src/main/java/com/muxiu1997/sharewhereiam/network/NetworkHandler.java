package com.muxiu1997.sharewhereiam.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHandler {

    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("sharewhereiam");

    // Define empty interfaces for client and server-side handlers
    public interface IServerSideHandler {
    }

    public interface IClientSideHandler {
    }
}
