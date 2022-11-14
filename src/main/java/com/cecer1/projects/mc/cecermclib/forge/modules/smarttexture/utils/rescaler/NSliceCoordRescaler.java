package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.rescaler;

import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice.NSliceResourceMetadata;
import com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice.NSliceSlice;

public class NSliceCoordRescaler implements ICoordRescaler {
    private final NSliceResourceMetadata metadata;

    public NSliceCoordRescaler(NSliceResourceMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public int scaleX(int x, int fullWidth) {
        return this.scalePosition(x, fullWidth, this.metadata.getColumns());
    }

    @Override
    public int scaleY(int y, int fullHeight) {
        return this.scalePosition(y, fullHeight, this.metadata.getRows());
    }

    @Override
    public int unscaleX(int x, int fullWidth) {
        return this.unscalePosition(x, fullWidth, this.metadata.getColumns());
    }

    @Override
    public int unscaleY(int y, int fullHeight) {
        return this.unscalePosition(y, fullHeight, this.metadata.getRows());
    }
    

    // This method is verbose for clarity.
    private int scalePosition(int unscaledPosition, int scaledSize, NSliceSlice[] slices) {
        ScaledDivisionData divisionData = ScaledDivisionData.calculate(scaledSize, slices);

        // This WILL hold the unscaled number of pixels that are before the slice containing our position.
        int unscaledPixelsBeforeSlice = 0;
        // This WILL hold the scaled number of pixels that are before the slice containing our position.
        int scaledPixelsBeforeSlice = 0;

        // This WILL hold the slice index containing our position.
        int sliceIndex = 0;
        for (; sliceIndex < slices.length; sliceIndex++) {
            if (unscaledPixelsBeforeSlice + slices[sliceIndex].size() > unscaledPosition) {
                // We have reached the slice containing the position. Break out of the loop!
                break;
            }
            unscaledPixelsBeforeSlice += slices[sliceIndex].size();
            scaledPixelsBeforeSlice += divisionData.targetSizesPixels[sliceIndex];
        }
        
        /*
        
        3
        1-1
        ----
        
        |0|1|2|
        
        |0|1|1|1|1|2|
        |0|1|2|3|4|5
        
        ---
        
        unscaledPosition = 1        
        unscaledPixelsBeforeSlice = 1
        scaledPixelsBeforeSlice = 1
        sliceIndex = 1
        
        ---
        
        unscaledPositionRelativeToSliceStart = 1 - 1 = 0
        positionRelativeToSliceNormalised = 0 / 1 = 0
        scaledPositionRelativeToSliceStart = 4 * 0 = 0
        
        return 1 + 0
        
         */
        
        
        // If we're past the last slice, just use the last slice.
        if (sliceIndex == slices.length) {
            sliceIndex--;
        }
        
        // unscaledPixelsBeforeSlice, scaledPixelsBeforeSlice and sliceIndex now contain their final values.
        
        // How many unscaled pixels the unscaled position is beyond the start of the unscaled containing slice. 
        int unscaledPositionRelativeToSliceStart = unscaledPosition - unscaledPixelsBeforeSlice;
        
        // The percentage (0-1) of the containing slice that is before the position.
        double positionRelativeToSliceNormalised = (double) unscaledPositionRelativeToSliceStart / slices[sliceIndex].size();
        
        // How many scaled pixels the scaled position is beyond the start of the scaled containing slice.
        int scaledPositionRelativeToSliceStart = (int) (divisionData.targetSizesPixels[sliceIndex] * positionRelativeToSliceNormalised);

        // Add the scaled slice pixels before the position to the scaled pixels before the slice entirely... and we're done!
        return scaledPixelsBeforeSlice + scaledPositionRelativeToSliceStart;
    }
    
    // This method is verbose for clarity.
    private int unscalePosition(int scaledPosition, int scaledSize, NSliceSlice[] slices) {
        ScaledDivisionData divisionData = ScaledDivisionData.calculate(scaledSize, slices);

        // This WILL hold the unscaled number of pixels that are before the slice containing our position.
        int unscaledPixelsBeforeSlice = 0;
        // This WILL hold the scaled number of pixels that are before the slice containing our position.
        int scaledPixelsBeforeSlice = 0;

        // This WILL hold the slice index containing our position.
        int sliceIndex = 0;
        for (; sliceIndex < slices.length; sliceIndex++) {
            if (scaledPixelsBeforeSlice + divisionData.targetSizesPixels[sliceIndex] > scaledPosition) {
                // We have reached the slice containing the position. Break out of the loop!
                break;
            }
            unscaledPixelsBeforeSlice += slices[sliceIndex].size();
            scaledPixelsBeforeSlice += divisionData.targetSizesPixels[sliceIndex];
        }
        if (sliceIndex == slices.length) {
            sliceIndex--;
        }
        // unscaledPixelsBeforeSlice, scaledPixelsBeforeSlice and sliceIndex now contain their final values.

        // How many scaled pixels the scaled position is beyond the start of the scaled containing slice. 
        int scaledPositionRelativeToSliceStart = scaledPosition - scaledPixelsBeforeSlice;

        // The percentage (0-1) of the containing slice that is before the position.
        double positionRelativeToSliceNormalised = (double) scaledPositionRelativeToSliceStart / divisionData.targetSizesPixels[sliceIndex];

        // How many unscaled pixels the unscaled position is beyond the start of the unscaled containing slice.
        int unscaledPositionRelativeToSliceStart = (int) (slices[sliceIndex].size() * positionRelativeToSliceNormalised);

        // Add the scaled slice pixels before the position to the scaled pixels before the slice entirely... and we're done!
        return unscaledPixelsBeforeSlice + unscaledPositionRelativeToSliceStart;
    }
}
