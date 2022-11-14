package com.cecer1.projects.mc.cecermclib.forge.environment;

import com.cecer1.projects.mc.cecermclib.common.environment.AbstractEnvironment;
import com.cecer1.projects.mc.cecermclib.common.modules.ModuleRegistrationCallback;
import com.cecer1.projects.mc.cecermclib.common.modules.text.TextModule;
import com.cecer1.projects.mc.cecermclib.forge.modules.text.ForgeTextAdapter;

public abstract class AbstractForgeEnvironment extends AbstractEnvironment {

    public AbstractForgeEnvironment(AbstractEnvironment.Side side) {
        super(side);
    }
    public void registerModules(ModuleRegistrationCallback.RegistrationContext ctx) {
        super.registerModules(ctx);
        
        ctx.registerModule(new TextModule(new ForgeTextAdapter()));
    }
}
