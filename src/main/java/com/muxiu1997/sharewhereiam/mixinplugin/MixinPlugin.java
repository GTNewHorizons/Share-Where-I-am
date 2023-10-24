package com.muxiu1997.sharewhereiam.mixinplugin;

import static java.nio.file.Files.walk;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.launchwrapper.Launch;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import ru.timeconqueror.spongemixins.MinecraftURLClassPath;

import com.google.common.io.Files;
import cpw.mods.fml.relauncher.FMLLaunchHandler;

public class MixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        final File journeyMapModJar = findJourneyMapModJar();
        if (journeyMapModJar == null) return null;
        if (!journeyMapModJar.exists()) return null;
        try {
            MinecraftURLClassPath.addJar(journeyMapModJar);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        final List<String> mixins = new ArrayList<>(4);
        if (FMLLaunchHandler.side().isClient()) {
            mixins.add("journeymap.MixinWaypointManagerItem");
            mixins.add("journeymap.MixinWaypointStore");
            mixins.add("journeymap.MixinRenderWaypointBeacon");
            mixins.add("journeymap.MixinFullscreen");
        }
        return mixins;
    }

    @SuppressWarnings({ "UnstableApiUsage", "resource" })
    private static File findJourneyMapModJar() {
        try {
            return walk(new File(Launch.minecraftHome, "mods/").toPath()).filter(path -> {
                final String pathString = path.toString();
                final String nameLowerCase = Files.getNameWithoutExtension(pathString).toLowerCase();
                final String fileExtension = Files.getFileExtension(pathString);
                return nameLowerCase.startsWith("journeymap-1.7.10") && "jar".equals(fileExtension);
            }).map(Path::toFile).findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
