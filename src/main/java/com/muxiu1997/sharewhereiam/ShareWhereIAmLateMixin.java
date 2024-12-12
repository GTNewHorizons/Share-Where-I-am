package com.muxiu1997.sharewhereiam;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import com.muxiu1997.sharewhereiam.mixins.Mixins;

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
