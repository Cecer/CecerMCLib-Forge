package com.cecer1.projects.mc.cecermclib.forge.environment.mod;

import com.cecer1.projects.mc.cecermclib.common.CecerMCLib;
import com.cecer1.projects.mc.cecermclib.common.modules.chatmetadata.ChatMessageData;
import com.cecer1.projects.mc.cecermclib.common.modules.chatmetadata.ChatMetadataModule;
import com.cecer1.projects.mc.cecermclib.forge.environment.ForgeClientEnvironment;
import com.cecer1.projects.mc.cecermclib.forge.environment.ForgeServerEnvironment;
import com.cecer1.projects.mc.cecermclib.forge.modules.text.WrappedForgeComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class ForgeProxy {
    public abstract void initEnvironment();

    public static class Client extends ForgeProxy {
        
        @Override
        public void initEnvironment() {
            CecerMCLib.initEnvironment(new ForgeClientEnvironment());
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void onClientChatReceived(ClientChatReceivedEvent event) {
            WrappedForgeComponent component = new WrappedForgeComponent(event.message);
            CecerMCLib.get(ChatMetadataModule.class).process(new ChatMessageData(component));
        }
    }

    public static class Server extends ForgeProxy {
    
        @Override
        public void initEnvironment() {
            CecerMCLib.initEnvironment(new ForgeServerEnvironment());
        }
    }
}
