package com.cecer1.projects.mc.cecermclib.forge.modules.text;

import com.cecer1.projects.mc.cecermclib.common.modules.text.ChatInputMutateCallback;
import com.cecer1.projects.mc.cecermclib.common.modules.text.TextColor;
import com.cecer1.projects.mc.cecermclib.common.modules.text.WrappedComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class WrappedForgeComponent extends WrappedComponent<IChatComponent> {

    public WrappedForgeComponent(IChatComponent component) {
        super(component);
    }
    
    @Override
    public String getPlainString() {
        return this.getComponent().getUnformattedText();
    }
    @Override
    public String getFormattedString() {
        throw new UnsupportedOperationException("Not implemented");
    }
    @Override
    public String getJson() {
        return IChatComponent.Serializer.componentToJson(this.getComponent());
    }

    @Override
    public TextColor getColor() {
        EnumChatFormatting color = this.getComponent().getChatStyle().getColor();
        if (color == null) {
            return null;
        }
        return TextColor.fromName(color.getFriendlyName());
    }
    @Override
    public boolean isBold() {
        return this.getComponent().getChatStyle().getBold();
    }
    @Override
    public boolean isItalic() {
        return this.getComponent().getChatStyle().getItalic();
    }
    @Override
    public boolean isUnderline() {
        return this.getComponent().getChatStyle().getUnderlined();
    }
    @Override
    public boolean isStrikethrough() {
        return this.getComponent().getChatStyle().getStrikethrough();
    }
    @Override
    public boolean isObfuscated() {
        return this.getComponent().getChatStyle().getObfuscated();
    }

    @Override
    public void appendChild(IChatComponent child) {
        this.getComponent().appendSibling(child);
    }

    @Override
    public WrappedComponent<IChatComponent>[] getChildren() {
        List<IChatComponent> childList = this.getComponent().getSiblings();
        WrappedForgeComponent[] children = new WrappedForgeComponent[childList.size()];
        for (int i = 0; i < childList.size(); i++) {
            IChatComponent child = childList.get(i);
            children[i] = new WrappedForgeComponent(child);
        }
        return children;
    }

    @Override
    public WrappedComponent<IChatComponent> getCopyWithoutChildren() {
        IChatComponent copy = this.getComponent().createCopy();
        copy.getSiblings().clear();
        return new WrappedForgeComponent(copy);
    }

    @Override
    public void handleClick() {
        if (GuiScreen.isShiftKeyDown() && this.getComponent().getChatStyle().getInsertion() != null) {
            ChatInputMutateCallback.EVENT.invoker().handle(this.getComponent().getChatStyle().getInsertion(), false);
        } else {
            ClickEvent clickEvent = this.getComponent().getChatStyle().getChatClickEvent();
            if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                ChatInputMutateCallback.EVENT.invoker().handle(clickEvent.getValue(), true);
            } else {
                IGuiScreenExposer screen = (IGuiScreenExposer) Minecraft.getMinecraft().currentScreen;
                if (screen != null) {
                    screen.handleComponentClick_exposed(this.getComponent());
                }
            }
        }
    }
}