package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.utils.rescaler;

public class NullCoordRescaler implements ICoordRescaler {
    
    public static NullCoordRescaler INSTANCE = new NullCoordRescaler();
    
    private NullCoordRescaler() {}

    @Override
    public int scaleX(int x, int fullWidth) {
        return x;
    }
    @Override
    public int scaleY(int y, int fullHeight) {
        return y;
    }

    @Override
    public int unscaleX(int x, int fullWidth) {
        return x;
    }
    @Override
    public int unscaleY(int y, int fullHeight) {
        return y;
    }
}
