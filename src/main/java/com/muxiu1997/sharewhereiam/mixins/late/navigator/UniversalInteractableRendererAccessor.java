package com.muxiu1997.sharewhereiam.mixins.late.navigator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.gtnewhorizons.navigator.api.model.layers.UniversalInteractableRenderer;
import com.gtnewhorizons.navigator.api.model.steps.UniversalInteractableStep;

@Mixin(UniversalInteractableRenderer.class)
public interface UniversalInteractableRendererAccessor {

    @Accessor(value = "hoveredRenderStep", remap = false)
    UniversalInteractableStep<?> getHoveredRenderStep();

}
