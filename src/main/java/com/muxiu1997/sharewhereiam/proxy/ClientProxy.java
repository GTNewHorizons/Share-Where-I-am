package com.muxiu1997.sharewhereiam.proxy;

import net.minecraftforge.client.ClientCommandHandler;

import com.muxiu1997.sharewhereiam.client.key.KeyBindings;
import com.muxiu1997.sharewhereiam.command.CommandWaypointSave;
import com.muxiu1997.sharewhereiam.command.CommandWaypointShareLocation;
import com.muxiu1997.sharewhereiam.command.CommandWaypointToggleTempBeacon;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        KeyBindings.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        ClientCommandHandler.instance.registerCommand(new CommandWaypointSave());
        ClientCommandHandler.instance.registerCommand(new CommandWaypointShareLocation());
        ClientCommandHandler.instance.registerCommand(new CommandWaypointToggleTempBeacon());
    }
}
