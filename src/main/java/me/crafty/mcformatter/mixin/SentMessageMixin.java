package me.crafty.mcformatter.mixin;

import me.crafty.mcformatter.McFormatterMod;
import me.crafty.mcformatter.data.FormattedSentMessage;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.network.message.SentMessage;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SentMessage.class)
public interface SentMessageMixin {

    @Inject(method="of", at = @At("HEAD"), cancellable = true)
    private static void mcFormatter$setStyle(SignedMessage pMessage, CallbackInfoReturnable<SentMessage> pCallback) {
        Text override = pMessage.getContent();
        MessageType.Parameters params = MessageType.params(McFormatterMod.MESSAGE_TYPE_ID, McFormatterMod.server.getRegistryManager(), override);

        if(!pMessage.isSenderMissing()) {
            pCallback.setReturnValue(new FormattedSentMessage(pMessage,pMessage.getContent(), params, MessageType.CHAT));
        }
    }
}
