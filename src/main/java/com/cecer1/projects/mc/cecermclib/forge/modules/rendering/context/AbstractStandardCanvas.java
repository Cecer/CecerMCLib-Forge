package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context;

import net.minecraft.client.renderer.GlStateManager;

public abstract class AbstractStandardCanvas extends AbstractCanvas implements AutoCloseable {

    protected AbstractStandardCanvas(AbstractCanvas parentCanvas, RenderContext ctx) {
        super(parentCanvas, ctx);
    }

    /**
     * Applies this canvas to the render context.
     * @return This canvas
     */
    public AbstractStandardCanvas open() {
        this.setOpenTrace();
        GlStateManager.pushMatrix();
        this.ctx.pushCanvas(this);
        return this;
    }

    /**
     * Unappplies this canvas from the render context.
     */
    @Override
    public void close() {
        this.ctx.popCanvas();
        GlStateManager.popMatrix();
    }
}
