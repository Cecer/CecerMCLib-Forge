package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.slots;

import java.util.Objects;

public final class Slot {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    Slot(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Slot that = (Slot) obj;
        return this.x == that.x &&
                this.y == that.y &&
                this.width == that.width &&
                this.height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Slot[" +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "width=" + width + ", " +
                "height=" + height + ']';
    }


}
