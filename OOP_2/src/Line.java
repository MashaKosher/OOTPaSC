import java.awt.*;

public class Line extends DisplayObject{

    private int x1, y1;
    private int x2, y2;

    public Line(int x1, int y1, int x2, int y2,  int r, int g, int b, int lineSize) {
        super((x2+x1)/2, (y2+y1)/2, r,g,b,r,g,b, lineSize);
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        x1Frame=Math.min(x1,x2)-lineSize/2;
        y1Frame=Math.min(y1,y2)-lineSize/2;
        x2Frame=Math.max(x1,x2)+lineSize/2;
        y2Frame=Math.max(y1,y2)+lineSize/2;

    }

    public void draw(Graphics2D g){
        g.setStroke(new BasicStroke(lineSize));
        g.setColor(new Color(rDraw,gDraw,bDraw));
        g.drawLine(x1,y1,x2,y2);
    }
    @Override
    public void upd(int x,int y) {
        int deltaX=x-this.x;
        int deltaY=y-this.y;
        this.x=x;
        this.y=y;
        this.x1+=deltaX;
        this.y1+=deltaY;
        this.x2+=deltaX;
        this.y2+=deltaY;
        x1Frame+=deltaX;
        y1Frame+=deltaY;
        x2Frame+=deltaX;
        y2Frame+=deltaY;
        x1Inner+=deltaX;
        y1Inner+=deltaY;
        x2Inner+=deltaX;
        y2Inner+=deltaY;
    }
    @Override
    public String toString(){
        return "Line";

    }
}

