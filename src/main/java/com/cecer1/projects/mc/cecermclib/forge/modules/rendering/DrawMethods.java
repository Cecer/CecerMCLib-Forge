package com.cecer1.projects.mc.cecermclib.forge.modules.rendering;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class DrawMethods {

    /**
     * Draws a solid color rectangle with the specified coordinates and color (ARGB format).
     */
    public static void drawSolidRect(int x, int y, int width, int height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);;
    }

    /**
     * Draws a hollow color rectangle with the specified coordinates and color (ARGB format).
     */
    public static void drawHollowRect(int x, int y, int width, int height, int thickness, int color) {
        drawSolidRect(x, y, width, thickness, color); // Top line
        drawSolidRect(x, y+thickness, thickness, height - (thickness * 2), color); // Left line
        drawSolidRect(x, y+height-thickness, width, thickness, color); // Bottom line
        drawSolidRect(x+width-thickness, y+thickness, thickness, height - (thickness * 2), color); // Right line
    }

    public static void drawAlphaTexturedQuad(ITextureObject texture, int x, int y, int width, int height, float srcX, float srcY, float srcWidth, float srcHeight, float alpha) {
        drawAlphaTexturedQuad(texture.getGlTextureId(), x, y, width, height, srcX, srcY, srcWidth, srcHeight, alpha);
    }
    public static void drawAlphaTexturedQuad(int textureId, int x, int y, int width, int height, float srcX, float srcY, float srcWidth, float srcHeight, float alpha) {
        int color = 0xffffff | (((int)alpha * 255) << 24);
        drawColoredTexturedQuad(textureId, x, y, width, height, srcX, srcY, srcWidth, srcHeight, color);
    }
    public static void drawColoredTexturedQuad(ITextureObject texture, int x, int y, int width, int height, float srcX, float srcY, float srcWidth, float srcHeight, int color) {
        drawColoredTexturedQuad(texture.getGlTextureId(), x, y, width, height, srcX, srcY, srcWidth, srcHeight, color);
    }
    public static void drawColoredTexturedQuad(int textureId, int x, int y, int width, int height, float srxX, float srcY, float srcWidth, float srcHeight, int color) {
        int xEnd = x + width;
        int yEnd = y + height;
        float srcXEnd = srxX + srcWidth;
        float srcYEnd = srcY + srcHeight;

        int alpha = (color >> 24) & 0xff;
        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = color & 0xff;
        
        float f = 1.0F / srcWidth;
        float f1 = 1.0F / srcHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x, y + height, 0.0D)          .tex((srxX * f), ((srcY + (float)height) * f1))                  .color(red, green, blue, alpha) .endVertex();
        worldrenderer.pos((x + width), (y + height), 0.0D) .tex(((srxX + (float)width) * f), ((srcY + (float)height) * f1)) .color(red, green, blue, alpha) .endVertex();
        worldrenderer.pos((x + width), y, 0.0D)            .tex(((srxX + (float)width) * f), (srcY * f1))                   .color(red, green, blue, alpha) .endVertex();
        worldrenderer.pos(x, y, 0.0D)                      .tex((srxX * f), (srcY * f1))                                    .color(red, green, blue, alpha) .endVertex();
        tessellator.draw();
    }
}
