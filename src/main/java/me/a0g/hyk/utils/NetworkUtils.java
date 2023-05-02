package me.a0g.hyk.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class NetworkUtils {

    public static void sendPacket(final Packet<?> packet){
        Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)packet);
    }

}
