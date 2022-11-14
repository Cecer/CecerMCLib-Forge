package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.transformations;

import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.AbstractCanvas;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.AbstractStandardCanvas;
import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.RenderContext;

/**
 * A simplified canvas that is not intended to be applied directly but rather a result of a compound canvas.
 * Unlike an {@link AbstractStandardCanvas} canvas, this should generally not pop or push states and, in many cases, no unapply method is even required as a result.
 */
public abstract class AbstractTransformationCanvas extends AbstractCanvas {

    protected AbstractTransformationCanvas(AbstractCanvas parentCanvas, RenderContext ctx) {
        super(parentCanvas, ctx);
    }

    public void apply() { }

    public void unapply() {}
}
