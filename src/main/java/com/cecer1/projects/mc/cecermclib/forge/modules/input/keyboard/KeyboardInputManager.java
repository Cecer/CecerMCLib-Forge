package com.cecer1.projects.mc.cecermclib.forge.modules.input.keyboard;

import com.cecer1.projects.mc.cecermclib.common._misc.collections.Tree;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.function.Function;

public class KeyboardInputManager {

    private final TabCycleGroup rootHandler = new RootKeyboardInputHandler();

    private final Tree<IKeyboardInputHandler> handlers = new Tree<>(rootHandler);
    private Tree<IKeyboardInputHandler> leafHandlerTree;

    public Tree<IKeyboardInputHandler> getHandlers() {
        return this.handlers;
    }
    public Tree<IKeyboardInputHandler> getLeafHandlerTree() {
        return this.leafHandlerTree;
    }
    public void setLeafHandlerTree(Tree<IKeyboardInputHandler> leafHandlerTree) {
        this.leafHandlerTree = leafHandlerTree;
    }
    
    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void onKeyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        int key = Keyboard.getEventKey();
        
        if (Keyboard.getEventKeyState()) {
            this.onKeyboardInputDown(event);
        } else {
            this.onKeyboardInputUp(event);
        }
    }
    
    @FunctionalInterface
    private interface ScreenKeyEventConsumer {
        IKeyboardInputHandler.KeyboardInputResult handle();
    }
    private boolean onKeyboardInputDown(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        return this.onAllowKeyEvent(handler -> handler::onKeyboardKeyDown);
    }
    private boolean onKeyboardInputUp(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        return this.onAllowKeyEvent(handler -> handler::onKeyboardKeyUp);
    }

    private boolean onAllowKeyEvent(Function<IKeyboardInputHandler, ScreenKeyEventConsumer> handlerFunc) {
        Tree<IKeyboardInputHandler> handlerTree = this.getLeafHandlerTree();
        while (handlerTree != null) {
            IKeyboardInputHandler handler = handlerTree.getValue();
            IKeyboardInputHandler.KeyboardInputResult result;

            if (handler == null) {
                return true;
            } else {
                result = handlerFunc.apply(handler).handle();
            }

            switch (result) {
                case BUBBLE_UP: {
                    handlerTree = handlerTree.getParent();
                    break;
                }
                case CONSUME: {
                    return false;
                }
                case PASSIVE: {
                    return true;
                }
            }
        }
        return true;
    }

    public Tree<IKeyboardInputHandler> tabCycle(boolean backwards) {
        Tree<IKeyboardInputHandler> handler = this.getLeafHandlerTree();
        while (!(handler.getValue() instanceof TabCycleGroup)) {
            handler = handler.getParent();
        }
        if (backwards) {
            return ((TabCycleGroup) handler.getValue()).tabCycleBack();
        } else {
            return ((TabCycleGroup) handler.getValue()).tabCycleNext();
        }
    }
}
