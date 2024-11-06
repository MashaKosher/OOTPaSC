import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class GameMove extends JFrame implements Runnable {
    public int FPS =60;
    public GameField gameField;
    private JPanel panel;
    private Random rand=new Random();
     Thread gameThread;
    int minX, minY;
    int maxX, maxY;
    int width, height;

    int type;
    long CONST =1000000000;
    public static  void main(String[] args) {

        new GameMove(1440, 818,"Game",50,50,1432,789,10,120,160,4,55,14,225,180,180,215,20,2);
    }

    public GameMove(int w, int h,String title,int x1, int y1, int x2, int y2, int r,int g, int b, int window, int r1, int g1, int b1,int r2, int g2, int b2, int frameSize,int type) {
        setUndecorated(true);

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        size(w,h);
        //size(w,h);
        setLayout(null);
        //getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.RED));
        JPanel p= new JPanel();
        p.setBounds(0,0,w,h);
        p.setBackground(Color.LIGHT_GRAY);
        gameField=new GameField(x1,y1,x2,y2,r1,g1,b1,r2,g2,b2,frameSize);
        width=gameField.x2Inner-gameField.x1Inner;
        height=gameField.y2Inner-gameField.y1Inner;
        minX=gameField.x1Inner-gameField.x1;
        minY=gameField.y1Inner-gameField.y1;
        maxX=minX+(gameField.x2Inner-gameField.x1Inner);
        maxY=minY+(gameField.y2Inner-gameField.y1Inner);
        this.type=type;

        panel=new JPanel() {
            @Override
            public void paintComponent(Graphics gr) {
                Graphics2D graphics=(Graphics2D) gr;
                super.paintComponent(graphics);
                gameField.draw(graphics);

            }
        };

        panel.setBounds(x1,y1,x2-x1,y2-y1);

        getContentPane().add(p);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);

        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        MetalLookAndFeel.setCurrentTheme(new changeTheme(new Color(r,g,b)));

        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        getRootPane().setBorder(BorderFactory.createMatteBorder(window, window, window, window, new Color(r,g,b)));
        setVisible(true);
        startGameThread();

    }

    public void startGameThread(){
        gameThread=new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        createList(type);
        double timePerFrame= CONST /FPS;
        long lastFrame=System.nanoTime();
        long now=System.nanoTime();
        while (gameThread!=null){
            now= System.nanoTime();

            if(now-lastFrame>=timePerFrame){
                update(now);

                panel.repaint();
                lastFrame=now;
            }
        }
    }

    public void update(long t){
        gameField.moveAllFigures(type,t);
        ArrayList<DisplayObject> copy=new ArrayList<>(gameField.objects);
        for(DisplayObject obj:copy){
            if (obj.x<=minX || obj.x>=maxX || obj.y<=minY || obj.y>=maxY) {
                DisplayObject newObj=createFigure(obj.toString());
                gameField.addToMove(newObj,type);
                gameField.objects.removeIf(o->o==obj);


            }
        }


    }

    public DisplayObject createFigure(String cls) {
        int rDraw = rand.nextInt(256);
        int gDraw = rand.nextInt(256);
        int bDraw = rand.nextInt(256);
        int rFill = rand.nextInt(256);
        int gFill = rand.nextInt(256);
        int bFill = rand.nextInt(256);
        int lineSize = rand.nextInt(5);

        if (cls.equals("Square")) {
            int a = rand.nextInt(10, width/5);
            int x1 = rand.nextInt(4+minX, maxX - a-3);
            int y1 = rand.nextInt(4+minY, maxY -a -3);
            Rectangle sq=new Rectangle(x1, y1, a, rDraw, gDraw,bDraw,rFill,gFill,bFill, lineSize);
            return sq;
        }
        if (cls.equals("Rectangle")) {
            int a = rand.nextInt(10, width/5);
            int b=a;
            while(b==a){
                b = rand.nextInt(4, height / 6);
            }
            int x1 = rand.nextInt(4+minX, maxX - a-3);
            int y1 = rand.nextInt(4+minY, maxY -b -3);
            Rectangle rec=new Rectangle(x1, y1, a,b, rDraw, gDraw,bDraw,rFill,gFill,bFill, lineSize);
            return rec;
        }
        if (cls.equals("Circle")) {
            int radius = rand.nextInt(10, Math.min(width, height) / 10);
            int x1 = rand.nextInt(4+minX, maxX - 2 * radius -3);
            int y1 = rand.nextInt(4+minY, maxY - 2 * radius -3);
            Circle cir=new Circle(x1, y1, radius, rDraw, gDraw,bDraw,rFill,gFill,bFill, lineSize);
            return cir;
        }
        if (cls.equals("Line")) {
            int x1 = rand.nextInt(4+minX, maxX-3);
            int y1 = rand.nextInt(4+minY, maxY -3);
            int x2 = rand.nextInt(4+minX, maxX-3);
            int y2 = rand.nextInt(4+minY, maxY -3);
            Line l=new Line(x1, y1, x2,y2, rDraw, gDraw,bDraw ,lineSize*2);
            return l;
        }
        if (cls.equals("Ellipse")) {
            int a = rand.nextInt(10, width/5);
            int b = rand.nextInt(10, height/5);
            int x1 = rand.nextInt(4+minX, maxX - a-3);
            int y1 = rand.nextInt(4+minY, maxY -b -3);
            Ellipse el=new Ellipse(x1, y1, a,b, rDraw, gDraw,bDraw,rFill,gFill,bFill, lineSize);
            return el;
        }

        if (cls.equals("Triangle")) {
            int x1 = rand.nextInt(4+minX, maxX-3);
            int y1 = rand.nextInt(4+minY, maxY-3);
            int x2=-1;
            int y2=-1;
            int x3=-1;
            int y3=-1;
            while(x2<4+minX || y2<4+minY || x2>maxX-3 ||y2>maxY-3){
                x2=x1+rand.nextInt(-width/5, width/5);
                y2=y1+rand.nextInt(-height/5, height/5);
            }
            while(x3<4+minX || y3<4+minY || x3>maxX-3 ||y3>maxY-3){
                x3=x1+rand.nextInt(-width/5, width/5);
                y3=y1+rand.nextInt(-height/5, height/5);
            }
            Triangle tr=new Triangle(x1, y1, x2,y2,x3,y3, rDraw, gDraw,bDraw,rFill,gFill,bFill ,lineSize);
            return tr;
        }
        return null;

    }

    public void createList(int type){
        DisplayObject cir=createFigure("Circle");
        DisplayObject l=createFigure("Line");
        DisplayObject el=createFigure("Ellipse");
        DisplayObject rec=createFigure("Rectangle");
        DisplayObject sq=createFigure("Square");
        DisplayObject tr=createFigure("Triangle");
        gameField.addToMove(cir,type);
        gameField.addToMove(l,type);
        gameField.addToMove(el,type);
        gameField.addToMove(rec,type);
        gameField.addToMove(sq,type);
        gameField.addToMove(tr,type);

    }

    public Dimension size(int w, int h){
        Dimension dim=new Dimension();
        dim.width=w;
        dim.height=h;
        this.setPreferredSize(dim);
        this.pack();
        int realW=this.getContentPane().getWidth();
        int realH=this.getContentPane().getHeight();
        System.out.println(realW+" "+realH);
        dim.width= 2*dim.width-realW;
        dim.height= 2*dim.height-realH;
        this.setPreferredSize(dim);
        System.out.println(dim.width+" "+dim.height);
        System.out.println(this.getContentPane().getSize().getHeight());
        this.pack();
        return dim;
    }


}

class changeTheme extends DefaultMetalTheme {
    public Color c;
    public changeTheme(Color c){
        super();
        this.c=c;
    }
    public ColorUIResource getWindowTitleInactiveBackground() {
        return new ColorUIResource(c);
    }

    public ColorUIResource getWindowTitleBackground() {
        return new ColorUIResource(c);
    }

    public ColorUIResource getPrimaryControlHighlight() {
        return new ColorUIResource(c);
    }

    public ColorUIResource getPrimaryControlDarkShadow() {
        return new ColorUIResource(c);
    }

    public ColorUIResource getPrimaryControl() {
        return new ColorUIResource(c);
    }

    public ColorUIResource getControlHighlight() {
        return new ColorUIResource(c);
    }

    public ColorUIResource getControlDarkShadow() {
        return new ColorUIResource(c);
    }

    public ColorUIResource getControl() {
        return new ColorUIResource(c);
    }
}
