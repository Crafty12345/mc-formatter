package me.crafty.mcformatter.data;

import me.crafty.mcformatter.McFormatterMod;
import me.crafty.mcformatter.parser.ParserTree;
import net.minecraft.datafixer.fix.TextComponentStrictJsonFix;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;

import javax.swing.text.MutableAttributeSet;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
        System.out.println("Content=" + signedMessage.getSignedContent());
        //Text txt = Text.literal(tree.toString());
        MutableText txt = tree.toText();

        McFormatterMod.LOGGER.info("Output: " + tree.toString());
        //McFormatterMod.LOGGER.info("sending message");
        pSender.networkHandler.sendChatMessage(signedMessage, MessageType.params(McFormatterMod.MESSAGE_TYPE_ID, pSender.server.getRegistryManager(), txt));
    }
}
