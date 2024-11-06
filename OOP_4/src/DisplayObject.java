import java.awt.*;

import static java.lang.Math.*;

public abstract class DisplayObject {
    // прямоугольная рамка
    public int x1Frame, y1Frame;
    public int x2Frame, y2Frame;

    // внутренняя прямоугольная рамка фигуры
    public int x1Inner, y1Inner;
    public int x2Inner, y2Inner;

    //точка привязки
    public int x, y;

    //цвет оконтовки фигуры
    public int rDraw, gDraw, bDraw;

    //цвет заливки фигуры
    public int rFill, gFill, bFill;

    //толщина линии
    public int lineSize;

    // начальная координата движения
    public int x0, y0;

    // координта направления движения фигуры
    public int xf, yf;

    // начальная скорость
    public double v;

    // ускорение
    public double a;

    // координата положения фигуры в double
    public double dX, dY;

    // время создания
    public long createTime;
    //угол
    double ang;
    long CONST =1000000000;

    public DisplayObject(int x, int y, int r1, int g1, int b1, int r2, int g2, int b2, int lineSize) {
        this.x=x;
        this.y=y;
        rDraw = r1;
        gDraw = g1;
        bDraw = b1;
        rFill = r2;
        gFill = g2;
        bFill = b2;
        this.lineSize=lineSize;
    }

    public abstract void draw(Graphics2D g);

    public abstract void upd(int x, int y);


    // нахождение координаты фигуры в момент времени в double v=const
    public void move(double v,int x1,int y1, int x2, int y2,long t){

        double cos=(x2-x1)/sqrt(pow((x2-x1),2)+pow((y2-y1),2));
        double sin=(y2-y1)/sqrt(pow((x2-x1),2)+pow((y2-y1),2));
        this.dX= x1+v*cos*(t-createTime)/ CONST;
        this.dY=y1+v*sin*(t-createTime)/ CONST;

    }
    // нахождение координаты фигуры в момент времени в double c ускорением
    public void moveAcc(double v, double a,  int x1,int y1, int x2, int y2,long t){
        double cos=(x2-x1)/sqrt(pow((x2-x1),2)+pow((y2-y1),2));
        double sin=(y2-y1)/sqrt(pow((x2-x1),2)+pow((y2-y1),2));
        this.dX= x1+v*cos*(double)(t-createTime)/ CONST +a*cos*pow((double) (t - createTime) / CONST,2)/2;
        this.dY=y1+v*sin*(double)(t-createTime)/ CONST +a*sin*pow((double) (t - createTime) / CONST,2)/2;
    }
    public void move(double v,int x1,int y1, double ang,long t){
        this.dX= x1+v*Math.cos(ang*Math.PI/180)*(t-createTime)/ CONST;
        this.dY=y1+v*Math.sin(ang*Math.PI/180)*(t-createTime)/ CONST;

    }

    public double mirVert(double ang) {
        if (ang <= 180)
            return 180 - ang;
        else {
            var tempAng = ang - 180;
            return (180 - tempAng) + 180;

        }
    }    public double mirHoriz(double ang) {
        if (ang <= 180)
            return 360 - ang;
        else {
            var tempAng = ang - 180;
            return (360 - tempAng) - 180;

        }
    }


    public void moveAcc(double v, double a,  int x1,int y1,double ang,long t){

        this.dX= x1+v*Math.cos(ang*Math.PI/180)*(t-createTime)/ CONST +a*Math.cos(ang*Math.PI/180)*pow((double) (t - createTime) / CONST,2)/2;
        this.dY=y1+v*Math.sin(ang*Math.PI/180)*(t-createTime)/ CONST +a*sin(ang*Math.PI/180)*pow((double) (t - createTime) / CONST,2)/2;
    }



}
