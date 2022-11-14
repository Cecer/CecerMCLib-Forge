package com.cecer1.projects.mc.cecermclib.forge.environment.mixin;

import com.cecer1.projects.mc.cecermclib.forge.modules.text.IGuiScreenExposer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin extends Gui implements GuiYesNoCallback, IGuiScreenExposer {

    @Shadow
    protected abstract void handleComponentHover(IChatComponent component, int x, int y);
    @Override
    public void handleComponentHover_exposed(IChatComponent component, int x, int y) {
        this.handleComponentHover(component, x, y);
    }

    @Shadow
    protected abstract boolean handleComponentClick(IChatComponent component);
    @Override
    public boolean handleComponentClick_exposed(IChatComponent component) {
        return this.handleComponentClick(component);
    }
}
