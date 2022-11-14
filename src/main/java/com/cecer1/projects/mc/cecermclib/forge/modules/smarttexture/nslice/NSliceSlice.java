package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice;

import java.util.Objects;

public final class NSliceSlice {
    private final int size;
    private final NSliceGrowBehaviour growBehaviour;
    private final int growWeight;

    NSliceSlice(int size, NSliceGrowBehaviour growBehaviour, int growWeight) {
        this.size = size;
        this.growBehaviour = growBehaviour;
        this.growWeight = growWeight;
    }

    public int size() {
        return size;
    }

    public NSliceGrowBehaviour growBehaviour() {
        return growBehaviour;
    }

    public int growWeight() {
        return growWeight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        NSliceSlice that = (NSliceSlice) obj;
        return this.size == that.size &&
                Objects.equals(this.growBehaviour, that.growBehaviour) &&
                this.growWeight == that.growWeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, growBehaviour, growWeight);
    }

    @Override
    public String toString() {
        return "NSliceSlice[" +
                "size=" + size + ", " +
                "growBehaviour=" + growBehaviour + ", " +
                "growWeight=" + growWeight + ']';
    }


}
