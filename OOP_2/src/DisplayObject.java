import java.awt.*;

import static java.lang.Math.*;

public abstract class DisplayObject {

    public int x1Frame, y1Frame;
    public int x2Frame, y2Frame;


    public int x1Inner, y1Inner;
    public int x2Inner, y2Inner;


    public int x, y;


    public int rDraw, gDraw, bDraw;


    public int rFill, gFill, bFill;

    public int lineSize;

    //new

    public int x0, y0;

    public int xf, yf;

    public double v;

    public double a;

    public double dX, dY;

    public long createTime;
    double angle;
    long CONST = 1000000000;

    public DisplayObject(int x, int y, int r1, int g1, int b1, int r2, int g2, int b2, int lineSize) {
        this.x = x;
        this.y = y;
        rDraw = r1;
        gDraw = g1;
        bDraw = b1;
        rFill = r2;
        gFill = g2;
        bFill = b2;
        this.lineSize = lineSize;
    }

    public abstract void draw(Graphics2D g);

    public abstract void upd(int x, int y);

    public void move(double v, int x1, int y1, double ang, long t) {
        this.angle=ang;
        this.dX = x1 + v * Math.cos(this.angle * Math.PI / 180) * (t - createTime) / CONST;
        this.dY = y1 + v * Math.sin(this.angle * Math.PI / 180) * (t - createTime) / CONST;

    }

    public void moveAcc(double v, double a, int x1, int y1, double ang, long t) {
        this.angle=ang;
        this.dX= x1+v*Math.cos(angle*Math.PI/180)*(t-createTime)/CONST+a*Math.cos(angle*Math.PI/180)*((double) (t - createTime) /CONST)*((double) (t - createTime) /CONST)/2;
        this.dY=y1+v*Math.sin(angle*Math.PI/180)*(t-createTime)/CONST+a*sin(angle*Math.PI/180)*((double) (t - createTime) /CONST)*((double) (t - createTime) /CONST)/2;

    }


    public void move(double v, int x1, int y1, int x2, int y2, long t) {
        double s = sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2-y1));
        double cos = (x2 - x1) / s;
        double sin = (y2 - y1) / s;
        this.dX = x1 + v * cos * (t - createTime) / CONST;
        this.dY = y1 + v * sin * (t - createTime) / CONST;

    }

    public void moveAcc(double v, double a, int x1, int y1, int x2, int y2, long t) {
        double s = sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2-y1));
        double cos = (x2 - x1) / s;
        double sin = (y2 - y1) / s;
        this.dX= x1+v*cos*(double)(t-createTime)/CONST+a*cos*((double) (t - createTime) /CONST)*((double) (t - createTime) /CONST)/2;
        this.dY=y1+v*sin*(double)(t-createTime)/CONST+a*sin*((double) (t - createTime) /CONST)*((double) (t - createTime) /CONST)/2;
    }


}
