import java.awt.*;
import java.util.Random;


public class Circle extends DisplayObject {
    public int radius;
    public int x1, y1;

    double newAng, newV;
    int mass;
    int bufX, bufY;
    boolean r;
    int rr;
    private Random rand = new Random();


    public Circle(int x1, int y1, int radius, int r1, int g1, int b1, int r2, int g2, int b2, int lineSize) {
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
        this.mass = radius*radius;
//        this.mass = 5;
        this.rr=0;
    }

    @Override
    public void draw(Graphics2D g){
        g.setColor(new Color(rDraw,gDraw,bDraw));
        g.fillOval(x1, y1, 2*radius, 2*radius);
        g.setColor(new Color(rFill,gFill,bFill));
        g.fillOval(x1Inner, y1Inner, 2*(radius-lineSize), 2*(radius-lineSize));
    }


    public void rotation(Circle obj,long t){
        if(this.rr ==0) {
            double dx = obj.x - this.x;
            double dy = obj.y - this.y;

            double phiRad = Math.atan2(dy, dx);
          //  System.out.printf("Старые для 1 объекта x: %d, y: %d и угол %f и скорость %f\n", this.x, this.y, this.ang, this.v);
           // System.out.printf("Старые для 2 объекта x: %d, y: %d и угол %f и скорость %f\n", obj.x, obj.y, obj.ang, obj.v);
            double theta1Rad = this.ang * Math.PI / 180;
            double theta2Rad = obj.ang * Math.PI / 180;

            if (phiRad < 0) {
                phiRad += Math.PI;
            }
//            int m1 = this.radius * this.radius;
//            int m2 = obj.radius * obj.radius;
//            ;

            var temp1 = this.v * Math.cos(theta1Rad - phiRad) * 0;
            var temp2 = 2 * obj.mass * obj.v * Math.cos(theta2Rad - phiRad);


            var vx = ((temp1 + temp2) / (this.mass + obj.mass)) * Math.cos(phiRad) + this.v * Math.sin(theta1Rad - phiRad) * Math.cos(phiRad + Math.PI / 2);
            var vy = ((temp1 + temp2) / (this.mass + obj.mass)) * Math.sin(phiRad) + this.v * Math.sin(theta1Rad - phiRad) * Math.sin(phiRad + Math.PI / 2);


            this.newV = Math.sqrt(vx * vx + vy * vy);
            this.newAng = Math.atan2(vy, vx) * 180 / Math.PI;
            if (this.ang < 0) {
                this.ang += 360;
            }

            this.bufX = x;
            this.bufY = y;
            if(dx>0 && dy < 0){
                this.bufX -=2;
                this.bufY +=2;
            } else if (dx > 0 && dy>0) {
                this.bufX -=2;
                this.bufY -=2;
            } else if (dx < 0 && dy<0 ) {
                this.bufX +=2;
                this.bufY +=2;
            }else{
                this.bufX +=2;
                this.bufY -=2;
            }
            this.x0 = this.bufX;
            this.y0 = this.bufY;
            this.createTime = t;
            this.r = true;
            this.rr = 0;

        }
    }



    public void changeVector(long t, int type){
        if(type==1) {
            this.upd(this.x + 2,this.y);
            this.x0 = this.x + 2;
            this.y0 = this.y;
        }
        else if(type==2) {
            this.upd(this.x - 2,this.y);
            this.x0 = this.x -2;
            this.y0 = this.y;
        }
        else if(type==3) {
            this.upd(this.x ,this.y+ 2);
            this.x0 = this.x ;
            this.y0 = this.y+2;
        }
        else if(type==4) {
            this.upd(this.x ,this.y- 2);
            this.x0 = this.x ;
            this.y0 = this.y-2;
        }

        if(type==1||type==2)
            this.ang=this.mirVert(this.ang);
        else
            this.ang=this.mirHoriz(this.ang);
        this.createTime=t;
//        this.v*=-1;


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
