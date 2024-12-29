package com.muxiu1997.sharewhereiam.localization;

import net.minecraft.client.resources.I18n;

public class Lang {

    public static LocalizedString SAVE_WAYPOINT_USAGE;
    public static LocalizedString SAVE_WAYPOINT_SUCCESS;
    public static LocalizedString SAVE_WAYPOINT_EXISTS;
    public static LocalizedString SHARE_WHERE_I_AM_USAGE;
    public static LocalizedString TOGGLE_TEMP_BEACON_USAGE;
    public static LocalizedString ERROR_INVALID_WAYPOINT_CODE;
    public static LocalizedString SHARE_WAYPOINT_MSG;
    public static LocalizedString SHARE_WAYPOINT_SAVE;
    public static LocalizedString SHARE_WAYPOINT_SAVE_DESC;
    public static LocalizedString SHARE_WAYPOINT_EDIT;
    public static LocalizedString SHARE_WAYPOINT_EDIT_DESC;
    public static LocalizedString SHARE_WAYPOINT_TOGGLE;
    public static LocalizedString SHARE_WAYPOINT_TOGGLE_DESC;
    public static LocalizedString SHARE_WAYPOINT_VP;
    public static LocalizedString KEYBINDING_SHARE_NAME;
    public static LocalizedString KEYBINDING_SHARE_CATEGORY;
    public static LocalizedString TEXT_DEFAULT_WAYPOINT_NAME;
    public static LocalizedString TEXT_JM_WAYPOINT_BUTTON;

    private static final String PREFIX_COMMAND = "sharewhereiam.command";
    private static final String PREFIX_COMMAND_ERROR = "sharewhereiam.commanderror";
    private static final String PREFIX_CHAT = "sharewhereiam.chat";
    private static final String PREFIX_KEYBINDING = "sharewhereiam.keybinding";
    private static final String PREFIX_TEXT = "sharewhereiam.text";

    static {
        SAVE_WAYPOINT_USAGE = new LocalizedString(PREFIX_COMMAND + ".savewaypoint.usage");
        SAVE_WAYPOINT_SUCCESS = new LocalizedString(PREFIX_COMMAND + ".savewaypoint.save.success");
        SAVE_WAYPOINT_EXISTS = new LocalizedString(PREFIX_COMMAND + ".savewaypoint.save.exists");
        SHARE_WHERE_I_AM_USAGE = new LocalizedString(PREFIX_COMMAND + ".sharewhereiam.usage");
        TOGGLE_TEMP_BEACON_USAGE = new LocalizedString(PREFIX_COMMAND + ".toggletempbeacon.usage");
        ERROR_INVALID_WAYPOINT_CODE = new LocalizedString(PREFIX_COMMAND_ERROR + ".invalidcode");
        SHARE_WAYPOINT_MSG = new LocalizedString(PREFIX_CHAT + ".share");
        SHARE_WAYPOINT_SAVE = new LocalizedString(PREFIX_CHAT + ".save");
        SHARE_WAYPOINT_SAVE_DESC = new LocalizedString(PREFIX_CHAT + ".save.desc");
        SHARE_WAYPOINT_EDIT = new LocalizedString(PREFIX_CHAT + ".edit");
        SHARE_WAYPOINT_EDIT_DESC = new LocalizedString(PREFIX_CHAT + ".edit.desc");
        SHARE_WAYPOINT_TOGGLE = new LocalizedString(PREFIX_CHAT + ".toggle");
        SHARE_WAYPOINT_TOGGLE_DESC = new LocalizedString(PREFIX_CHAT + ".toggle.desc");
        SHARE_WAYPOINT_VP = new LocalizedString(PREFIX_CHAT + ".additionalInformation.VP");
        KEYBINDING_SHARE_NAME = new LocalizedString(PREFIX_KEYBINDING + ".share.name");
        KEYBINDING_SHARE_CATEGORY = new LocalizedString(PREFIX_KEYBINDING + ".share.category");
        TEXT_DEFAULT_WAYPOINT_NAME = new LocalizedString(PREFIX_TEXT + ".default_waypoint_name");
        TEXT_JM_WAYPOINT_BUTTON = new LocalizedString(PREFIX_TEXT + ".jm.waypoint.share");
    }

    public static class LocalizedString {

        private final String key;

        public LocalizedString(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public String translate() {
            return I18n.format(key);
        }
    }
}
