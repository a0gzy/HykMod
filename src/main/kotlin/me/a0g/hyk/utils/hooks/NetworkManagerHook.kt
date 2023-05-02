package me.a0g.hyk.utils.hooks


import io.netty.channel.ChannelHandlerContext
import me.a0g.hyk.core.events.PacketEvent
import net.minecraft.network.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun onReceivePacket(context: ChannelHandlerContext, packet: Packet<*>, ci: CallbackInfo) {
    if (PacketEvent.ReceiveEvent(packet).postAndCatch()) ci.cancel()
}

fun onSendPacket(packet: Packet<*>, ci: CallbackInfo) {
    if (PacketEvent.SendEvent(packet).postAndCatch()) ci.cancel()
}