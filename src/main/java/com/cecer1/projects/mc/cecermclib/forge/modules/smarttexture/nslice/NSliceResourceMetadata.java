package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class NSliceResourceMetadata implements IMetadataSection {

    private final NSliceSlice[] rows;
    private final NSliceSlice[] columns;

    public NSliceSlice[] getRows() {
        return this.rows;
    }
    public NSliceSlice[] getColumns() {
        return this.columns;
    }

    public NSliceResourceMetadata(NSliceSlice[] rows, NSliceSlice[] columns) {
        this.rows = rows;
        this.columns = columns;
    }
    
    // <editor-fold desc="[Equality]">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NSliceResourceMetadata that = (NSliceResourceMetadata) o;
        return Arrays.equals(rows, that.rows) &&
                Arrays.equals(columns, that.columns);
    }
    @Override
    public int hashCode() {
        int result = Arrays.hashCode(rows);
        result = 31 * result + Arrays.hashCode(columns);
        return result;
    }

    public void validate(ResourceLocation textureIdentifier, int width, int height) {
        int widthSum = 0;
        for (NSliceSlice column : this.getColumns()) {
            widthSum += column.size();
        }
        int heightSum = 0;
        for (NSliceSlice row : this.getRows()) {
            heightSum += row.size();
        }

        if (widthSum != width || heightSum != height) {
            throw new IllegalStateException(String.format("Size mismatch with nslice splits for texture %s! Check your rows/columns! (%dx%d != %dx%d)",
                    textureIdentifier,
                    width, height,
                    widthSum, heightSum));
        }
    }
    // </editor-fold>

}
