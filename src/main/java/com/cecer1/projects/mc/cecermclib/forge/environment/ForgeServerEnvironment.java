package com.cecer1.projects.mc.cecermclib.forge.environment;

import com.cecer1.projects.mc.cecermclib.common.config.ICecerMCLibConfig;
import com.cecer1.projects.mc.cecermclib.common.environment.IServerEnvironment;
import com.cecer1.projects.mc.cecermclib.common.modules.ModuleRegistrationCallback;
import com.cecer1.projects.mc.cecermclib.forge.environment.AbstractForgeEnvironment;

public class ForgeServerEnvironment extends AbstractForgeEnvironment implements IServerEnvironment {

    public ForgeServerEnvironment() {
        super(Side.SERVER);
    }

    @Override
    public void registerModules(ModuleRegistrationCallback.RegistrationContext ctx) {
        super.registerModules(ctx);
        this.registerSideModules(ctx);
    }

    @Override
    public ICecerMCLibConfig getConfig() {
        return new ICecerMCLibConfig() {
            @Override
            public boolean isLongChatBackportEnabled() {
                return false; // TODO: Move this to another mod?
            }

            @Override
            public boolean isFastWorldSwitchEnabled() {
                return false; // Meaningless on servers
            }
        };
    }
}
