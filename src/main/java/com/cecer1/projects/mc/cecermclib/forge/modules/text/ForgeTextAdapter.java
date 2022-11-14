package com.cecer1.projects.mc.cecermclib.forge.modules.text;

import com.cecer1.projects.mc.cecermclib.common.modules.text.ITextAdapter;
import com.cecer1.projects.mc.cecermclib.common.modules.text.WrappedComponent;
import com.cecer1.projects.mc.cecermclib.common.modules.text.parts.Click;
import com.cecer1.projects.mc.cecermclib.common.modules.text.parts.Hover;
import com.cecer1.projects.mc.cecermclib.common.modules.text.parts.components.AbstractXmlTextComponent;
import com.cecer1.projects.mc.cecermclib.common.modules.text.parts.components.TextXmlTextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Collection;

public class ForgeTextAdapter implements ITextAdapter<IChatComponent> {

    private FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRendererObj;
    }

    @Override
    public int getStringWidth(String s) {
        return this.getFontRenderer().getStringWidth(s);
    }
    @Override
    public WrappedComponent<IChatComponent> newRootComponent() {
        return new WrappedForgeComponent(new ChatComponentText(""));
    }

    @Override
    public WrappedComponent<IChatComponent> adapt(Collection<AbstractXmlTextComponent> parts) {
        WrappedComponent<IChatComponent> out = ITextAdapter.super.adapt(parts);
        if (out == null) {
            out = new WrappedForgeComponent(new ChatComponentText("Error: Failed to compile XML message!"));
        }
        return out;
    }
    @Override
    public void appendPart(WrappedComponent<IChatComponent> fullComponent, TextXmlTextComponent part) {
        IChatComponent partComponent = new ChatComponentText(part.getText());
        
        ChatStyle style = partComponent.getChatStyle()
                .setBold(part.getState().isBold())
                .setItalic(part.getState().isItalic())
                .setUnderlined(part.getState().isUnderlined())
                .setStrikethrough(part.getState().isStrikethrough())
                .setObfuscated(part.getState().isObfuscated())
                .setColor(EnumChatFormatting.valueOf(part.getState().getColor().getValue().toUpperCase()))
                .setInsertion(part.getState().getInsertion());
        
        if (part.getState().getClick() != Click.NONE) {
            style = style.setChatClickEvent(this.adaptClick(part.getState().getClick()));
        }
        if (part.getState().getHover() != Hover.NONE) {
            style = style.setChatHoverEvent(this.adaptHover(part.getState().getHover()));
        }
        fullComponent.appendChild(partComponent.setChatStyle(style));
    }
    private ClickEvent adaptClick(Click click) {
        if (click instanceof Click.OpenFile) {
            return new ClickEvent(ClickEvent.Action.OPEN_FILE, ((Click.OpenFile) click).getPath());
        }
        if (click instanceof Click.RunCommand) {
            return new ClickEvent(ClickEvent.Action.RUN_COMMAND, ((Click.RunCommand) click).getCommand());
        }
        if (click instanceof Click.Suggest) {
            return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ((Click.Suggest) click).getSuggest());
        }
        if (click instanceof Click.ChangePage) {
            return new ClickEvent(ClickEvent.Action.CHANGE_PAGE, ((Click.ChangePage) click).getPage());
        }
        if (click instanceof Click.Url) {
            return new ClickEvent(ClickEvent.Action.OPEN_URL, ((Click.Url) click).getUrl());
        }
        throw new IllegalArgumentException("Unsupported click type: " + click.getClass().getName());
    }
    private HoverEvent adaptHover(Hover hover) {
        if (hover instanceof Hover.Text) {
            return new HoverEvent(HoverEvent.Action.SHOW_TEXT, IChatComponent.Serializer.jsonToComponent(((Hover.Text) hover).getJson()));
        }
        if (hover instanceof Hover.Item) {
            return new HoverEvent(HoverEvent.Action.SHOW_ITEM, IChatComponent.Serializer.jsonToComponent(((Hover.Item) hover).getJson()));
        }
        if (hover instanceof Hover.Entity) {
            return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, IChatComponent.Serializer.jsonToComponent(((Hover.Entity) hover).getJson()));
        }
        if (hover instanceof Hover.Segment) {
            WrappedComponent<IChatComponent> segment = this.adapt(((Hover.Segment) hover).getComponentList());
            return new HoverEvent(HoverEvent.Action.SHOW_TEXT, segment.getComponent());
        }
        throw new IllegalArgumentException("Unsupported hover type: " + hover.getClass().getName());
    }

    @Override
    public int getFontHeight() {
        return this.getFontRenderer().FONT_HEIGHT;
    }
}