package com.cecer1.projects.mc.cecermclib.forge.modules.input.mouse;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MouseInputManager {
    // TODO: Implement this class
    private int lastMouseX = -1;
    private int lastMouseY = -1;

    private final List<MouseRegionHandler> mouseRegionHandlers;
//    private final Int2ObjectMap<MouseRegionHandler> clickInProgressHandlers;
    private MouseRegionHandler handlerUnderCursor;

    public MouseInputManager() {
        this.mouseRegionHandlers = new ArrayList<>();
//        this.clickInProgressHandlers = new Int2ObjectArrayMap<>();
        this.handlerUnderCursor = null;
    }

    public void registerEvents() {
//        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
//            this.onCursorActivated();
//            this.registerEvents(screen);
//        });
//        
//        // Track mouse movement every frame
//        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
//            if (this.lastMouseX != -1) {
//                MinecraftClient mc = Minecraft.getMinecraft();
//                int trueMouseX = (int) mc.mouse.getX();
//                int trueMouseY = (int) mc.mouse.getY();
//                
//                if (this.lastMouseX != trueMouseX || this.lastMouseY != trueMouseY) {
//                    this.onMouseMove(trueMouseX, trueMouseY);
//                }
//            }
//        });
    }
    
//    private void registerEvents(Screen screen) {
//        ScreenEvents.remove(screen).register(ignore -> this.onCursorDeactivated());
//        ScreenMouseEvents.allowMouseClick(screen).register((ignore, ignore2, ignore3, button) -> this.onMouseDown(button));
//        ScreenMouseEvents.allowMouseRelease(screen).register((ignore, ignore2, ignore3, button) -> this.onMouseUp(button));
//        ScreenMouseEvents.allowMouseScroll(screen).register((ignore, ignore2, ignore3, horizontalAmount, verticalAmount) -> this.onScroll(horizontalAmount, verticalAmount));
//    }

    public void registerHandler(MouseRegionHandler handler) {
        this.mouseRegionHandlers.add(handler);
    }

    public void clearHandlers() {
        this.mouseRegionHandlers.clear();
    }


    public void onCursorActivated() {
        Minecraft mc = Minecraft.getMinecraft();
        int trueMouseX = Mouse.getX();
        int trueMouseY = mc.displayHeight - Mouse.getY();

        MouseRegionHandler handler = this.getTopHandler(trueMouseX, trueMouseY);
        if (handler != null) {
            this.handleCursorEnter(handler, trueMouseX, trueMouseY);
        }

        this.lastMouseX = trueMouseX;
        this.lastMouseY = trueMouseY;
    }
    public void onCursorDeactivated() {
        if (this.handlerUnderCursor != null) {
            this.handleCursorExit(this.handlerUnderCursor, this.lastMouseX, this.lastMouseY);
        }
        this.lastMouseX = -1;
        this.lastMouseY = -1;

//        for (Int2ObjectMap.Entry<MouseRegionHandler> entry : this.clickInProgressHandlers.int2ObjectEntrySet()) {
//            entry.getValue().onClickAbandoned(entry.getIntKey());
//        }
//        this.clickInProgressHandlers.clear();
    }

    public boolean onMouseDown(int button) {
        if (this.handlerUnderCursor != null) {
            return this.handleMouseDown(this.handlerUnderCursor, button);
        }
        return true;
    }
    public boolean onMouseUp(int button) {
        if (this.handlerUnderCursor != null) {
            return this.handleMouseUp(this.handlerUnderCursor, button);
        }
        return true;
    }

    public void onMouseMove(int trueMouseX, int trueMouseY) {
        MouseRegionHandler newHandlerUnderCursor = this.getTopHandler(trueMouseX, trueMouseY);

        if (this.handlerUnderCursor != newHandlerUnderCursor) {
            if (this.handlerUnderCursor != null) {
                this.handleCursorExit(this.handlerUnderCursor, trueMouseX, trueMouseY); // TODO: Would this maybe want the old position? Maybe both?
            }
            if (newHandlerUnderCursor != null) {
                this.handleCursorEnter(newHandlerUnderCursor, trueMouseX, trueMouseY);
            }
        } else if (this.handlerUnderCursor != null) {
            this.handleMouseMove(this.handlerUnderCursor, this.lastMouseX, this.lastMouseY, trueMouseX, trueMouseY);
        }

        this.lastMouseX = trueMouseX;
        this.lastMouseY = trueMouseY;
    }

    public boolean onScroll(double horizontalAmount, double verticalAmount) {
        if (this.handlerUnderCursor != null) {
            return this.handleScroll(this.handlerUnderCursor, horizontalAmount, verticalAmount);
        }
        return true;
    }


    private void handleCursorEnter(MouseRegionHandler handler, int trueMouseX, int trueMouseY) {
        this.handlerUnderCursor = handler;

        if (handler != null) {
            handler.onMouseEnter(trueMouseX, trueMouseY);
        }
    }
    private void handleCursorExit(MouseRegionHandler handler, int trueMouseX, int trueMouseY) {
        this.handlerUnderCursor = null;

        if (handler != null) {
            handler.onMouseExit(trueMouseX, trueMouseY);
        }
    }
    private boolean handleMouseDown(MouseRegionHandler handler, int button) {
        boolean allow = handler.onMouseDown(button);
        if (allow) {
//            this.clickInProgressHandlers.put(button, this.handlerUnderCursor);
        }
        return allow;
    }
    private boolean handleMouseUp(MouseRegionHandler handler, int button) {
        boolean allow = handler.onMouseUp(button);
        MouseRegionHandler clickHandler = null; //this.clickInProgressHandlers.remove(button);

        if (clickHandler != null) {
            if (clickHandler == handler) {
                allow |= clickHandler.onClick(button);
            } else {
                allow |= clickHandler.onClickAbandoned(button);
            }
        }
        return allow;
    }
    private void handleMouseMove(MouseRegionHandler handler, int lastMouseX, int lastMouseY, int trueMouseX, int trueMouseY) {
        handler.onMouseMove(lastMouseX, lastMouseY, trueMouseX, trueMouseY);
    }
    private boolean handleScroll(MouseRegionHandler handler, double horizontalAmount, double verticalAmount) {
        return handler.onScroll(horizontalAmount, verticalAmount);
    }

    private MouseRegionHandler getTopHandler(int x, int y) {
        if (x == -1 && y == -1) {
            return null;
        }

        ListIterator<MouseRegionHandler> iterator = this.mouseRegionHandlers.listIterator(this.mouseRegionHandlers.size());
        while (iterator.hasPrevious()) {
            MouseRegionHandler handler = iterator.previous();
            if (handler != null && handler.isMouseOver(x, y)) {
                return handler;
            }
        }
        return null;
    }

    public Iterable<MouseRegionHandler> getHandlersIterator() {
        return this.mouseRegionHandlers;
    }
}
