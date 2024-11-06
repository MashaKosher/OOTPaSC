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
    public String buftext;
    public int rt;
    public int gt;
    public int bt;
    public int x, y;
    public boolean autoTransform;
    int w,  h;
    public Text(int x1,int y1, int w, int h,String font, int textSize,String text , int rt, int gt, int bt,boolean autoTransform){
        this.text=text;
        this.buftext=text;
        this.fontString=font;
        this.fontSize=textSize;
        createFont(font,textSize);
        this.rt=rt;
        this.gt=gt;
        this.bt=bt;
        x=x1;
        y=y1;
        this.w=w;
        this.h=h;
        this.autoTransform=autoTransform;
    }
    public void draw(Graphics2D g){
        g.setColor(new Color(rt,gt,bt));
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int width = fontMetrics.stringWidth(text);
        int height=fontMetrics.getHeight();
        if(autoTransform==false){
            int y1;
            if(text.contains("\n")) {
                y1=y;
                for (String line : text.split("\n")) {
                    width = fontMetrics.stringWidth(line);
                    g.drawString(line, x + w / 2 - width / 2, y1 += height);
                }
            }
            else {
                y1=y+height;
                g.drawString(text, x + w / 2 - width / 2, y1);
            }
        }
        else if(width<=w){
            g.drawString(text,x+w/2-width/2,y+h/2);
        }
        else
        drawParagraph(g,text,w,x,y);

    }
    public void drawParagraph(Graphics2D g, String paragraph, float width, int x1, int y1) {
        AttributedString attStr = new AttributedString(paragraph);
        attStr.addAttribute(TextAttribute.FONT, g.getFont());
        LineBreakMeasurer linebreaker = new LineBreakMeasurer(attStr.getIterator(), g.getFontRenderContext());

        float y = 0.0f;
        while (linebreaker.getPosition() < paragraph.length()) {
            TextLayout tl = linebreaker.nextLayout(width);
            y += tl.getAscent();
            tl.draw(g, x1, y+y1);
            y += tl.getDescent() + tl.getLeading();
        }
    }
    public void createFont(String str, int size){
        String fontFamily;
        int fontStyle;
        String fontStr;
        String[] s = str.split(" ");
        if(s[0].trim().toLowerCase().equals("serif"))
            fontFamily="Serif";
        else if(s[0].trim().toLowerCase().equals("sansserif"))
            fontFamily="SansSerif";
        else if(s[0].trim().toLowerCase().equals("monospaced"))
            fontFamily="Monospaced";
        else if(s[0].trim().toLowerCase().equals("dialog"))
            fontFamily="Dialog";
        else if(s[0].trim().toLowerCase().equals("dialoginput"))
            fontFamily="DialogInput";
        else{
            fontFamily="Serif";
        }
        if(s[1].trim().toLowerCase().equals("plain")){
            fontStyle=Font.PLAIN;
            fontStr=fontFamily+" Plain";
        }
        else if(s[1].trim().toLowerCase().equals("bold")) {
            fontStyle = Font.BOLD;
            fontStr=fontFamily+" Bold";
        }
        else if(s[1].trim().toLowerCase().equals("italic")) {
            fontStyle = Font.ITALIC;
            fontStr=fontFamily+" Italic";
        }
        else{
            fontStyle=Font.PLAIN;
            fontStr=fontFamily+" Plain";
        }
        this.fontFamily=fontFamily;
        this.fontStyle=fontStyle;
        this.fontSize=size;
        this.fontString=fontStr;
        this.font=new Font(fontFamily,fontStyle,size);
      
    }
    public void createFont(int size){
        this.fontSize=size;
        this.font= new Font(this.fontFamily,this.fontStyle,size);
    }
    public void createFont(String str){
        createFont(str,this.fontSize);
    }
}
