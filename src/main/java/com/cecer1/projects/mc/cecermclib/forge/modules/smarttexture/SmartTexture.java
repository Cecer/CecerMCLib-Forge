package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture;

import com.cecer1.projects.mc.cecermclib.common.CecerMCLib;
import com.cecer1.projects.mc.cecermclib.common.modules.logger.LoggerModule;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.RenderContext;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.TransformCanvas;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.TransformCanvasBuilder;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice.NSliceResourceMetadata;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice.NSliceResourceMetadataReader;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.slots.Slot;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.slots.SlotsResourceMetadata;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.slots.SlotsResourceMetadataReader;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.tags.TagMapTexture;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.tags.TagResourceMetadata;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.tags.TagResourceMetadataReader;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.renderer.IScalableRenderer;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.renderer.NSliceRenderer;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.renderer.SimpleRenderer;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.rescaler.ICoordRescaler;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.rescaler.NSliceCoordRescaler;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.rescaler.NullCoordRescaler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

public class SmartTexture {
    
    // <editor-fold desc="[Resource and Texture management]">
    private static final IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
    private static final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    // </editor-fold>

    private final ResourceLocation textureIdentifier;
    private final NSliceResourceMetadata nsliceMetadata;
    private final SlotsResourceMetadata slotsMetadata;
    private final TagResourceMetadata tagsMetadata;
    
    private final TagMapTexture tagMapTexture;
    
    private final IScalableRenderer renderer;
    private final ICoordRescaler coordRescaler;
    
    private final int textureWidth;
    private final int textureHeight;

