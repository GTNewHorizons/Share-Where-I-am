package com.muxiu1997.sharewhereiam;

import com.muxiu1997.sharewhereiam.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(
        modid = "sharewhereiam",
        version = Tags.VERSION,
        name = "Share Where I am",
        acceptedMinecraftVersions = "[1.7.10]",
        dependencies = "required-after:gtnhmixins")
public class ShareWhereIAm {

    @SidedProxy(
            clientSide = "com.muxiu1997.sharewhereiam.proxy.ClientProxy",
            serverSide = "com.muxiu1997.sharewhereiam.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
