package com.muxiu1997.sharewhereiam.client.key;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.muxiu1997.sharewhereiam.localization.Lang;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class KeyBindings {

    public static final KeyBinding WaypointShare;

    static {
        WaypointShare = new KeyBinding(
                Lang.KEYBINDING_SHARE_NAME.getKey(),
                Keyboard.KEY_INSERT,
                Lang.KEYBINDING_SHARE_CATEGORY.getKey());
    }

    private KeyBindings() {}

    public static void init() {
        ClientRegistry.registerKeyBinding(WaypointShare);
    }
}
