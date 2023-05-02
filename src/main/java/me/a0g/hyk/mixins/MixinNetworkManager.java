package me.a0g.hyk.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.a0g.hyk.core.events.PacketEvent;
import me.a0g.hyk.utils.hooks.NetworkManagerHookKt;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkManager.class, priority = 1001)
public class MixinNetworkManager {

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
//        if (new PacketEvent.ReceiveEvent(packet).postAndCatch()) ci.cancel();
        NetworkManagerHookKt.onReceivePacket(context, packet, ci);
    }

}
