package com.muxiu1997.sharewhereiam.command.base;

import static com.muxiu1997.sharewhereiam.util.WaypointUtil.fromBase64;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.muxiu1997.sharewhereiam.command.CommandError;

import journeymap.client.model.Waypoint;

public abstract class CommandWaypointBase extends CommandBase {

    final protected String commandName;

    public CommandWaypointBase(@Nonnull String commandName) {
        super();
        this.commandName = commandName;
    }

    @Override
    @Nonnull
    public String getCommandName() {
        return this.commandName;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (!(sender instanceof AbstractClientPlayer)) return false;
        return super.canCommandSenderUseCommand(sender);
    }

    @Override
    @Nonnull
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Nonnull
    protected Waypoint parseWaypoint(@Nonnull String waypointCode) throws CommandException {
        final Waypoint waypoint;
        try {
            waypoint = fromBase64(waypointCode);
        } catch (Exception ignored) {
            throw CommandError.INVALID_WAYPOINT_CODE.exception();
        }
        return waypoint;
    }

    protected void ensureArgsLength(@Nullable String[] args, int min, int max) throws CommandException {
        final int argsLength = args != null ? args.length : 0;
        if (argsLength < min || argsLength > max) throw getCommandException();
    }

    protected void ensureArgsLength(@Nullable String[] args, int length) throws CommandException {
        final int argsLength = args != null ? args.length : 0;
        if (argsLength != length) throw getCommandException();
    }

    @Nonnull
    public CommandException getCommandException() {
        return new CommandException("sharewhereiam.command." + this.getCommandName() + ".usage");
    }
}
