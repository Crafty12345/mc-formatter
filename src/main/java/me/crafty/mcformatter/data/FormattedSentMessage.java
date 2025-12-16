package me.crafty.mcformatter.data;

import me.crafty.mcformatter.McFormatterMod;
import me.crafty.mcformatter.parser.ParserTree;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;

// Parts of this were copied from
// https://github.com/Patbox/StyledChat/blob/1.21.5/src/main/java/eu/pb4/styledchat/other/StyledChatSentMessage.java
public record FormattedSentMessage(SignedMessage message,
                                   Text override,
                                   MessageType.Parameters parameters,
                                   RegistryKey<MessageType> sourceType) implements SentMessage {
    public Text content() {
        return message().getContent();
    }

    @Override
    public void send(ServerPlayerEntity pSender, boolean filterMaskEnabled, MessageType.Parameters pParams) {
        SignedMessage signedMessage = message();
        ParserTree tree = new ParserTree();
        tree.parse(signedMessage.getSignedContent());
        String senderName = pSender.getDisplayName().getString();
        String prefix = "<" + senderName + "> ";
        MutableText txt = Text.literal(prefix);
        txt.append(tree.toText());
        pSender.networkHandler.sendChatMessage(signedMessage, MessageType.params(McFormatterMod.MESSAGE_TYPE_ID, pSender.server.getRegistryManager(), txt));
    }
}
