package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class RootCanvas extends AbstractStandardCanvas {

    private float correctiveScaleFactor;

    public RootCanvas(RenderContext ctx) {
        super(null, ctx);
    }

    @Override
    public TransformCanvasBuilder transform() {
        return super.transform();
    }

    @Override
    public float getTrueScale() {
        return 1.0f;
    }

    @Override
    public int getTrueX() {
        return 0;
    }

    @Override
    public int getTrueY() {
        return 0;
    }

    @Override
    public int getTrueWidth() {
        return Minecraft.getMinecraft().displayWidth;
    }

    @Override
    public int getTrueHeight() {
        return Minecraft.getMinecraft().displayHeight;
    }

    @Override
    public int getRelativeX() {
        return 0;
    }

    @Override
    public int getRelativeY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return Minecraft.getMinecraft().displayWidth;
    }

    @Override
    public int getHeight() {
        return Minecraft.getMinecraft().displayHeight;
    }

    @Override
    public AbstractStandardCanvas open() {
        super.open();
        this.correctiveScaleFactor = 1.0f / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GlStateManager.scale(this.correctiveScaleFactor, this.correctiveScaleFactor, 1);
        return this;
    }
}
