package com.muxiu1997.sharewhereiam.mixinplugin;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

@LateMixin
public class ShareWhereIAmLateMixin implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.sharewhereiam.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Mixins.getLateMixins(loadedMods);
    }
}
