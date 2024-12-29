package com.muxiu1997.sharewhereiam.loader;

import java.util.Arrays;

import com.muxiu1997.sharewhereiam.integration.Mods;

public class IntegrationLoader {

    public static void load() {
        Arrays.stream(Mods.values()).forEach(Mods::isLoaded);
    }
}
