package me.crafty.mcformatter.parser;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;

public class ParserTree {

    private final LinkedList<ParserNode> nodes;

    public ParserTree() {
        nodes = new LinkedList<>();
    }

    public MutableText toText() {
        MutableText result = Text.literal("");
        for (ParserNode node : nodes) {
            result.append(node.toText());
        }
        return result;
    }

    public void parse(String pString) {
        StringBuilder builder = new StringBuilder();
        StringBuilder linkText = new StringBuilder();
        StringBuilder linkURL = new StringBuilder();

        char[] chars = pString.toCharArray();
        Queue<Character> charQ = new LinkedList<>();
        for (char currentChar: chars) {
            charQ.add(currentChar);
        }

        boolean bold = false;
        boolean italics = false;

        ParserNodeFactory factory = new ParserNodeFactory();

        while (!charQ.isEmpty()) {
            char previousChar = '\0';

            char char1 = charQ.poll();
            if (char1 == '*') {
                if (!charQ.isEmpty()) {
                    char char2 = charQ.poll();
                    if (char2 == '*') {
                        // If already bold, then this means that bold is closed
                        if (bold) {
                            if (italics) {
                                if (charQ.peek() == '*') {
                                    charQ.poll();
                                }
                            }
                            nodes.add(factory.createNode(builder, bold, italics));
                            // Clear builder
                            builder.setLength(0);
                            bold = false;
                        }
                        else if(!bold) {
                            if (boldClosed(charQ)) {
                                if (!builder.isEmpty()) {
                                    nodes.add(factory.createNode(builder, bold, italics));
                                    builder.setLength(0);
                                }
                                bold = true;
                            } else {
                                builder.append(char1);
                                builder.append(char2);
                            }
                        }
                    } else {
                        // TODO: Reduce code duplication here
                        if(italics) {
                            nodes.add(factory.createNode(builder, bold, italics));
                            builder.setLength(0);
                            italics = false;
                        }
                        else if (!italics) {
                            if (findChar(charQ, '*')) {
                                if (!builder.isEmpty()) {
                                    nodes.add(factory.createNode(builder, bold, italics));
                                    builder.setLength(0);
                                }
                                italics = true;
                            } else {
                                builder.append(char1);
                            }
                        }
                        builder.append(char2);
                    }
                } else {
                    if(italics) {
                        nodes.add(factory.createNode(builder, bold, italics));
                        builder.setLength(0);
                        italics = false;
                    }
                    else if (!italics) {
                        if (findChar(charQ, '*')) {
                            italics = true;
                        } else {
                            builder.append(char1);
                        }
                    }
                }

            } else if (char1 == '[') {
                if (hyperlinkClosed(charQ)) {
                    linkText = new StringBuilder();
                    char1 = charQ.poll();
                    while (char1 != ']') {
                        linkText.append(char1);
                        char1 = charQ.poll();
                    }
                    char1 = charQ.poll();
                    if (char1 == '(') {
                        linkURL = new StringBuilder();
                        char1 = charQ.poll();
                        while (char1 != ')') {
                            linkURL.append(char1);
                            char1 = charQ.poll();
                        }
                    }

                    if ((!linkText.isEmpty()) && (!linkURL.isEmpty())) {
                        if (isURI(linkURL.toString())) {
                            if (!builder.isEmpty()) {
                                nodes.add(factory.createNode(builder, bold, italics));
                                builder.setLength(0);
                            }

                            String text = linkText.toString();
                            String url = linkURL.toString();
                            ParserNode node = factory.createNode(text, bold, italics);
                            node.setLinkTarget(url);
                            nodes.add(node);
                        } else {
                            // Don't interpret as URL if it's not URL
                            builder.append('[');
                            builder.append(linkText);
                            builder.append("](");
                            builder.append(linkURL);
                            builder.append(')');
                        }
                    }
                }
            } else if (char1 == '<') {
                if (findChar(charQ, '>')) {
                    StringBuilder url = new StringBuilder();
                    char1 = charQ.poll();
                    while (char1 != '>') {
                        url.append(char1);
                        char1 = charQ.poll();
                    }
                    if (!url.isEmpty()) {
                        if (isURI(url.toString())) {
                            if (!builder.isEmpty()) {
                                nodes.add(factory.createNode(builder, bold, italics));
                                builder.setLength(0);
                            }

                            ParserNode node = factory.createNode(builder, bold, italics);
                            node.setContent(url.toString());
                            node.setLinkTarget(url.toString());
                            nodes.add(node);
                        } else {
                            // Don't interpret as URL if it's not URL
                            builder.append('<');
                            builder.append(url);
                            builder.append('>');
                        }
                    }
                }
            } else {
                builder.append(char1);
            }

            // If last character, but there are still characters in builder
            if(charQ.isEmpty()) {
                if(!builder.isEmpty()) {
                    nodes.add(factory.createNode(builder, bold, italics));
                }
            }
        }
    }

    private boolean isURI(String pStr) {
        try {
            URI uri = new URI(pStr);
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    private boolean boldClosed(Queue<Character> pChars) {
        boolean result = false;
        char previousChar = '\0';
        for (char currentChar : pChars) {
            if (!result) {
                if ((previousChar == '*') && (currentChar == '*')) {
                    result = true;
                }
                previousChar = currentChar;
            }
        }
        return result;
    }

    private boolean findChar(Queue<Character> pChars, char pTargetChar) {
        boolean result = false;
        for (char currentChar : pChars) {
            if (!result) {
                if (currentChar == pTargetChar) {
                    result = true;
                }
            }
        }
        return result;
    }

    private boolean hyperlinkClosed(Queue<Character> pChars) {
        boolean result = false;
        char previousChar = '\0';
        boolean endSquareBracketFound = false;
        boolean startParenthesisFound = false;
        boolean endParenthesisFound = false;
        for (char currentChar : pChars) {
            if (!result) {
                if ((currentChar == ']') && (!endSquareBracketFound)) {
                    endSquareBracketFound = true;
                }
                if (previousChar == ']') {
                    if (currentChar == '(') {
                        startParenthesisFound = true;
                    } else {
                        return false;
                    }
                }
                if ((endSquareBracketFound) && (startParenthesisFound) && (currentChar == ')')) {
                    endParenthesisFound = true;
                    result = true;
                }
                previousChar = currentChar;
            }
        }
        return result;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ParserNode node : nodes) {
            builder.append(node.toString());
        }
        return builder.toString();
    }
}
