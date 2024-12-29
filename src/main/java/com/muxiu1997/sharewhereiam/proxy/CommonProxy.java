package com.muxiu1997.sharewhereiam.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.muxiu1997.sharewhereiam.event.EventHandler;
import com.muxiu1997.sharewhereiam.loader.IntegrationLoader;
import com.muxiu1997.sharewhereiam.loader.NetworkLoader;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class CommonProxy {

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        FMLCommonHandler.instance().bus().register(new EventHandler());
        IntegrationLoader.load();
        NetworkLoader.load();
    }
}
