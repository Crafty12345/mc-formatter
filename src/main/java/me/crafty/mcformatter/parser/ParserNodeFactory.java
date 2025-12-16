package me.crafty.mcformatter.parser;

public class ParserNodeFactory {
    public ParserNodeFactory() {}
    public ParserNode createNode(String pText, boolean pIsBold, boolean pIsItalic) {
        ParserNode result = new ParserNode();
        result.setContent(pText);
        result.setBold(pIsBold);
        result.setItalics(pIsItalic);
        return result;
    }
    public ParserNode createNode(StringBuilder pText, boolean pIsBold, boolean pIsItalic) {
        return createNode(pText.toString(), pIsBold, pIsItalic);
    }
}