    public static SmartTexture fromIdentifier(ResourceLocation identifier) {
        try {
            return new SmartTexture(identifier);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static SmartTexture recreateSmartTexture(SmartTexture texture) {
        return SmartTexture.fromIdentifier(texture.textureIdentifier);
    }
    
    private SmartTexture(ResourceLocation textureIdentifier) throws IOException {
        this.textureIdentifier = textureIdentifier;

        IResource resource = SmartTexture.resourceManager.getResource(textureIdentifier);
        try {
            int textureWidth, textureHeight;
            try(InputStream inputStream = resourceManager.getResource(textureIdentifier).getInputStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                textureWidth = image.getWidth();
                textureHeight = image.getHeight();
            } catch (IOException e) {
                CecerMCLib.get(LoggerModule.class).getChannel(SmartTexture.class).log("Failed to read texture dimensions for %s. This may cause graphic errors or even crashes.", textureIdentifier);
                e.printStackTrace();
                textureWidth = 0;
                textureHeight = 0;
            }
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            
            this.nsliceMetadata = resource.getMetadata(NSliceResourceMetadataReader.getInstance().getSectionName());
            this.slotsMetadata = resource.getMetadata(SlotsResourceMetadataReader.getInstance().getSectionName());
            this.tagsMetadata = resource.getMetadata(TagResourceMetadataReader.getInstance().getSectionName());
        } catch (Exception e) {
            CecerMCLib.get(LoggerModule.class).getChannel(this.getClass()).log("Failed to load SmartTexture at %s!", textureIdentifier);
            throw e;
        }
        
        String path = textureIdentifier.getResourcePath();
        if (path.endsWith(".png")) {
            path = path.substring(0, path.length() - 4) + ".tagmap.png";
            this.tagMapTexture = new TagMapTexture(new ResourceLocation(textureIdentifier.getResourceDomain(), path), resourceManager);
            this.tagMapTexture.validate(this.textureIdentifier, this.textureWidth, this.textureHeight);
        } else {
            this.tagMapTexture = null;
        }
        
        if (this.nsliceMetadata != null) {
            this.nsliceMetadata.validate(this.textureIdentifier, this.textureWidth, this.textureHeight);
            
            this.renderer = new NSliceRenderer(this.nsliceMetadata); 
            this.coordRescaler = new NSliceCoordRescaler(this.nsliceMetadata);
        } else {
            this.renderer = SimpleRenderer.INSTANCE;
            this.coordRescaler = NullCoordRescaler.INSTANCE;
        }
    }
    
    private ITextureObject getTexture() {
        return SmartTexture.textureManager.getTexture(this.textureIdentifier);
    }

    public int getTextureWidth() {
        return this.textureWidth;
    }
    public int getTextureHeight() {
        return this.textureHeight;
    }

    public void draw(int targetWidth, int targetHeight) {
        this.renderer.draw(this.getTexture(), targetWidth, targetHeight);
    }


    private TransformCanvas selectSlot(String name, RenderContext ctx) {
        return this.selectSlot(name, false, ctx);
    }
    private TransformCanvas selectSlot(String name, boolean crop, RenderContext ctx) {
        if (this.slotsMetadata != null) {
            Slot slot = this.getSlot(name);
            if (slot != null) {
                return this.selectSlot(slot, crop, ctx);
            }
        }

        // No slot metadata or no slot, do nothing.
        return ctx.getCanvas().transform().openTransformation();
    }
    
    public TransformCanvas selectSlot(Slot slot, RenderContext ctx) {
        return this.selectSlot(slot, false, ctx);
    }
    
    public TransformCanvas selectSlot(Slot slot, boolean crop, RenderContext ctx) {
        int width = ctx.getCanvas().getWidth();
        int height = ctx.getCanvas().getHeight();

        int xStart = this.coordRescaler.scaleX(slot.x(), width);
        int xEnd = this.coordRescaler.scaleX(slot.x() + slot.width(), width);
        
        int yStart = this.coordRescaler.scaleY(slot.y(), height);
        int yEnd = this.coordRescaler.scaleY(slot.y() + slot.height(), height);
        TransformCanvasBuilder canvasBuilder = ctx.getCanvas().transform()
                .margin(yStart, 
                        width - xEnd, 
                        height - yEnd, 
                        xStart);
        if (crop) {
            canvasBuilder = canvasBuilder.crop();
        }
        return canvasBuilder.openTransformation();
    }

    public Set<String> getTagsAt(int x, int y, int targetWidth, int targetHeight) {
        if (this.tagsMetadata == null) {
            // No tag metadata, do nothing.
            return Collections.emptySet();
        }
        
        int color = this.tagMapTexture.getPixelColor(
                this.coordRescaler.unscaleX(x, targetWidth),
                this.coordRescaler.unscaleY(y, targetHeight));
        
        return this.tagsMetadata.getTags(color);
    }
    
    public Slot getSlot(String slotName) {
        if (this.slotsMetadata == null) {
            return null;
        }
        return this.slotsMetadata.getSlot(slotName);
    }

//    @Deprecated
//    public int getSlotMarginX(String name) {
//        return this.getSlotMarginX(name, this.minWidth);
//    }
//    @Deprecated
//    public int getSlotMarginY(String name) {
//        return this.getSlotMarginY(name, this.minHeight);
//    }
//
//    @Deprecated
//    public int getSlotMarginX(String name, int totalWidth) {
//        SlotsResourceMetadata.Slot slot = this.slotsMetadata.getSlot(name);
//        return totalWidth - (this.coordRescaler.scaleX(slot.x + slot.width, totalWidth, 1) - this.coordRescaler.scaleX(slot.x, totalWidth, 1));
//    }
//    @Deprecated
//    public int getSlotMarginY(String name, int totalHeight) {
//        SlotsResourceMetadata.Slot slot = this.slotsMetadata.getSlot(name);
//        return totalHeight - (this.coordRescaler.scaleY(slot.y + slot.height, 1, totalHeight) - this.coordRescaler.scaleY(slot.y, 1, totalHeight));
//    }
//
//
//    @Deprecated
//    public int getSlotWidth(String name, int totalWidth) {
//        SlotsResourceMetadata.Slot slot = this.slotsMetadata.getSlot(name);
//        return this.coordRescaler.scaleX(slot.x + slot.width, totalWidth, 1) - this.coordRescaler.scaleX(slot.x, totalWidth, 1);
//    }
//    @Deprecated
//    public int getSlotHeight(String name, int totalHeight) {
//        SlotsResourceMetadata.Slot slot = this.slotsMetadata.getSlot(name);
//        return this.coordRescaler.scaleY(slot.y + slot.height, 1, totalHeight) - this.coordRescaler.scaleY(slot.y, 1, totalHeight);
//    }
}
