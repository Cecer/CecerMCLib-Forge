package com.cecer1.projects.mc.cecermclib.forge.environment.mod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CecerMCLibForgeMod.MODID, version = CecerMCLibForgeMod.VERSION)
public class CecerMCLibForgeMod {

    public static final String MODID = "cecerpersonalmod";
    public static final String VERSION = "1.0";
    
    @SidedProxy(
            clientSide = "com.cecer1.projects.mc.cecermclib.forge.environment.mod.ForgeProxy$Client",
            serverSide = "com.cecer1.projects.mc.cecermclib.forge.environment.mod.ForgeProxy$Server")
    public static ForgeProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.initEnvironment();
    }
}
