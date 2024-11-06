import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Menu {
    ArrayList<MenuItem> menuList;
    boolean isVisiable, isPressed;
    MenuItem pressedItem, clickedItem;
    int x1, y1;

    int x2, y2;


    public Menu(int x1, int y1, int x2, int y2) {
        menuList = new ArrayList<>();
        isVisiable = false;
        pressedItem = null;
        clickedItem = null;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

    }

    public void draw(Graphics2D g) {
        if (isVisiable) {
            if (!menuList.isEmpty()) {
                for (MenuItem i : menuList) {
                    if (i.isVisiable)
                        i.draw(g);
                }
            }
        }
    }

    public MenuItem add(String type, int w, int h, int x1, int y1, String font, int textSize, String text, int r1, int g1, int b1, int r2, int g2, int b2, int rt, int gt, int bt, int frSize, boolean autoTransform) {
        MenuItem item = new MenuItem(type, w, h, x1, y1, font, textSize, text, r1, g1, b1, r2, g2, b2, rt, gt, bt, frSize, autoTransform);
        menuList.add(item);
        return item;
    }

    public void add(MenuItem item) {
        menuList.add(item);
    }

    public MenuItem findlastUp(MenuItem item) {
        if (item == null) {
            for (int i = this.menuList.size() - 1; i >= 0; i--) {
                if (!this.menuList.get(i).isSub) {
                    return this.menuList.get(i);
                }
            }
        } else {
            if (this.menuList.indexOf(item) == 0) return null;
            else {
                for (int i = this.menuList.indexOf(item) - 1; i >= 0; i--) {
                    if (!this.menuList.get(i).isSub) {
                        return this.menuList.get(i);
                    }
                }
            }
        }
        return null;
    }

    public MenuItem addCopy() {
        if (!menuList.isEmpty()) {

            MenuItem item = findlastUp(null);
            if (item != null) {

                MenuItem i = new MenuItem(item);

                i.changeBordColor(item.bufr, item.bufg, item.bufb);

                if ((i.x1 + i.dx + i.w) <= this.x2 && (i.x1 + i.dx) >= this.x1 && (i.y1 + i.dy + i.h) <= this.y2 && (i.y1 + i.dy) >= this.y1) {
                    i.changeLocation(i.x1 + i.dx, i.y1 + i.dy, this, true);

                } else {
                    if (i.x1 + i.dx + i.w > this.x2 && (i.y1 + i.dy + i.h) <= this.y2) {
                        if (i.dy == 0) {

                            if (i.y1 + item.h <= this.y2) {
                                i.dx = 0;
                                i.dy = item.h;
                                i.changeLocation(i.x1, i.y1 + i.dy, this, true);
                            } else {
                                int bufx = i.x1 + i.dx + i.w;
                                while (bufx > this.x2) {
                                    i.dx -= 1;
                                    bufx = i.x1 + i.dx + i.w;
                                }
                                i.changeLocation(i.x1 + i.dx, i.y1 + i.dy, this, true);
                            }
                        } else {
                            i.dx = 0;
                            i.changeLocation(i.x1 + i.dx, i.y1 + i.dy, this, true);
                        }
                    } else if (i.x1 + i.dx + i.w <= this.x2 && (i.y1 + i.dy + i.h) > this.y2) {

                        if (i.dx == 0) {

                            if (i.x1 + item.w <= this.x2) {
                                i.dy = 0;
                                i.dx = item.w;
                                i.changeLocation(i.x1 + i.dx, i.y1 + i.dy, this, true);
                            } else {
                                int bufy = i.y1 + i.dy + i.h;
                                while (bufy > this.y2) {
                                    i.dy -= 1;
                                    bufy = i.y1 + i.dy + i.h;
                                }
                                i.changeLocation(i.x1 + i.dx, i.y1 + i.dy, this, true);
                            }
                        } else {
                            i.dy = 0;
                            i.changeLocation(i.x1 + i.dx, i.y1 + i.dy, this, true);
                        }

                    } else {
                        i.changeLocation((this.x2 - this.x1) / 2 - i.w / 2, (this.y2 - this.y1) / 2 - i.h / 2, this, true);
                        i.dx = i.x1 - item.x1;
                        i.dy = i.y1 - item.y1;
                    }
                }
                menuList.add(i);
                return i;
            } else return null;
        } else return null;

    }

    public MenuItem addCopy(MenuItem item) {
        MenuItem i = new MenuItem(item);

        menuList.add(i);
        return i;
    }

    public MenuItem addSubCopy(MenuItem item) {
        MenuItem sub;
        if (item.subItems.isEmpty()) {
            sub = new MenuItem(item);
            sub.dx = 0;
            sub.dy = item.h;

            sub.changeBordColor(item.bufr, item.bufg, item.bufb);
        } else {
            sub = new MenuItem(item.subItems.get(item.subItems.size() - 1));
            sub.dx = item.subItems.get(item.subItems.size() - 1).dx;
            sub.dy = item.subItems.get(item.subItems.size() - 1).dy;
        }
        sub.changeLocation(sub.x1 + sub.dx, sub.y1 + sub.dy);
        sub.isSub = true;
        item.addSub(sub);
        if (!item.isOpen) {
            item.isOpen = true;
            // item.colorOnOpen();
        }
        for (MenuItem i : item.subItems)
            i.isVisiable = true;
        menuList.add(sub);
        return sub;
    }

    public MenuItem addSub(MenuItem item, String type, int w, int h, int x1, int y1, String font, int textSize, String text, int r1, int g1, int b1, int r2, int g2, int b2, int rt, int gt, int bt, int frSize, boolean autoTransform) {
        MenuItem sub = add(type, w, h, x1, y1, font, textSize, text, r1, g1, b1, r2, g2, b2, rt, gt, bt, frSize, autoTransform);
        sub.isSub = true;
        item.addSub(sub);
        item.isOpen = true;
        for (MenuItem i : item.subItems)
            i.isVisiable = true;
        return sub;
    }

    public synchronized void delete2(MenuItem item) {
        if (item.subItems != null && !item.subItems.isEmpty()) {
            Iterator<MenuItem> iter = item.subItems.iterator();
            while (iter.hasNext()) {
                MenuItem i = iter.next();
                delete2(i);
            }
            menuList.removeAll(item.subItems);
        }
    }

    public synchronized void delete(MenuItem item) {
        if (item.isSub) {

            item.parent.subItems.remove(item);
        }
        if (item.subItems != null && !item.subItems.isEmpty()) {
            Iterator<MenuItem> iter = item.subItems.iterator();
            while (iter.hasNext()) {
                MenuItem i = iter.next();
                delete2(i);
            }

            menuList.removeAll(item.subItems);

        }
        menuList.remove(item);
    }


    // добавление/удаление метода MenuItem_____________________________________________________________________________________________________________________________
    public void setPressedAction(MenuItem item, Method action, Object o, Object... args) {
        item.newActionPressed(action, o, args);
    }

    public void set2PressedAction(MenuItem item, Method action, Object o, Object... args) {
        item.newAction2Pressed(action, o, args);
    }


    // вызов метода MenuItem по клику мыши_____________________________________________________________________________________________________________________________


    public synchronized boolean listenerPressed(int x, int y, int count) throws InvocationTargetException, IllegalAccessException {
        boolean result = false;
        if (isVisiable && !menuList.isEmpty()) {
            //ArrayList<MenuItem> copy=menuList;
            ArrayList<MenuItem> copy = new ArrayList<MenuItem>(menuList);
            Iterator<MenuItem> iterator = copy.iterator();
            // for (MenuItem i : menuList) {
            while (iterator.hasNext()) {
                MenuItem i = iterator.next();
                boolean flag = false;
                if (i.form.toString().equals("Rectangle") || i.form.toString().equals("Square")) {
                    Rectangle f;
                    f = (Rectangle) i.form;
                    if (x >= f.x1 && x <= f.x1 + f.w && y >= f.y1 && y <= f.y1 + f.h) {
                        flag = true;

                    }
                } else if (i.form.toString().equals("Circle")) {
                    Circle f;
                    f = (Circle) i.form;
                    int a = (f.x - x) * (f.x - x);
                    int b = (f.y - y) * (f.y - y);
                    int r = (int) Math.sqrt(a + b);
                    if (r <= f.radius) {
                        flag = true;

                    }
                } else if (i.form.toString().equals("Ellipse")) {
                    Ellipse f;
                    f = (Ellipse) i.form;
                    int a = (int) Math.pow((f.x - x) / (f.w / 2), 2);
                    int b = (int) Math.pow((f.y - y) / (f.h / 2), 2);
                    int r = (int) Math.sqrt(a + b);
                    if (r <= 1) {
                        flag = true;
                    }

                } else if (i.form.toString().equals("Triangle")) {
                    Triangle f;
                    f = (Triangle) i.form;
                    int a = (f.x1 - x) * (f.y2 - f.y1) - (f.x2 - f.x1) * (f.y1 - y);
                    int b = (f.x2 - x) * (f.y3 - f.y2) - (f.x3 - f.x2) * (f.y2 - y);
                    int c = (f.x3 - x) * (f.y1 - f.y3) - (f.x1 - f.x3) * (f.y3 - y);
                    if ((a == 0 || b == 0 || c == 0) || (a > 0 && b > 0 && c > 0) || (a < 0 && b < 0 && c < 0)) {
                        flag = true;
                    }
//                    int a = (f.x1 + +200 x) * (f.y2 + f.y1) - (f.x2 + f.x1) * (f.y1 + y);
//                    int b = (f.x2 + x) * (f.y3 + f.y2) - (f.x3 + f.x2) * (f.y2 + y);
//                    int c = (f.x3 + x) * (f.y1 + f.y3) - (f.x1 + f.x3) * (f.y3 + y);
//                    if ((a == 0 || b == 0 || c == 0) || (a > 0 && b > 0 && c > 0) || (a < 0 && b < 0 && c < 0)) {
//                        flag = true;
//                    }
                }
                if (flag && i.isVisiable) {

                    if (i.methodPressed != null && count == 1) {
                        i.actionPressedDo();
                    }
                    if (i.method2Pressed != null && count == 1) {
                        i.action2PressedDo();
                        System.out.println("Кнопка 2 нажата");
                    }
                    result = true;
                    return result;

                } else result = false;
            }
        }
        return result;
    }
//_____________________________________________________________________________________________________________________________
}
