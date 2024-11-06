import java.awt.*;

public class Triangle extends DisplayObject{
    public int x1, y1;
    public int x2, y2;
    public int x3, y3;

    public static int minCord(int a,int b,int c){
        return Math.min(a,Math.min(b,c));
    }

    public static int maxCord(int a,int b,int c){
        return Math.max(a,Math.max(b,c));
    }

    public Triangle(int x1, int y1,int x2,int y2, int x3,int y3,int r1, int g1, int b1,int r2, int g2, int b2, int lineSize) {
        super(( minCord(x1,x2,x3) + maxCord(x1,x2,x3))/2,( minCord(y1,y2,y3) + maxCord(y1,y2,y2))/2, r1,g1,b1,r2,g2,b2,lineSize);
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.x3=x3;
        this.y3=y3;
        x1Frame= Math.min(x1,Math.min(x2,x3))-lineSize/2;
        y1Frame=Math.min(y1,Math.min(y2,y3))-lineSize/2;
        x2Frame=Math.max(x1,Math.max(x2,x3))+lineSize/2;
        y2Frame=Math.max(y1,Math.max(y2,y3))+lineSize/2;
        x1Inner=x1Frame+lineSize;
        y1Inner=y1Frame+lineSize;
        x2Inner=x2Frame-lineSize;
        y2Inner=y2Frame-lineSize;

    }

    public void draw(Graphics2D g){
        g.setColor(new Color(rFill,gFill,bFill));
        g.fillPolygon(new int[]{x1,x2,x3},new int[]{y1,y2,y3},3);
        g.setStroke(new BasicStroke(lineSize));
        g.setColor(new Color(rDraw,gDraw,bDraw));
        g.drawPolygon(new int[]{x1,x2,x3},new int[]{y1,y2,y3},3);
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
        this.x3+=deltaX;
        this.y3+=deltaY;
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
        return "Triangle";

    }
}