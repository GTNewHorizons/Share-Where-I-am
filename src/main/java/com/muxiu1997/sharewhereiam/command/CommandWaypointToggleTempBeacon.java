package com.muxiu1997.sharewhereiam.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.muxiu1997.sharewhereiam.command.base.CommandWaypointBase;
import com.muxiu1997.sharewhereiam.integration.journeymap.WaypointManager;
import com.muxiu1997.sharewhereiam.localization.Lang;

import journeymap.client.model.Waypoint;

public class CommandWaypointToggleTempBeacon extends CommandWaypointBase {

    public CommandWaypointToggleTempBeacon() {
        super("toggletempbeacon");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return Lang.TOGGLE_TEMP_BEACON_USAGE.getKey();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ensureArgsLength(args, 0, 1);
        if (args.length == 0) {
            WaypointManager.clearActiveTempBeacon();
            return;
        }
        Waypoint waypoint = parseWaypoint(args[0]);
        WaypointManager.toggleActiveTempBeacon(waypoint);
    }
}
