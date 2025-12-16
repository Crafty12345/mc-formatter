package me.crafty.mcformatter.parser;

public class Hyperlink {
    private String text;
    private String link;
    public Hyperlink(String pText, String pLink) {
        this.text = pText;
        this.link = pLink;
    }

    public String getText() {return this.text;}
    public String getLink() {return this.link;}
}
