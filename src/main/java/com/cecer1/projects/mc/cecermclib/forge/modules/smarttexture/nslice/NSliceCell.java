package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice;

import java.util.Objects;

public final class NSliceCell {
    private final int row;
    private final int column;

    NSliceCell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int row() {
        return row;
    }

    public int column() {
        return column;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        NSliceCell that = (NSliceCell) obj;
        return this.row == that.row &&
                this.column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "NSliceCell[" +
                "row=" + row + ", " +
                "column=" + column + ']';
    }

}
