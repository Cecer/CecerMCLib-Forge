package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.transformations;

import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.AbstractCanvas;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.RenderContext;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class CropTransformationCanvas extends AbstractTransformationCanvas {

    public CropTransformationCanvas(AbstractCanvas parentCanvas, RenderContext ctx) {
        super(parentCanvas, ctx);
    }

    @Override
    public void apply() {
        int y = Minecraft.getMinecraft().displayHeight - this.getTrueY() - this.getTrueHeight();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(this.getTrueX(), y, this.getTrueWidth(), this.getTrueHeight());
    }

    @Override
    public void unapply() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
