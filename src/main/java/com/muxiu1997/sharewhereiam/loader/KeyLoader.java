package com.muxiu1997.sharewhereiam.loader;

import com.muxiu1997.sharewhereiam.client.key.KeyShare;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyLoader {

    public static final KeyLoader INSTANCE = new KeyLoader();

    public void clientLoad() {
        ClientRegistry.registerKeyBinding(KeyShare.INSTANCE);
    }
}
