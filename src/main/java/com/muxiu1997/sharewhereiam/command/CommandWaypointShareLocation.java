package com.muxiu1997.sharewhereiam.command;

import static com.muxiu1997.sharewhereiam.network.NetworkHandler.network;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.muxiu1997.sharewhereiam.command.base.CommandWaypointBase;
import com.muxiu1997.sharewhereiam.localization.Lang;
import com.muxiu1997.sharewhereiam.network.MessageShareWaypoint;
import com.muxiu1997.sharewhereiam.util.WaypointUtil;

public class CommandWaypointShareLocation extends CommandWaypointBase {

    public CommandWaypointShareLocation() {
        super("sharewhereiam");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return Lang.SHARE_WHERE_I_AM_USAGE.getKey();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ensureArgsLength(args, 0);
        if (sender instanceof EntityPlayer) {
            network.sendToServer(new MessageShareWaypoint(WaypointUtil.waypointOfLocation()));
        }
    }
}
