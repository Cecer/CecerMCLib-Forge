package com.cecer1.projects.mc.cecermclib.forge.environment.mod;

import com.cecer1.projects.mc.cecermclib.common.CecerMCLib;
import com.cecer1.projects.mc.cecermclib.forge.environment.ForgeClientEnvironment;
import com.cecer1.projects.mc.cecermclib.forge.environment.ForgeServerEnvironment;

public abstract class ForgeProxy {
    public abstract void initEnvironment();

    public static class Client extends ForgeProxy {
        
        @Override
        public void initEnvironment() {
            CecerMCLib.initEnvironment(new ForgeClientEnvironment());
        }
    }

    public static class Server extends ForgeProxy {
    
        @Override
        public void initEnvironment() {
            CecerMCLib.initEnvironment(new ForgeServerEnvironment());
        }
    }
}
