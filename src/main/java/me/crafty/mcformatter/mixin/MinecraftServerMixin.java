package me.crafty.mcformatter.mixin;

import me.crafty.mcformatter.McFormatterMod;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/* Adapted from https://github.com/Patbox/StyledChat/blob/1.21.5/src/main/java/eu/pb4/styledchat/mixin/MinecraftServerMixin.java */

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method="runServer", at= @At(value="INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeNano()J"))
    private void registerStart(CallbackInfo pCallback) {
        McFormatterMod.onServerStart((MinecraftServer) (Object)this);
    }

    @Inject(method="shutdown", at=@At("TAIL"))
    private void registerStop(CallbackInfo pCallback) {
        McFormatterMod.onServerStop();
    }
}
