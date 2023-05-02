package me.a0g.hyk.mixins.accessors;

import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S3BPacketScoreboardObjective.class)
public interface AccessorS3BPacketScoreboardObjective {
    @Accessor
    void setObjectiveValue(String value);
}
