package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.fbo;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class FBOTexture extends AbstractTexture {

    private final ResourceLocation identifier;
    private final int width;
    private final int height;

    public FBOTexture(int framebufferId, int width, int height) {
        this.identifier = new ResourceLocation("cecermclib", "fbotexture/" + framebufferId);
        this.width = width;
        this.height = height;
    }

    public ResourceLocation getIdentifier() {
        return this.identifier;
    }

    @Override
    public void loadTexture(IResourceManager manager) {
        this.prepare();
    }

    private void prepare() {
    }
}
