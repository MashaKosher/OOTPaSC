import java.awt.*;

public class Circle extends DisplayObject {
    public int radius;
    public int x1, y1;

    public Circle(int x1, int y1,int radius, int r1, int g1, int b1,int r2, int g2, int b2, int lineSize) {
        super(x1+radius, y1+radius, r1,g1,b1,r2,g2,b2,lineSize);
        this.radius=radius;
        this.x1=x1;
        this.y1=y1;
        x1Frame=x1;
        y1Frame=y1;
        x2Frame=x1+2*radius;
        y2Frame=y1+2*radius;
        x1Inner=x1Frame+lineSize;
        y1Inner=y1Frame+lineSize;
        x2Inner=x2Frame-lineSize;
        y2Inner=y2Frame-lineSize;

    }

    @Override
    public void draw(Graphics2D g){
        g.setColor(new Color(rDraw,gDraw,bDraw));
        g.fillOval(x1, y1, 2*radius, 2*radius);
        g.setColor(new Color(rFill,gFill,bFill));
        g.fillOval(x1Inner, y1Inner, 2*(radius-lineSize), 2*(radius-lineSize));

    }
    @Override
    public void upd(int x,int y) {
        int deltaX=x-this.x;
        int deltaY=y-this.y;
        this.x=x;
        this.y=y;
        this.x1+=deltaX;
        this.y1+=deltaY;
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
        return "Circle";
    }

}

