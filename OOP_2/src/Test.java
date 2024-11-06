import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

public class Test extends JFrame implements Runnable {
    public GameField gameField;

    final JPanel panel;

    Thread gameThread;
    int minX, minY;

    int maxX, maxY;
    int width, height;
    int type;
    int flag;
    final long CONST = 1000000000;
    final int FPS = 60;

    final long TIME_PER_FRAME = CONST / FPS;

    public static void main(String[] args) {

        new Test(1440, 818, "Game", 50, 50, 1432, 789, 10, 120, 160, 4, 55, 14, 225, 180, 180, 215, 20, 2);

    }

    public Test(int w, int h, String title, int x1, int y1, int x2, int y2, int r, int g, int b, int window, int r1, int g1, int b1, int r2, int g2, int b2, int frameSize, int type) {
        setUndecorated(true);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        flag = 0;
        size(w, h);
        setLayout(null);
        JPanel p = new JPanel();
        p.setBounds(0, 0, w, h);
        p.setBackground(Color.LIGHT_GRAY);

        JButton but = new JButton();
        but.setBounds(30, 30, 20, 20);
        but.setBackground(Color.RED);
        but.addActionListener(event -> flag = flag == 0 ? 1 : 0);
        p.add(but);

        gameField = new GameField(x1, y1, x2, y2, r1, g1, b1, r2, g2, b2, frameSize);
        width = gameField.x2Inner - gameField.x1Inner;
        height = gameField.y2Inner - gameField.y1Inner;
        minX = gameField.x1Inner - gameField.x1;
        minY = gameField.y1Inner - gameField.y1;
        maxX = minX + (gameField.x2Inner - gameField.x1Inner);
        maxY = minY + (gameField.y2Inner - gameField.y1Inner);
        this.type = type;

        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics gr) {
                Graphics2D graphics = (Graphics2D) gr;
                super.paintComponent(graphics);
                gameField.draw(graphics);

            }
        };

        panel.setBounds(x1, y1, x2 - x1, y2 - y1);

        getContentPane().add(p);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);

        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        MetalLookAndFeel.setCurrentTheme(new changeTheme(new Color(r, g, b)));
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        getRootPane().setBorder(BorderFactory.createMatteBorder(window, window, window, window, new Color(r, g, b)));
        setVisible(true);


        startGameThread();

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        DisplayObject cir = new Circle(30, 600, 40, 70, 12, 80, 90, 200, 77, 5);
        addObject(cir);
        DisplayObject cir1 = new Circle(30, 600, 40, 70, 80, 80, 78, 45, 77, 5);
        addObject(cir1);
        DisplayObject cir2 = new Circle(30, 600, 40, 70, 80, 80, 78, 45, 77, 5);
        addObject(cir2);

        long lastFrame = System.nanoTime();
        while (gameThread != null) {
            if (flag == 0) {
                long now = System.nanoTime();

                if (now - lastFrame >= TIME_PER_FRAME) {
                    update(now);
                    panel.repaint();
                    lastFrame = now;
                }
            }
        }
    }

    public void addObject(DisplayObject obj) {
        obj.x0 = obj.x;
        obj.y0 = obj.y;
        obj.v = 41;
        obj.createTime = System.nanoTime();
        gameField.objects.add(obj);
    }

    public void update(long t) {
        moveAll(t);
        for (DisplayObject obj : gameField.objects) {
            if (obj.x <= minX || obj.x >= maxX || obj.y <= minY || obj.y >= maxY) {
                obj.upd(obj.x0, obj.y0);
                obj.createTime = t;
            }
        }
    }

    public void moveAll(long now) {
        DisplayObject o1 = gameField.objects.get(0);
        DisplayObject o2 = gameField.objects.get(1);
        DisplayObject o3 = gameField.objects.get(2);
        o1.move(41, o1.x0, o1.y0, -45, now);
        o2.move(41, o2.x0, o2.y0, 0, now);
        o3.move(0, o3.x0, o3.y0, 0, now);
        o1.upd((int) (o1.dX), (int) (o1.dY));
        o2.upd((int) (o2.dX), (int) (o2.dY));
    }

    public void size(int w, int h) {
        Dimension dim = new Dimension();
        dim.width = w;
        dim.height = h;
        this.setPreferredSize(dim);
        this.pack();
        int realW = this.getContentPane().getWidth();
        int realH = this.getContentPane().getHeight();
        System.out.println(realW + " " + realH);
        dim.width = 2 * dim.width - realW;
        dim.height = 2 * dim.height - realH;
        this.setPreferredSize(dim);
        System.out.println(dim.width + " " + dim.height);
        System.out.println(this.getContentPane().getSize().getHeight());
        this.pack();
    }
}

