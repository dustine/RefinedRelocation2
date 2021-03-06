package net.blay09.mods.refinedrelocation2.network.container;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.container.IContainerNetworked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerContainerAction implements IMessageHandler<MessageContainerAction, IMessage> {
    @Override
    public IMessage onMessage(MessageContainerAction message, MessageContext ctx) {
        RefinedRelocation2.proxy.addScheduledTask(() -> {
            EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
            if(entityPlayer.openContainer instanceof IContainerNetworked) {
                ((IContainerNetworked) entityPlayer.openContainer).receiveAction(message.getName());
            }
        });
        return null;
    }
}
