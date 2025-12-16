package me.crafty.mcformatter.parser;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.net.URI;
import java.net.URISyntaxException;

public class ParserNode {

    private String content;

    private boolean bold;
    private boolean italics;
    private boolean emoji;
    private String linkTarget = null;

    public ParserNode(String pContent) {
        content = pContent;
    }

    public ParserNode() {content = null;}

    public String getContent() { return content; }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<RESET>");
        if(bold) {
            builder.append("<BOLD>");
        }
        if (italics) {
            builder.append("<ITALIC>");
        }
        builder.append(content);
        return builder.toString();
    }

    public MutableText toText() {
        Style style = Style.EMPTY.withBold(bold).withItalic(italics);
        if (linkTarget != null) {
            try {
                style = style.withColor(TextColor.fromFormatting(Formatting.AQUA)).withUnderline(true);
                style = style.withClickEvent(new ClickEvent.OpenUrl(new URI(linkTarget)));
            } catch (URISyntaxException e) {

            }
        }
        MutableText result = Text.literal(content)
                .setStyle(style);
        return result;
    }

    public boolean isBold()         { return bold;               }
    public boolean isItalics()      { return italics;            }
    public boolean isEmoji()        { return emoji;              }
    public boolean isLink()         { return linkTarget != null; }

    public void setContent(String pNewContent) {content = pNewContent;}
    public void setBold(boolean pShouldBold) {bold = pShouldBold;}
    public void setItalics(boolean pShouldItalic) {italics = pShouldItalic;}
    public void setLinkTarget(String pLink) {linkTarget = pLink;}
}