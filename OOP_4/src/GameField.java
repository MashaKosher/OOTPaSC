import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameField extends Rectangle {
    public int x1, y1;
    public int x2, y2;

    public ArrayList<Circle> objects;
    int[][] positions;
    boolean[][]crashState;
    boolean[][]frameCrashState;
    public GameField(int x1, int y1, int x2, int y2, int r1, int g1, int b1, int r2, int g2, int b2, int frameSize){
        super(x1,y1,x2-x1,y2-y1,r1,g1,b1,r2,g2,b2,frameSize);
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        objects=new ArrayList< >();
    }
    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(rDraw,gDraw,bDraw));
        g.fillRect(0,0,x2-x1,y2-y1);
        g.setColor(new Color(rFill,gFill,bFill));
        g.fillRect(x1Inner-x1,y1Inner-y1,x2Inner-x1Inner,y2Inner-y1Inner);
        g.setClip(x1Inner-x1,y1Inner-y1,x2Inner-x1Inner,y2Inner-y1Inner);
        if (!objects.isEmpty()){
            for(DisplayObject obj:objects) {
                obj.draw(g);
            }
        }
    }


    public void moveAllFigures(int type,long now){
        int i = 0;
        for(Circle obj:objects) {
            if (type==1) {
                obj.move(obj.v, obj.x0, obj.y0, obj.ang,now);
                obj.upd((int)(obj.dX),(int)(obj.dY));
            }
            else{
                obj.moveAcc(obj.v, obj.a, obj.x0, obj.y0, obj.ang,now);
                obj.upd((int)(obj.dX),(int)(obj.dY));
            }
            positions[i][0] = obj.x;
            positions[i][1] = obj.y;
            i++;
        }
    }

    public void addToMove(Circle obj,int type){
        Random rand=new Random();
        obj.x0=obj.x;
        obj.y0=obj.y;
        //obj.mas=rand.nextInt(1,20);

        obj.ang=rand.nextDouble(0,361);
       // obj.ang=45;
        obj.v=rand.nextDouble(40, 60);
        if (type==2){
            obj.a=rand.nextDouble(30, 70);
        }
        obj.createTime=System.nanoTime();
        objects.add(obj);


    }

    @Override
    public void upd(int type,int t) {
    }


}
