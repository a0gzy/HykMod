package me.a0g.hyk.mixins;

import me.a0g.hyk.HykX;
import me.a0g.hyk.utils.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(FontRenderer.class)
public class FontRendererMixin {

    // NickChange
    @ModifyArg(method = "renderString",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"
            ), index = 0)
    private String renderStringAtPos(String text) {
        if(text != null) {
            if (HykX.config.getNameChanger()) {
                text = MinecraftUtil.INSTANCE.replaceUserName(text);
            }
        }

        return text;
    }

}
