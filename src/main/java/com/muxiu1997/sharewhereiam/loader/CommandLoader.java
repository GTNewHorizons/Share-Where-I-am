package com.muxiu1997.sharewhereiam.loader;

import net.minecraftforge.client.ClientCommandHandler;

import com.muxiu1997.sharewhereiam.command.CommandWaypointSave;
import com.muxiu1997.sharewhereiam.command.CommandWaypointShareLocation;
import com.muxiu1997.sharewhereiam.command.CommandWaypointToggleTempBeacon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CommandLoader {

    public static CommandLoader INSTANCE = new CommandLoader();

    @SideOnly(Side.CLIENT)
    public void clientLoad() {
        ClientCommandHandler.instance.registerCommand(CommandWaypointSave.INSTANCE);
        ClientCommandHandler.instance.registerCommand(CommandWaypointShareLocation.INSTANCE);
        ClientCommandHandler.instance.registerCommand(CommandWaypointToggleTempBeacon.INSTANCE);
    }
}
