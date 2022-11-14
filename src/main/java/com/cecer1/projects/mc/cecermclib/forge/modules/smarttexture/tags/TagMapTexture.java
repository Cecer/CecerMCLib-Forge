package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.tags;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TagMapTexture {
    private final BufferedImage bufferedImage;

    public TagMapTexture(ResourceLocation textureResourceLocation, IResourceManager resourceManager) {
        BufferedImage image;
        try (InputStream inputStream = resourceManager.getResource(textureResourceLocation).getInputStream()) {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            image = null;
        }
        this.bufferedImage = image;
    }

    public int getPixelColor(int x, int y) {
        if (this.bufferedImage == null) {
            return 0;
        }
        return this.bufferedImage.getRGB(x, y);
    }

    public void validate(ResourceLocation textureIdentifier, int width, int height) {
        if (this.bufferedImage == null) {
            return;
        }
        
        if (this.bufferedImage.getWidth() != width || this.bufferedImage.getHeight() != height) {
            throw new IllegalStateException(String.format("Size mismatch with tag map texture for texture %s! (%dx%d != %dx%d)",
                    textureIdentifier,
                    width, height,
                    this.bufferedImage.getWidth(), this.bufferedImage.getHeight()));
        }
    }
}
