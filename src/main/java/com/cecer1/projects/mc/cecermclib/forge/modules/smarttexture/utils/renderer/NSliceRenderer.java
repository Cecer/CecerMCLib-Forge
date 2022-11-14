package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.renderer;

import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.DrawMethods;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice.NSliceResourceMetadata;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice.NSliceSlice;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.rescaler.ScaledDivisionData;
import net.minecraft.client.renderer.texture.ITextureObject;

public class NSliceRenderer implements IScalableRenderer {
    private final NSliceResourceMetadata metadata;

    public NSliceRenderer(NSliceResourceMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void draw(ITextureObject texture, int targetWidth, int targetHeight) {
        this.draw(texture, targetWidth, targetHeight, 1);
    }
    
    @Override
    public void draw(ITextureObject texture, int targetWidth, int targetHeight, float alpha) {
        ScaledDivisionData columnWidths = ScaledDivisionData.calculate(targetWidth, this.metadata.getColumns());
        ScaledDivisionData rowHeights = ScaledDivisionData.calculate(targetHeight, this.metadata.getRows());

        float srcX = 0.0f;
        float srcY = 0.0f;
        int targetX = 0;
        int targetY = 0;


        for (int row = 0; row < rowHeights.targetSizesPixels.length; row++) {
            srcX = 0.0f;
            targetX = 0;

            for (int column = 0; column < columnWidths.targetSizesPixels.length; column++) {
                this.drawSlice(
                        // Source texture
                        texture,

                        // Source offsets
                        srcX, srcY,

                        // Target offsets
                        targetX, targetY,

                        // Segment indexes
                        column, row,

                        // Segment sizes
                        columnWidths, rowHeights,

                        // Raw metadata
                        metadata.getColumns()[column], metadata.getRows()[row],
                        
                        // Alpha value
                        alpha);

                srcX += columnWidths.sourceSizesNormalized[column];
                targetX += columnWidths.targetSizesPixels[column];
            }
            srcY += rowHeights.sourceSizesNormalized[row];
            targetY += rowHeights.targetSizesPixels[row];
        }
    }

    private void drawSlice(
            ITextureObject texture,
            float srcX, float srcY,
            int targetX, int targetY,
            int columnIndex, int rowIndex,
            ScaledDivisionData columnWidths, ScaledDivisionData rowHeights,
            NSliceSlice columnMetadata, NSliceSlice rowMetadata,
            float alpha) {


        int targetWidth = columnMetadata.size();
        int targetHeight = rowMetadata.size();
        
        float srcHeight = rowHeights.sourceSizesNormalized[rowIndex];
        float srcWidth = columnWidths.sourceSizesNormalized[columnIndex];
        
        switch (columnMetadata.growBehaviour()) {
            case NONE:
                if (columnWidths.targetSizesPixels[columnIndex] < columnMetadata.size()) {
                    // If the target size is smaller than the source size then we crop the source size so that it fits
                    srcWidth *= ((float) columnWidths.targetSizesPixels[columnIndex] / columnMetadata.size());
                }
                break;

            // TODO: Use GL_REPEAT which'll require splitting textures or simply draw multiple times.
            // GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            case REPEAT:
                throw new UnsupportedOperationException("Not implemented yet!");
            case STRETCH:
                targetWidth = columnWidths.targetSizesPixels[columnIndex];
                break;
            default:
                throw new IllegalArgumentException("Unknown column GrowBehaviour: " + columnMetadata.growBehaviour());
        }

        switch (rowMetadata.growBehaviour()) {
            case NONE:
                if (rowHeights.targetSizesPixels[rowIndex] < rowMetadata.size()) {
                    // If the target size is smaller than the source size then we crop the source size so that it fits
                    srcHeight *= ((float) rowHeights.targetSizesPixels[rowIndex] / rowMetadata.size());
                }
                break;
                
            // TODO: Use GL_REPEAT which may require splitting textures or simply draw multiple times.
            // GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            case REPEAT:
                throw new UnsupportedOperationException("Not implemented yet!");
            case STRETCH:
                targetHeight = rowHeights.targetSizesPixels[rowIndex];
                break;
            default:
                throw new IllegalArgumentException("Unknown row GrowBehaviour: " + rowMetadata.growBehaviour());
        }

        DrawMethods.drawColoredTexturedQuad(texture,
                targetX, targetY,
                targetWidth, targetHeight,
                srcX, srcY,
                srcWidth, srcHeight,
                ~(~((int)alpha * 255) << 24)); // To ARGB with RGB all being 1
        
    }
}
