package me.crafty.mcformatter.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTests {
    @Test
    public void testBold() {
        String str = "**text**";
        String expected = "<RESET><BOLD>text";
        ParserTree tree = new ParserTree();
        tree.parse(str);
        assertEquals(expected, tree.toString());

        str = "**text";
        expected = "<RESET>**text";
        tree = new ParserTree();
        tree.parse(str);
        assertEquals(expected, tree.toString());

        str = "text**";
        expected = "<RESET>text**";
        tree = new ParserTree();
        tree.parse(str);
        assertEquals(expected, tree.toString());

        str = "**bold**not bold";
        expected = "<RESET><BOLD>bold<RESET>not bold";
        tree = new ParserTree();
        tree.parse(str);
        assertEquals(expected, tree.toString());

        str = "not bold**bold**";
        expected = "<RESET>not bold<RESET><BOLD>bold";
        tree = new ParserTree();
        tree.parse(str);
        assertEquals(expected, tree.toString());
    }

    @Test
    public void testItalics() {
        String[] inputs = new String[]   { "*text*", "*text", "text*", "*italics*not italics", "not italics*italics*"};
        String[] expecteds = new String[]{ "<RESET><ITALIC>text", "<RESET>*text", "<RESET>text*", "<RESET><ITALIC>italics<RESET>not italics", "<RESET>not italics<RESET><ITALIC>italics"};
        String[] actuals = new String[expecteds.length];

        ParserTree tree;

        for (int i = 0; i < actuals.length; i++) {
            tree = new ParserTree();
            tree.parse(inputs[i]);
            actuals[i] = tree.toString();
        }
        assertArrayEquals(expecteds, actuals);
    }

    @Test
    public void testItalicAndBold() {
        String input = "***bold and italic***";
        String expected = "<RESET><BOLD><ITALIC>bold and italic";
        ParserTree tree = new ParserTree();
        tree.parse(input);
        assertEquals(expected, tree.toString());

        input = "**bold***italic*";
        expected = "<RESET><BOLD>bold<RESET><ITALIC>italic";
        tree = new ParserTree();
        tree.parse(input);
        assertEquals(expected, tree.toString());

        input = "***bold and italic**just italic*";
        expected = "<RESET><BOLD><ITALIC>bold and italic<RESET><ITALIC>just italic";
        tree = new ParserTree();
        tree.parse(input);
        assertEquals(expected, tree.toString());

        input = "***bold and italic*just bold**";
        expected = "<RESET><BOLD><ITALIC>bold and italic<RESET><BOLD>just bold";
        tree = new ParserTree();
        tree.parse(input);
        assertEquals(expected, tree.toString());
    }
}
