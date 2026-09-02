package com.muxiu1997.sharewhereiam.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.muxiu1997.sharewhereiam.command.base.CommandWaypointBase;
import com.muxiu1997.sharewhereiam.integration.journeymap.JourneyMapIntegration;
import com.muxiu1997.sharewhereiam.integration.journeymap.JourneyMapIntegration.SaveResult;
import com.muxiu1997.sharewhereiam.localization.Lang;
import com.muxiu1997.sharewhereiam.model.SharedWaypoint;

public class CommandWaypointSave extends CommandWaypointBase {

    public CommandWaypointSave() {
        super("savewaypoint");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return Lang.SAVE_WAYPOINT_USAGE.getKey();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ensureArgsLength(args, 1, 2);
        final SharedWaypoint waypoint = parseWaypoint(args[0]);
        boolean openWaypointEditor = parseOpenWaypointEditor(sender, args);
        SaveResult result = JourneyMapIntegration.saveWaypoint(waypoint, openWaypointEditor);
        if (result == SaveResult.EDITOR_OPENED) return;
        if (result == SaveResult.EXISTS) {
            sender.addChatMessage(new ChatComponentText(Lang.SAVE_WAYPOINT_EXISTS.translate()));
            return;
        }
        if (result == SaveResult.SAVED) {
            sender.addChatMessage(new ChatComponentText(Lang.SAVE_WAYPOINT_SUCCESS.translate()));
            return;
        }
        throw getCommandException();
    }

    private boolean parseOpenWaypointEditor(ICommandSender sender, String[] args) throws CommandException {
        boolean openWaypointEditor = true;
        if (args.length == 2) {
            openWaypointEditor = CommandBase.parseBoolean(sender, args[1]);
        }
        return openWaypointEditor;
    }
}
