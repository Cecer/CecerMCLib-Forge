package com.cecer1.projects.mc.cecermclib.forge.modules.text;

import net.minecraft.util.IChatComponent;

public interface IGuiScreenExposer {

    void handleComponentHover_exposed(IChatComponent component, int x, int y);

    boolean handleComponentClick_exposed(IChatComponent component);
}
