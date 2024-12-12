package com.muxiu1997.sharewhereiam.client.key;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.muxiu1997.sharewhereiam.localization.Lang;

public class KeyShare {

    public static final KeyBinding INSTANCE = new KeyBinding(
            Lang.KEYBINDING_SHARE_NAME.getKey(),
            Keyboard.KEY_INSERT,
            Lang.KEYBINDING_SHARE_CATEGORY.getKey());
}
