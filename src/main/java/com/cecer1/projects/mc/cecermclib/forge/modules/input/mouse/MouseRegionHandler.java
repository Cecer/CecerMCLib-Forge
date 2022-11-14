package com.cecer1.projects.mc.cecermclib.forge.modules.input.mouse;

import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.AbstractCanvas;

import java.util.ArrayList;
import java.util.List;

public class MouseRegionHandler {

    private final List<ButtonHandler> onMouseUp;
    private final List<ButtonHandler> onMouseDown;
    private final List<MoveHandler> onMouseMove;
    private final List<ScrollHandler> onScroll;
    private final List<MousePositionEventHandler> onMouseEnter;
    private final List<MousePositionEventHandler> onMouseExit;
    private final List<ButtonHandler> onClick;
    private final List<ButtonHandler> onClickAbandoned;
    
    private int minX;
    private int maxX;

    private int minY;
    private int maxY;
    
    private float scale;
    
    public MouseRegionHandler() {
        this.onMouseUp = new ArrayList<>();
        this.onMouseDown = new ArrayList<>();
        this.onMouseMove = new ArrayList<>();
        this.onScroll = new ArrayList<>();
        this.onMouseEnter = new ArrayList<>();
        this.onMouseExit = new ArrayList<>();
        this.onClick = new ArrayList<>();
        this.onClickAbandoned = new ArrayList<>();
    }

    public MouseRegionHandler(int minX, int minY, int width, int height, float scale) {
        this();
        this.setTrueRegion(minX, minY, width, height, scale);
    }

    public int getMinX() {
        return this.minX;
    }
    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return this.maxX;
    }
    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return this.minY;
    }
    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return this.maxY;
    }
    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public float getScale() {
        return this.scale;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isMouseOver(int x, int y) {
        if (x < this.getMinX()) {
            return false;
        }
        if (x > this.getMaxX()) {
            return false;
        }
        if (y < this.getMinY()) {
            return false;
        }
        if (y > this.getMaxY()) {
            return false;
        }
        return true;
    }
    
    public void setRelativeRegion(AbstractCanvas canvas, int x, int y, int width, int height) {
        float trueScale = canvas.getTrueScale();
        int trueX = (int) (canvas.getTrueX() + (x * trueScale));
        int trueY = (int) (canvas.getTrueY() + (y * trueScale));
        int trueWidth = (int) (width * trueScale);
        int trueHeight = (int) (height * trueScale);
        this.setTrueRegion(trueX, trueY, trueWidth, trueHeight, trueScale);
    }
    public void setTrueRegion(int minX, int minY, int width, int height, float scale) {
        this.setMinX(minX);
        this.setMinY(minY);
        this.setMaxX(minX + width - 1);
        this.setMaxY(minY + height - 1);
        this.setScale(scale);
    }

    public void addOnMouseUpHandler(ButtonHandler handler) {
        this.onMouseUp.add(handler);
    }
    public void removeOnMouseUpHandler(ButtonHandler handler) {
        this.onMouseUp.remove(handler);
    }
    protected boolean onMouseUp(int button) {
        boolean result = true;
        for (ButtonHandler handler : this.onMouseUp) {
            if (!handler.handle(button)) {
                result = false;
            }
        }
        return result;
    }
    
    public boolean onMouseDown(int button) {
        boolean result = true;
        for (ButtonHandler handler : this.onMouseDown) {
            if (!handler.handle(button)) {
                result = false;
            }
        }
        return result;
    }
    public void addOnMouseDownHandler(ButtonHandler handler) {
        this.onMouseDown.add(handler);
    }
    public void removeOnMouseDownHandler(ButtonHandler handler) {
        this.onMouseDown.remove(handler);
    }

    public void onMouseMove(int lastMouseX, int lastMouseY, int trueMouseX, int trueMouseY) {
        for (MoveHandler handler : this.onMouseMove) {
            handler.handle(lastMouseX, lastMouseY, trueMouseX, trueMouseY);
        }
    }
    public void addOnMouseMoveHandler(MoveHandler handler) {
        this.onMouseMove.add(handler);
    }
    public void removeOnMouseMoveHandler(MoveHandler handler) {
        this.onMouseMove.remove(handler);
    }

    public boolean onScroll(double horizontalAmount, double verticalAmount) {
        boolean result = true;
        for (ScrollHandler handler : this.onScroll) {
            if (!handler.handle(horizontalAmount, verticalAmount)) {
                    result = false;
            }
        }
        return result;
    }
    public void addOnScrollHandler(ScrollHandler handler) {
        this.onScroll.add(handler);
    }
    public void removeOnScrollHandler(ScrollHandler handler) {
        this.onScroll.remove(handler);
    }

    public void onMouseEnter(int mouseTrueX, int mouseTrueY) {
        for (MousePositionEventHandler handler : this.onMouseEnter) {
            handler.handle(mouseTrueX, mouseTrueY);
        }
    }
    public void addOnMouseEnterHandler(MousePositionEventHandler handler) {
        this.onMouseEnter.add(handler);
    }
    public void removeOnMouseEnterHandler(MousePositionEventHandler handler) {
        this.onMouseEnter.remove(handler);
    }
    
    public void onMouseExit(int mouseTrueX, int mouseTrueY) {
        for (MousePositionEventHandler handler : this.onMouseExit) {
            handler.handle(mouseTrueX, mouseTrueY);
        }
    }
    public void addOnMouseExitHandler(MousePositionEventHandler handler) {
        this.onMouseExit.add(handler);
    }
    public void removeOnMouseExitHandler(MousePositionEventHandler handler) {
        this.onMouseExit.remove(handler);
    }

    public boolean onClick(int button) {
        boolean result = true;
        for (ButtonHandler handler : this.onClick) {
            if (!handler.handle(button)) {
                result = false;
            }
        }
        return result;
    }
    public void addOnClickHandler(ButtonHandler handler) {
        this.onClick.add(handler);
    }
    public void removeOnClickHandler(ButtonHandler handler) {
        this.onClick.remove(handler);
    }
    
    public boolean onClickAbandoned(int button) {
        boolean result = true;
        for (ButtonHandler handler : this.onClickAbandoned) {
            if (!handler.handle(button)) {
                result = false;
            }
        }
        return result;
    }
    public void addOnClickAbandonedHandler(ButtonHandler handler) {
        this.onClickAbandoned.add(handler);
    }
    public void removeOnClickAbandonedHandler(ButtonHandler handler) {
        this.onClickAbandoned.remove(handler);
    }


    @FunctionalInterface
    public interface MousePositionEventHandler {
        boolean handle(int x, int y);
    }
    @FunctionalInterface
    public interface MoveHandler {
        boolean handle(int fromX, int fromY, int toX, int toY);
    }
    @FunctionalInterface
    public interface ButtonHandler {
        boolean handle(int button);
    }
    @FunctionalInterface
    public interface ScrollHandler {
        boolean handle(double vertical, double horizontal);
    }
}
