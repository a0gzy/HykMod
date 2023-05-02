package me.a0g.hyk.mixins;


import me.a0g.hyk.HykX;
import me.a0g.hyk.core.events.MinecraftCloseEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow private int leftClickCounter;

    @Inject(method = "clickMouse", at = @At("RETURN"))
    private void hitFix(CallbackInfo ci){
        if(HykX.config.getHitFix()){
            leftClickCounter = 0;
        }
    }

    @Inject(method = "shutdown", at = @At("HEAD"), cancellable = true)
    private void disableRPC(CallbackInfo ci){
        if(new MinecraftCloseEvent().postAndCatch()) ci.cancel();
        if (HykX.INSTANCE.getDiscordRpc().isRpcActive()) {
            System.out.println("HYK RPC STOPPING");
            HykX.INSTANCE.getDiscordRpc().stopRpc();
        }
    }
}
