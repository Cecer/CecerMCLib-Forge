package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.transformations;

import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.AbstractCanvas;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.RenderContext;
import net.minecraft.client.renderer.GlStateManager;

public class TranslateTransformationCanvas extends AbstractTransformationCanvas {

    private int x;
    private int y;

    public TranslateTransformationCanvas(AbstractCanvas parentCanvas, RenderContext ctx, int x, int y) {
        super(parentCanvas, ctx);
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getRelativeX() {
        return this.x;
    }

    @Override
    public int getRelativeY() {
        return this.y;
    }

    @Override
    public void apply() {
        GlStateManager.translate(this.x, this.y, 0);
    }
}
