package com.cecer1.projects.mc.cecermclib.forge.modules.input.keyboard;

public class RootKeyboardInputHandler extends TabCycleGroup {

    @Override
    public KeyboardInputResult onKeyboardKeyDown() {
        return KeyboardInputResult.PASSIVE;
    }

    @Override
    public KeyboardInputResult onKeyboardKeyUp() {
        return KeyboardInputResult.PASSIVE;
    }
}
