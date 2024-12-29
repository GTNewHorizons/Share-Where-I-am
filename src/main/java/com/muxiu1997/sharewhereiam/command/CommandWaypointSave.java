package com.muxiu1997.sharewhereiam.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.muxiu1997.sharewhereiam.command.base.CommandWaypointBase;
import com.muxiu1997.sharewhereiam.localization.Lang;
import com.muxiu1997.sharewhereiam.mixinplugin.interfaces.IMixinWaypointStore;

import journeymap.client.model.Waypoint;
import journeymap.client.ui.UIManager;
import journeymap.client.waypoint.WaypointStore;

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
        final Waypoint waypoint = parseWaypoint(args[0]);
        boolean openWaypointEditor = parseOpenWaypointEditor(sender, args);
        if (openWaypointEditor) {
            UIManager.getInstance().openWaypointEditor(waypoint, true, null);
            return;
        }
        if (((IMixinWaypointStore) WaypointStore.instance()).exists(waypoint)) {
            sender.addChatMessage(new ChatComponentText(Lang.SAVE_WAYPOINT_EXISTS.translate()));
            return;
        }
        WaypointStore.instance().add(waypoint);
        sender.addChatMessage(new ChatComponentText(Lang.SAVE_WAYPOINT_SUCCESS.translate()));
    }

    private boolean parseOpenWaypointEditor(ICommandSender sender, String[] args) throws CommandException {
        boolean openWaypointEditor = true;
        if (args.length == 2) {
            openWaypointEditor = CommandBase.parseBoolean(sender, args[1]);
        }
        return openWaypointEditor;
    }
}
