package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.fbo;

import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class FBOSession implements AutoCloseable {

    private final int restoreFrameBufferId;
    private final int restoreDepthBufferId;

    private final FBO fbo;
    private final FBOResources resources;
    private final float correctiveScaleFactorX;
    private final float correctiveScaleFactorY;

    protected FBOSession(FBO fbo, FBOResources resources, RenderContext ctx) {
        this(fbo, resources, ctx.getCanvas().getTrueScale());
    }
    protected FBOSession(FBO fbo, FBOResources resources) {
        this(fbo, resources, new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor());
    }
    private FBOSession(FBO fbo, FBOResources resources, float currentScale) {
        this.fbo = fbo;
        this.resources = resources;

        this.restoreFrameBufferId = FBO.getCurrentFrameBufferId();
        this.restoreDepthBufferId = FBO.getCurrentDepthBufferId();

        System.out.printf("Frame: %d => %d%n", this.restoreFrameBufferId, this.resources.getFrameBufferId());
        System.out.printf("Depth: %d => %d%n", this.restoreDepthBufferId, this.resources.getDepthBufferId());
        System.out.println();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.resources.getFrameBufferId());
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.resources.getDepthBufferId());

        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            this.close();
            throw new RuntimeException("Framebuffer creation incomplete! Unsupported graphics card or drivers? (Status code: " + status + ")");
        }

        this.correctiveScaleFactorX = 1.0f / currentScale * Minecraft.getMinecraft().displayWidth / this.resources.getBufferWidth();
        this.correctiveScaleFactorY = 1.0f / currentScale * Minecraft.getMinecraft().displayHeight / this.resources.getBufferHeight();
        GL11.glViewport(0, 0, this.resources.getBufferWidth(), this.resources.getBufferHeight());
        GlStateManager.scale(this.correctiveScaleFactorX, this.correctiveScaleFactorY, 1.0f);
    }

    public FBOSession clear() {
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        return this;
    }

    @Override
    public void close() {
        GlStateManager.scale(1f/this.correctiveScaleFactorX, 1f/this.correctiveScaleFactorY, 1f);
        GL11.glViewport(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.restoreFrameBufferId);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.restoreDepthBufferId);
    }
}