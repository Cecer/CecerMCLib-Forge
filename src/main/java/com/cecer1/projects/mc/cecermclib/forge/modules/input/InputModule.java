package com.cecer1.projects.mc.cecermclib.forge.modules.input;

import com.cecer1.projects.mc.cecermclib.common.modules.IModule;
import com.cecer1.projects.mc.cecermclib.forge.modules.input.keyboard.KeyboardInputManager;
import com.cecer1.projects.mc.cecermclib.forge.modules.input.mouse.MouseInputManager;

public class InputModule implements IModule {

    private final MouseInputManager mouseInputManager = new MouseInputManager();
    private final KeyboardInputManager keyboardInputManager = new KeyboardInputManager();

    public MouseInputManager getMouseInputManager() {
        return this.mouseInputManager;
    }
    public KeyboardInputManager getKeyboardInputManager() {
        return this.keyboardInputManager;
    }

    @Override
    public void onModuleRegister() {
        this.mouseInputManager.registerEvents();
        this.keyboardInputManager.registerEvents();
    }
}
