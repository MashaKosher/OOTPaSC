import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuItem extends DisplayObject {
    DisplayObject form;
    int X1, Y1;
    int x1, y1;
    int dx, dy;

    int w, h;
    int bufr, bufg, bufb;

    Text text;
    int frameSize;

    ArrayList<MenuItem> subItems;


    HashMap<String, ItemMethod> methodList;

    ItemMethod methodClicked, methodPressed, method2Pressed;

    Rectangle drawElement;
    boolean isSub, isPressed, isVisiable, isOpen;
    MenuItem parent;

    public MenuItem(String type, int w, int h, int x1, int y1, String font, int textSize, String text, int r1, int g1, int b1, int r2, int g2, int b2, int rt, int gt, int bt, int frSize, boolean autoTransform) {
        super(w / 2, h / 2, r1, g1, b1, r2, g2, b2, frSize);
        this.isSub = false;
        this.x1 = x1;
        this.y1 = y1;

        this.frameSize = frSize;
        this.text = new Text(x1 + frSize, y1 + frSize, w - 2 * frSize, h - 2 * frSize, font, textSize, text, rt, gt, bt, autoTransform);
        this.w = w;
        this.h = h;
        type = type.trim().toLowerCase();
        if (type.equals("rectangle")) {
            form = new  Rectangle(x1, y1, w, h, r1, g1, b1, r2, g2, b2, frSize);
        }
        if (type.equals("ellipse")) {
            form = new Ellipse(x1, y1, w, h, r1, g1, b1, r2, g2, b2, frSize);
        }
        if (type.equals("triangle")) {
            form = new Triangle(x1 + w / 2, y1, x1, y1 + h, x1 + w, y1 + h, r1, g1, b1, r2, g2, b2, frSize);
        }
        if (type.equals("square")) {
            form = new Rectangle(x1, y1, w, r1, g1, b1, r2, g2, b2, frSize);

        }
        if (type.equals("circle")) {
            form = new Circle(x1, y1, w, r1, g1, b1, r2, g2, b2, frSize);
        }
        isOpen = false;
        isVisiable = true;
        isPressed = false;
        isSub = false;
        parent = null;
        methodPressed = null;
        methodClicked = null;
        bufr = r1;
        bufg = g1;
        bufb = b1;
        methodList = new HashMap<String, ItemMethod>();
        subItems = new ArrayList<>();

    }

    public MenuItem(String type, int a, int x1, int y1, String font, int textSize, String text, int r1, int g1, int b1, int r2, int g2, int b2, int rt, int gt, int bt, int frSize, boolean autoTransform) {
        this(type, a, a, x1, y1, font, textSize, text, r1, g1, b1, r2, g2, b2, rt, gt, bt, frSize, autoTransform);
    }

    public MenuItem(MenuItem item) {
        super(item.w / 2, item.h / 2, item.form.rDraw, item.form.gDraw, item.form.bDraw, item.form.rFill, item.form.gFill, item.form.bFill, item.form.lineSize);
        this.isSub = false;
        this.x1 = item.x1;
        this.y1 = item.y1;

        this.dx = item.dx;
        this.dy = item.dy;
        this.frameSize = item.frameSize;
        this.w = item.w;
        this.h = item.h;
        this.text = new Text(x1, y1, w, h, item.text.fontString, item.text.fontSize, item.text.text, item.text.rt, item.text.gt, item.text.bt, item.text.autoTransform);
        bufr = item.bufr;
        bufg = item.bufg;
        bufb = item.bufb;
        String type = item.form.toString().trim().toLowerCase();
        if (type.equals("rectangle")) {
            form = new Rectangle(x1, y1, w, h, item.form.rDraw, item.form.gDraw, item.form.bDraw, item.form.rFill, item.form.gFill, item.form.bFill, item.form.lineSize);
        }
        if (type.equals("ellipse")) {
            form = new Ellipse(x1, y1, w, h, item.form.rDraw, item.form.gDraw, item.form.bDraw, item.form.rFill, item.form.gFill, item.form.bFill, item.form.lineSize);
        }
        if (type.equals("triangle")) {
            form = new Triangle(x1 + w / 2, y1, x1, y1 + h, x1 + w, y1 + h, item.form.rDraw, item.form.gDraw, item.form.bDraw, item.form.rFill, item.form.gFill, item.form.bFill, item.form.lineSize);
        }
        if (type.equals("square")) {
            form = new Rectangle(x1, y1, w, item.form.rDraw, item.form.gDraw, item.form.bDraw, item.form.rFill, item.form.gFill, item.form.bFill, item.form.lineSize);

        }
        if (type.equals("circle")) {
            form = new Circle(x1, y1, w / 2, item.form.rDraw, item.form.gDraw, item.form.bDraw, item.form.rFill, item.form.gFill, item.form.bFill, item.form.lineSize);
        }
        isOpen = false;
        isVisiable = true;
        isPressed = false;
        isSub = false;
        parent = null;
        methodPressed = null;
        methodClicked = null;
        methodList = new HashMap<String, ItemMethod>();
        subItems = new ArrayList<>();

    }

    @Override
    public void draw(Graphics2D g) {
        form.draw(g);
        if (drawElement != null) {
            drawElement.draw(g);
            //       System.out.println("rdr="+drawElement.x1);
        }
        text.draw(g);

    }

    @Override
    public void upd(int x, int y) {
    }

    public void changeSize(int w, int h) {
        this.w = w;
        this.h = h;
        String type = form.toString().trim().toLowerCase();
        if (type.equals("rectangle") || type.equals("square")) {
            form = new Rectangle(x1, y1, w, h, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);
        }
        if (type.equals("ellipse")) {
            form = new Ellipse(x1, y1, w, h, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);
        }
        if (type.equals("triangle")) {
            form = new Triangle(x1 + w / 2, y1, x1, y1 + h, x1 + w, y1 + h, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);
        }
        if (type.equals("circle")) {
            form = new Circle(x1, y1, w, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);
        }
    }

    public void colorOnClick() {
        this.form.rDraw = 0;
        this.form.gDraw = 0;
        this.form.bDraw = 0;
    }

    public void colorOnOpen() {
        this.form.rDraw = 210;
        this.form.gDraw = 201;
        this.form.bDraw = 236;
    }

    public void colorReturn() {
        this.form.rDraw = this.bufr;
        this.form.gDraw = this.bufg;
        this.form.bDraw = this.bufb;

    }

    public void addSub(MenuItem item) {
        if (subItems == null)
            subItems = new ArrayList<MenuItem>();
        subItems.add(item);
        item.isSub = true;
        item.parent = this;
    }

    public void deleteSub(MenuItem item) {
        subItems.removeIf(n -> n == item);
    }

    public void changeLocation(int x, int y) {
        int deltaX = x - this.x1;
        int deltaY = y - this.y1;
        form.upd(form.x + deltaX, form.y + deltaY);
        text.x = x;
        text.y = y;
        this.x1 = x;
        this.y1 = y;
    }

    public void changeLocation(int x, int y, Menu menu, boolean add) {
        int deltaX = x - this.x1;
        int deltaY = y - this.y1;
        form.upd(form.x + deltaX, form.y + deltaY);
        text.x = x;
        text.y = y;
        this.x1 = x;
        this.y1 = y;
        if (!add) {
            if (menu.findlastUp(this) != null) {
                MenuItem last = menu.findlastUp(this);
                this.dx = this.x1 - last.x1;
                this.dy = this.y1 - last.y1;
            }
        } else {
            if (menu.findlastUp(null) != null) {
                MenuItem last = menu.findlastUp(null);
                this.dx = this.x1 - last.x1;
                this.dy = this.y1 - last.y1;
                System.out.println(this.dx + " " + this.dy);
            }

        }


    }

    public void changeFont(String font, int size) {
        text.createFont(font, size);

    }

    public void changeFontColor(int r, int g, int b) {
        this.text.rt = r;
        this.text.gt = g;
        this.text.bt = b;
    }

    public void changeForm(String type, int w, int h, int r1, int g1, int b1, int r2, int g2, int b2, int frSize) {
        this.text.w = w;
        this.text.h = h;
        this.w = w;
        this.h = h;
        this.frameSize = frSize;

        type = type.trim().toLowerCase();
        if (type.equals("rectangle")) {
            form = new Rectangle(x1, y1, w, h, r1, g1, b1, r2, g2, b2, frSize);
        } else if (type.equals("ellipse")) {
            form = new Ellipse(x1, y1, w, h, r1, g1, b1, r2, g2, b2, frSize);
        } else if (type.equals("triangle")) {
            form = new Triangle(x1 + w / 2, y1, x1, y1 + h, x1 + w, y1 + h, r1, g1, b1, r2, g2, b2, frSize);
        } else if (type.equals("square")) {
            form = new Rectangle(x1, y1, w, r1, g1, b1, r2, g2, b2, frSize);

        } else if (type.equals("circle")) {
            form = new Circle(x1, y1, w, r1, g1, b1, r2, g2, b2, frSize);
        } else form = new Rectangle(x1, y1, w, h, r1, g1, b1, r2, g2, b2, frSize);
    }

    public void addDrawing(boolean add, Rectangle o) {
        if (add) {
            this.drawElement = o;
        } else {
            this.drawElement = null;
        }
    }

    public void changeFormType(String type) {
        type = type.trim().toLowerCase();
        DisplayObject change = this.form;
        int min = Math.min(w, h);
        if (type.equals("rectangle")) {
            change = new Rectangle(x1, y1, w, h, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);
        }
        if (type.equals("ellipse")) {

            change = new Ellipse(x1, y1, w, h, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);

        }
        if (type.equals("triangle")) {
            change = new Triangle(x1 + w / 2, y1, x1, y1 + h, x1 + w, y1 + h, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);
        }
        if (type.equals("square")) {

            change = new Rectangle(x1, y1, min, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);
            this.w = min;
            this.h = min;
            text.w = min;
            text.h = min;

        }
        if (type.equals("circle")) {
            change = new Circle(x1, y1, min / 2, form.rDraw, form.gDraw, form.bDraw, form.rFill, form.gFill, form.bFill, form.lineSize);
            this.w = min;
            this.h = min;
            text.w = min;
            text.h = min;
        }
        this.form = change;

    }

    public void changeFillColor(int r, int g, int b) {
        this.form.rFill = r;
        this.form.gFill = g;
        this.form.bFill = b;
    }

    public void changeBordColor(int r, int g, int b) {
        this.form.rDraw = r;
        this.form.gDraw = g;
        this.form.bDraw = b;
    }

    public void changeText(String str) {
        text.text = str;
    }

    public void newAction(Method action, Object o, Object... args) {
        ItemMethod method = new ItemMethod(action, o, args);
        methodList.put(action.getName(), method);
    }

    public void newAction(String name, Method action, Object o, Object... args) {
        ItemMethod method = new ItemMethod(action, o, args);
        methodList.put(name, method);
    }

    public void newActionPressed(Method action, Object o, Object... args) {
        methodPressed = new ItemMethod(action, o, args);
        methodList.put(action.getName(), methodPressed);
    }

    public void newAction2Pressed(Method action, Object o, Object... args) {
        method2Pressed = new ItemMethod(action, o, args);
        methodList.put(action.getName(), method2Pressed);
    }


    public void removeActionPressed() {
        if (methodPressed != null) {
            methodList.remove(methodPressed.m.getName());
            methodPressed = null;
        }
    }

    public void removeAction(String name) {
        methodList.remove(name);
    }

    public void actionPressedDo() throws InvocationTargetException, IllegalAccessException {
        if (methodPressed != null && methodPressed.m != null && methodPressed.o != null) {
            try {
                methodPressed.m.invoke(methodPressed.o, methodPressed.params);
            } catch (InvocationTargetException e) {
                System.out.println(e.getCause());
                // обрабатываем исключение cause
            }

        }
    }

    public void action2PressedDo() throws InvocationTargetException, IllegalAccessException {
        if (method2Pressed != null && method2Pressed.m != null && method2Pressed.o != null) {
            method2Pressed.m.invoke(method2Pressed.o, method2Pressed.params);

        }
    }

    public void actionDo(String name) throws InvocationTargetException, IllegalAccessException {
        ItemMethod method = methodList.get(name);
        if (method != null)
            method.m.invoke(method.o, method.params);

    }

    @Override
    public String toString() {
        return this.text.buftext;

    }

    public class ItemMethod {
        Method m;
        Object o;
        Object[] params;

        ItemMethod(Method m, Object o, Object... params) {

            this.m = m;
            this.o = o;
            this.params = params;
        }
    }
}
