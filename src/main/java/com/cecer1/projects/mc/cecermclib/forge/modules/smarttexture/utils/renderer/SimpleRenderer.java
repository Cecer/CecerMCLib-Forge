package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.renderer;

import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.DrawMethods;
import net.minecraft.client.renderer.texture.ITextureObject;

public class SimpleRenderer implements IScalableRenderer {
    
    public static SimpleRenderer INSTANCE = new SimpleRenderer();
    
    private SimpleRenderer() {}

    @Override
    public void draw(ITextureObject texture, int targetWidth, int targetHeight) {
        this.draw(texture, targetWidth, targetHeight, 1);;
    }

    @Override
    public void draw(ITextureObject texture, int targetWidth, int targetHeight, float alpha) {
        int targetX = 0;
        int targetY = 0;;

        DrawMethods.drawColoredTexturedQuad(texture,
                targetX, targetY,
                targetWidth, targetHeight,
                0, 0, 1, 1,
                ~(~((int)alpha * 255) << 24)); // To ARGB with RGB all being 1
    }
}
