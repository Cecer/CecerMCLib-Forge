package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.renderer;

import net.minecraft.client.renderer.texture.ITextureObject;

public interface IScalableRenderer {
    void draw(ITextureObject texture, int targetWidth, int targetHeight);
    void draw(ITextureObject texture, int targetWidth, int targetHeight, float alpha);
}
