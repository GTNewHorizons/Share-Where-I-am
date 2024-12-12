package com.muxiu1997.sharewhereiam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.muxiu1997.sharewhereiam.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(
        modid = "sharewhereiam",
        version = Tags.VERSION,
        name = "Share Where I am",
        acceptedMinecraftVersions = "[1.7.10]",
        dependencies = "required-after:gtnhmixins")
public class ShareWhereIAm {

    @SuppressWarnings("unused")
    public static final Logger logger = LogManager.getLogger("sharewhereiam");

    @SidedProxy(
            clientSide = "com.muxiu1997.sharewhereiam.proxy.ClientProxy",
            serverSide = "com.muxiu1997.sharewhereiam.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
