package com.muxiu1997.sharewhereiam.command;

import net.minecraft.command.CommandException;

import com.muxiu1997.sharewhereiam.localization.Lang;

public enum CommandError {

    INVALID_WAYPOINT_CODE(Lang.ERROR_INVALID_WAYPOINT_CODE.getKey());

    private final String localizationKey;

    CommandError(String localizationKey) {
        this.localizationKey = localizationKey;
    }

    public CommandException exception() {
        return new CommandException(localizationKey);
    }
}
