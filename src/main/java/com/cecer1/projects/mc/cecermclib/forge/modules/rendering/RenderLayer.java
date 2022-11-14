package com.cecer1.projects.mc.cecermclib.forge.modules.rendering;

import com.cecer1.projects.mc.cecermclib.common.CecerMCLib;
import com.cecer1.projects.mc.cecermclib.forge.modules.input.InputModule;
import com.cecer1.projects.mc.cecermclib.forge.modules.input.mouse.MouseInputManager;
import com.cecer1.projects.mc.cecermclib.forge.modules.input.mouse.MouseRegionHandler;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.RenderContext;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.fbo.FBO;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.fbo.FBOSession;

import java.util.ArrayList;
import java.util.List;

public class RenderLayer {

    private final FBO fbo;
    private final List<MouseRegionHandler> mouseRegionHandlers;

    public RenderLayer(int x, int y, int width, int height) {
        this.fbo = new FBO(width, height);
        this.mouseRegionHandlers = new ArrayList<>();
    }

    public int getWidth() {
        return this.fbo.getWidth();
    }

    public int getHeight() {
        return this.fbo.getHeight();
    }

    public void registerHandler(MouseRegionHandler handler) {
        synchronized (this.mouseRegionHandlers) {
            this.mouseRegionHandlers.add(handler);
        }
    }

    public void clearHandlers() {
        synchronized (this.mouseRegionHandlers) {
            this.mouseRegionHandlers.clear();
        }
    }

    private void applyMouseRegionHandlers() {
        MouseInputManager im = CecerMCLib.get(InputModule.class).getMouseInputManager();
        synchronized (this.mouseRegionHandlers) {
            for (MouseRegionHandler handler : this.mouseRegionHandlers) {
                im.registerHandler(handler);
            }
        }
    }

    public FBOSession openSession(RenderContext ctx) {
        return this.fbo.openSession(ctx);
    }
    public FBOSession openSession() {
        return this.fbo.openSession();
    }

    public void draw() {
        this.fbo.draw(0, 0);
        this.applyMouseRegionHandlers();
    }

    public void draw(float alpha) {
        this.fbo.draw(0, 0, alpha);
        this.applyMouseRegionHandlers();
    }

    public void dispose() {
        this.fbo.dispose();
    }
}
