package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context;

import com.cecer1.projects.mc.cecermclib.common._misc.annotations.InternalUseOnly;
import com.cecer1.projects.mc.cecermclib.common.modules.text.WrappedComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayDeque;
import java.util.Deque;

public class RenderContext {
        
    private float partialTicks;
    private WrappedComponent<?> hoverTextComponent;
    private Deque<AbstractCanvas> lastCanvases; // Used for logging in the event of exceptions during rendering
    
    private AbstractCanvas canvas;

    public RenderContext(float partialTicks) {
        this.partialTicks = partialTicks;
        this.lastCanvases = new ArrayDeque<>();

        RootCanvas rootCanvas = new RootCanvas(this);
        rootCanvas.setOpenTrace();
        this.pushCanvas(rootCanvas);
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
    public AbstractCanvas getCanvas() {
        return this.canvas;
    }
    void pushCanvas(AbstractCanvas canvas) {
        if (!this.lastCanvases.isEmpty()) {
            while (this.lastCanvases.peek() != this.canvas) {
                this.lastCanvases.pop();
            }
        }
        
        this.lastCanvases.push(canvas);
        this.canvas = canvas;
    }
    void popCanvas() {
        this.canvas = this.canvas.getParentCanvas();
    }

    @InternalUseOnly
    public Deque<AbstractCanvas> getLastCanvases() {
        return this.lastCanvases;
    }

    public WrappedComponent<?> getHoverTextComponent() {
        return hoverTextComponent;
    }
    public void setHoverTextComponent(WrappedComponent<?> hoverTextComponent) {
        this.hoverTextComponent = hoverTextComponent;
    }
    
    // TODO: Abstract text rendering for cross platform support
    public FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRendererObj;
    }
}