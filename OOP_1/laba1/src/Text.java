import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;

public class Text {

    public Font font;
    String fontFamily;
    int fontStyle;
    String fontString;
    int fontSize;
    public String text;
    public String bufText;
    public int rt, gt, bt;

    public int x, y;
    public boolean autoTransform;
    int w, h;

    public Text(int x1, int y1, int w, int h, String font, int textSize, String text, int rt, int gt, int bt, boolean autoTransform) {
        this.text = text;
        this.bufText = text;
        this.fontString = font;
        this.fontSize = textSize;
        createFont(font, textSize);
        this.rt = rt;
        this.gt = gt;
        this.bt = bt;
        x = x1;
        y = y1;
        this.w = w;
        this.h = h;
        this.autoTransform = autoTransform;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(rt, gt, bt));
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int width = fontMetrics.stringWidth(text);
        int height = fontMetrics.getHeight();
        if (!autoTransform) {
            int y1;
            if (text.contains("\n")) {
                y1 = y;
                for (String line : text.split("\n")) {
                    width = fontMetrics.stringWidth(line);
                    g.drawString(line, x + w / 2 - width / 2, y1 += height);
                }
            } else {
                y1 = y + height;
                g.drawString(text, x + w / 2 - width / 2, y1);
            }
        } else if (width <= w) {
            g.drawString(text, x + w / 2 - width / 2, y + h / 2);
        } else drawParagraph(g, text, w, x, y);

    }

    public void drawParagraph(Graphics2D g, String paragraph, float width, int x1, int y1) {
        AttributedString attStr = new AttributedString(paragraph);
        attStr.addAttribute(TextAttribute.FONT, g.getFont());
        LineBreakMeasurer linebreaker = new LineBreakMeasurer(attStr.getIterator(), g.getFontRenderContext());

        float y = 0.0f;
        while (linebreaker.getPosition() < paragraph.length()) {
            TextLayout tl = linebreaker.nextLayout(width);
            y += tl.getAscent();
            tl.draw(g, x1, y + y1);
            y += tl.getDescent() + tl.getLeading();
        }
    }

    public void createFont(String str, int size) {
        String fontFamily;
        int fontStyle;
        String fontStr;
        String[] s = str.split(" ");
        String temp0 = s[0].trim().toLowerCase();

        fontFamily = switch (temp0) {
            case "sansserif" -> "SansSerif";

            case "dialog" -> "Dialog";

            case "dialoginput" -> "DialogInput";

            default -> "Serif";
        };

        String temp1 = s[1].trim().toLowerCase();

        switch (temp1) {
            case "bold" -> {
                fontStyle = Font.BOLD;
                fontStr = fontFamily + " Bold";
            }
            case "italic" -> {
                fontStyle = Font.ITALIC;
                fontStr = fontFamily + " Italic";
            }
            default -> {
                fontStyle = Font.PLAIN;
                fontStr = fontFamily + " Plain";
            }

        }

        this.fontFamily = fontFamily;
        this.fontStyle = fontStyle;
        this.fontSize = size;
        this.fontString = fontStr;
        this.font = new Font(fontFamily, fontStyle, size);

    }

    public void createFont(int size) {
        this.fontSize = size;
        this.font = new Font(this.fontFamily, this.fontStyle, size);
    }

    public void createFont(String str) {
        createFont(str, this.fontSize);
    }
}
