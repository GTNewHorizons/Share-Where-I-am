package com.muxiu1997.sharewhereiam.mixins.late.journeymap;

import static com.muxiu1997.sharewhereiam.network.NetworkHandler.network;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.muxiu1997.sharewhereiam.client.key.KeyBindings;
import com.muxiu1997.sharewhereiam.integration.Mods;
import com.muxiu1997.sharewhereiam.localization.Lang;
import com.muxiu1997.sharewhereiam.network.MessageShareWaypoint;
import com.muxiu1997.sharewhereiam.util.VPWaypointUtil;
import com.muxiu1997.sharewhereiam.util.WaypointUtil;

import journeymap.client.Constants;
import journeymap.client.model.Waypoint;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.fullscreen.MapChat;

@SuppressWarnings("UnusedMixin")
@Mixin(Fullscreen.class)
public abstract class MixinFullscreen extends JmUI {

    @SuppressWarnings("ShadowModifiers")
    @Shadow(remap = false)
    private MapChat chat;

    public MixinFullscreen() {
        super("");
    }

    @Inject(method = "func_73869_a", at = @At(value = "HEAD"), remap = false, require = 1, cancellable = true)
    private void inject_func_73869_a(CallbackInfo callbackInfo) {
        if (!Mods.VisualProspecting.isLoaded()) return;
        if ((chat == null || chat.isHidden()) && Constants.isPressed(KeyBindings.WaypointShare)) {
            @Nullable
            final Waypoint waypoint = VPWaypointUtil.getHoveredWaypoint();
            if (waypoint == null) return;
            final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            network.sendToServer(
                    new MessageShareWaypoint(
                            new WaypointUtil.PlayerWaypoint(player, waypoint),
                            Lang.SHARE_WAYPOINT_VP.translate()));
            callbackInfo.cancel();
        }
    }
}
