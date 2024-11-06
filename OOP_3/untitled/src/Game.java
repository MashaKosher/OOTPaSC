import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Game extends JFrame implements Runnable {
    public GameField gameField;
    MouseListener mouseListener;
    private JPanel panel;
    Thread gameThread;
    int minX, minY;
    int maxX, maxY;
    int width, height;
    Menu editMenu;
    Menu newMenu;
    MenuItem defoult;
    MenuItem itemAdd;
    MenuItem itemAddSub;
    MenuItem itemDelete;
    MenuItem itemEdit;
    MenuItem itemType;
    MenuItem itemDelta;
    MenuItem itemPosition;
    MenuItem itemSize;
    MenuItem itemFillColor;
    MenuItem itemFrameColor;
    MenuItem itemFrameSize;
    MenuItem itemText;
    MenuItem itemFont;
    MenuItem itemFontSize;
    MenuItem itemFontColor;
    MenuItem itemTypeRec;
    MenuItem itemTypeSquare;
    MenuItem itemTypeCircle;
    MenuItem itemTypeEllipse;
    MenuItem itemTypeTriangle;
    MenuItem itemFontBold;
    MenuItem itemFontItalic;
    MenuItem itemFontPlain;
    MenuItem itemFontSerif;
    MenuItem itemFontSansSerif;
    MenuItem itemFontMonospaced;
    MenuItem itemFontDialog;
    MenuItem itemFontDialogInput;

    MenuItem itemNewChoose;
    Rectangle inputField;
    boolean opensub;
    boolean fisrtTime = true;

    public static void main(String[] args) throws NoSuchMethodException {
        new Game(1440, 818, "GameMenu", 5, 5, 1432, 789, 109, 33, 143, 4, 168, 187, 186, 151, 246, 243, 20);
    }

    public Game(int w, int h, String title, int x1, int y1, int x2, int y2, int r, int g, int b, int window, int r1, int g1, int b1, int r2, int g2, int b2, int frameSize) throws NoSuchMethodException {
        setUndecorated(true);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        size(w, h);
        opensub = false;
        setLayout(null);
        JPanel p = new JPanel();
        p.setBounds(0, 0, w, h);
        p.setBackground(Color.LIGHT_GRAY);
        gameField = new GameField(x1, y1, x2, y2, r1, g1, b1, r2, g2, b2, frameSize);
        inputField = null;
        width = gameField.x2Inner - gameField.x1Inner;
        height = gameField.y2Inner - gameField.y1Inner;
        minX = gameField.x1Inner - gameField.x1;
        minY = gameField.y1Inner - gameField.y1;
        maxX = minX + (gameField.x2Inner - gameField.x1Inner);
        maxY = minY + (gameField.y2Inner - gameField.y1Inner);
        editMenu();
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics gr) {
                Graphics2D graphics = (Graphics2D) gr;
                super.paintComponent(graphics);
                gameField.draw(graphics);
                newMenu.draw(graphics);
                editMenu.draw(graphics);
                if (inputField != null) {
                    inputField.draw(graphics);
                }
            }
        };
        panel.setBounds(x1, y1, x2 - x1, y2 - y1);


        getContentPane().add(p);
        getContentPane().add(panel);
        //panel.setFocusable(true);
        panel.requestFocus();
        panel.setFocusTraversalKeysEnabled(false);
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    if (editMenu.isVisiable) {
                        opensub = false;
                        if (newMenu.pressedItem != null)
                            newMenu.pressedItem.colorReturn();
                        for (MenuItem i : newMenu.menuList) {
                            if (i.isOpen) {
                                for (MenuItem s : i.subItems) {
                                    subHide(s);
                                }
                                i.isOpen = false;
                                i.colorReturn();
                            }

                            i.colorReturn();
                            // if(i.isOpen)
                            //   i.colorOnOpen();
                        }
                        //editMenu.isVisiable = false;
                    } else {
                        for (MenuItem i : newMenu.menuList) {
                            if (i.isOpen)
                                i.colorReturn();
                            if (i == newMenu.pressedItem)
                                i.colorOnClick();
                        }
                    }
                    editMenu.isVisiable = !editMenu.isVisiable;
                }
            }
        });

        mouseListener = new MouseAdapter() {
            /* @Override
             public void mouseClicked(MouseEvent e) {
                 super.mouseClicked(e);
                 int x=e.getX();
                 int y=e.getY();
                 System.out.println("clicked"+x+" "+y);
                 try {
                     editMenu.listenerClicked(x,y);
                 } catch (InvocationTargetException ex) {
                     throw new RuntimeException(ex);
                 } catch (IllegalAccessException ex) {
                     throw new RuntimeException(ex);
                 }
             }*/
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int x = e.getX();
                int y = e.getY();
                int count = e.getClickCount();
                System.out.println("pressed" + x + " " + y + " " + count + " " + editMenu.isVisiable);
                try {
                    boolean resnew = newMenu.listenerPressed(x, y, count);
                    if (resnew)
                        //deleteOn=false;
                        System.out.println("New item pressed-> " + newMenu.pressedItem);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }


                try {
                    boolean res = editMenu.listenerPressed(x, y, count);
                    if (res)
                        //deleteOn=false;
                        System.out.println("MyMenu item pressed-> " + editMenu.pressedItem);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }

            }

        };
        panel.addMouseListener(mouseListener);
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
        while (gameThread != null) {
            panel.repaint();
        }
    }

    public void pressed(MenuItem item) {
        item.changeFillColor(item.form.rFill - 10, item.form.gFill - 10, item.form.bFill - 10);
        if (newMenu.menuList.contains(item)) {
            itemNewChoose = item;
        }
    }

    public void changeEditInfo(int type, MenuItem item) {
        if (type == 0) {
            for (MenuItem i : editMenu.menuList) {
                i.changeText(i.text.buftext);
                panel.repaint();
            }
            itemFillColor.drawElement.rFill = itemFillColor.form.rFill;
            itemFillColor.drawElement.gFill = itemFillColor.form.gFill;
            itemFillColor.drawElement.bFill = itemFillColor.form.bFill;
            itemFrameColor.drawElement.rFill = itemFrameColor.form.rFill;
            itemFrameColor.drawElement.gFill = itemFrameColor.form.gFill;
            itemFrameColor.drawElement.bFill = itemFrameColor.form.bFill;
            itemFontColor.drawElement.rFill = itemFontColor.form.rFill;
            itemFontColor.drawElement.gFill = itemFontColor.form.gFill;
            itemFontColor.drawElement.bFill = itemFontColor.form.bFill;

            itemAddSub.isVisiable = false;
            itemFontSerif.colorReturn();
            itemFontSansSerif.colorReturn();
            itemFontMonospaced.colorReturn();
            itemFontDialog.colorReturn();
            itemFontDialogInput.colorReturn();
            itemFontBold.colorReturn();
            itemFontItalic.colorReturn();
            itemFontPlain.colorReturn();
            if (itemType.isOpen) {
                for (MenuItem i : itemType.subItems) {
                    i.colorReturn();
                }
            }
            if (itemFont.isOpen) {
                for (MenuItem i : itemFont.subItems) {
                    i.colorReturn();
                }
            }
        } else {
            itemType.changeText(itemType.text.text + item.form.toString());
            if(fisrtTime){
                itemDelta.changeText(itemDelta.text.text + (item.dx-200) + "," + item.dy);
                fisrtTime = false;
            }else {
                itemDelta.changeText(itemDelta.text.text + item.dx + "," + item.dy);

            }
            itemPosition.changeText(itemPosition.text.text + (item.x1 - gameField.lineSize) + ", " + (item.y1 - gameField.lineSize));

            itemSize.changeText(itemSize.text.text + item.w + "," + item.h);
            itemFillColor.changeText(itemFillColor.text.text + item.form.rFill + "," + item.form.gFill + ", " + item.form.bFill);
            itemFrameColor.changeText(itemFrameColor.text.text + item.bufr + "," + item.bufg + ", " + item.bufb);
            itemFrameSize.changeText(itemFrameSize.text.text + item.frameSize);
            itemText.changeText(itemText.text.text + item.text.text);
            itemFont.changeText(itemFont.text.text + item.text.fontString);
            itemFontSize.changeText(itemFontSize.text.text + item.text.fontSize);
            itemFontColor.changeText(itemFontColor.text.text + item.text.rt + "," + item.text.gt + "," + item.text.bt);
            if (itemEdit.isOpen)
                itemAddSub.isVisiable = true;
            itemFillColor.drawElement.rFill = item.form.rFill;
            itemFillColor.drawElement.gFill = item.form.gFill;
            itemFillColor.drawElement.bFill = item.form.bFill;
            itemFrameColor.drawElement.rFill = item.bufr;
            itemFrameColor.drawElement.gFill = item.bufg;
            itemFrameColor.drawElement.bFill = item.bufb;
            itemFontColor.drawElement.rFill = item.text.rt;
            itemFontColor.drawElement.gFill = item.text.gt;
            itemFontColor.drawElement.bFill = item.text.bt;
            if (itemType.isOpen) {
                for (MenuItem i : itemType.subItems) {
                    i.colorReturn();
                    if (item.form.toString().equals(i.text.text)) {
                        i.colorOnClick();
                    }
                }
            }
            if (itemFont.isOpen) {
                for (MenuItem i : itemFont.subItems) {
                    i.colorReturn();
                }
                String[] s = item.text.fontString.split(" ");
                if (s[0].trim().toLowerCase().equals("serif"))
                    itemFontSerif.colorOnClick();
                else if (s[0].trim().toLowerCase().equals("sansserif"))
                    itemFontSansSerif.colorOnClick();
                else if (s[0].trim().toLowerCase().equals("monospaced"))
                    itemFontMonospaced.colorOnClick();
                else if (s[0].trim().toLowerCase().equals("dialog"))
                    itemFontDialog.colorOnClick();
                else if (s[0].trim().toLowerCase().equals("dialoginput"))
                    itemFontDialogInput.colorOnClick();

                if (s[1].trim().toLowerCase().equals("plain")) {
                    itemFontPlain.colorOnClick();
                } else if (s[1].trim().toLowerCase().equals("bold")) {
                    itemFontBold.colorOnClick();
                } else if (s[1].trim().toLowerCase().equals("italic")) {
                    itemFontItalic.colorOnClick();
                }
            }
            panel.repaint();
        }
    }


    // методы, привязанные к MenuItem____________________________________________________________________________________

    public void itemActivate(MenuItem item) {
        if (editMenu.isVisiable) {
            if (item.isVisiable) {
                System.out.println("item info:" + item.toString() + "->" + item.subItems);
                changeEditInfo(0, null);
                if (newMenu.pressedItem != item) {

                    if (newMenu.pressedItem != null) {

                        newMenu.pressedItem.colorReturn();
                    }
                    newMenu.pressedItem = item;
                    item.colorOnClick();
                    changeEditInfo(1, item);

                } else {

                    newMenu.pressedItem = null;
                    item.colorReturn();
                }

            }
        } else {
            new Thread(new Runnable() {
                public void run() {
                    item.colorOnClick();
                    panel.repaint();
                    try {
                        Thread.sleep(100);
                        item.colorReturn();
                        panel.repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void subHide(MenuItem item) {
        if (item.isVisiable) {
            item.isVisiable = false;
            if (item.subItems != null && !item.subItems.isEmpty()) {
                item.isOpen = false;
                item.colorReturn();
                for (MenuItem i : item.subItems)
                    subHide(i);
            }
        }
    }

    public void item2Activate(MenuItem item) {
        if (!editMenu.isVisiable) {
            if (item.isVisiable && opensub == false) {
                if (item.subItems != null && !item.subItems.isEmpty()) {
                    if (item.isOpen) {
                        item.colorReturn();
                        for (MenuItem i : item.subItems)
                            subHide(i);
                        item.isOpen = false;
                        opensub = false;
                    } else {
                        item.colorReturn();
                        item.colorOnOpen();
                        for (MenuItem i : item.subItems)
                            i.isVisiable = true;
                        item.isOpen = true;
                        opensub = true;
                    }

                }
            } else if (item.subItems != null && !item.subItems.isEmpty() && item.isVisiable && opensub == true) {

                if (item.subItems != null && !item.subItems.isEmpty()) {
                    if (item.isOpen) {
                        item.colorReturn();
                        for (MenuItem i : item.subItems)
                            subHide(i);
                        item.isOpen = false;
                        opensub = false;
                    } else {
                        for (MenuItem it : newMenu.menuList) {
                            if (it.isOpen) {
                                it.colorReturn();
                                for (MenuItem its : it.subItems)
                                    subHide(its);

                            }
                            it.isOpen = false;
                        }
                        item.colorReturn();
                        item.colorOnOpen();
                        for (MenuItem i : item.subItems)
                            i.isVisiable = true;
                        item.isOpen = true;
                        opensub = true;
                    }

                }
            }
        } else {
            if (item.isVisiable) {
                if (item.subItems != null && !item.subItems.isEmpty()) {
                    if (item.isOpen) {
                        // item.colorReturn();
                        for (MenuItem i : item.subItems)
                            subHide(i);
                        item.isOpen = false;
                    } else {
                        //item.colorReturn();
                        // item.colorOnOpen();
                        for (MenuItem i : item.subItems)
                            i.isVisiable = true;
                        item.isOpen = true;
                    }

                }
            }

        }

    }

    public void itemAddAction() throws NoSuchMethodException {
        //System.out.println(editMenu.pressedItem);
        new Thread(new Runnable() {
            public void run() {
                itemAdd.colorOnClick();
                panel.repaint();
                try {
                    Thread.sleep(100);
                    itemAdd.colorReturn();
                    panel.repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        MenuItem item;
        if (newMenu.menuList.isEmpty())
            item = newMenu.addCopy(defoult);
        else
            item = newMenu.addCopy();
        Class[] types = new Class[1];
        types[0] = MenuItem.class;
        Method m = Game.class.getDeclaredMethod("itemActivate", types);
        newMenu.setPressedAction(item, m, this, item);
        Method m1 = Game.class.getDeclaredMethod("item2Activate", types);
        newMenu.set2PressedAction(item, m1, this, item);
        editMenu.pressedItem = null;
        // itemNewChoose=item;
    }

    public synchronized void itemDeleteAction() throws InterruptedException, NoSuchMethodException {
        new Thread(new Runnable() {
            public void run() {
                itemDelete.colorOnClick();
                panel.repaint();
                try {
                    Thread.sleep(100);
                    itemDelete.colorReturn();
                    panel.repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (newMenu.pressedItem != null) {
            MenuItem item = newMenu.pressedItem;
            newMenu.pressedItem = null;
            MenuItem buf = new MenuItem(item);
            buf.changeBordColor(item.bufr, item.bufg, item.bufb);
            newMenu.delete(item);
            if (newMenu.menuList.isEmpty())
                defoult = new MenuItem(buf);
            changeEditInfo(0, null);
        }
        editMenu.pressedItem = null;
    }

    public void itemTypesAction(MenuItem item) {
        if (newMenu.pressedItem != null && itemType.isOpen) {
            for (MenuItem i : itemType.subItems) {
                i.colorReturn();
            }
            item.colorOnClick();
            newMenu.pressedItem.changeFormType(item.text.text);
            itemType.changeText(itemType.text.buftext + newMenu.pressedItem.form.toString());

            itemSize.changeText(itemSize.text.buftext + newMenu.pressedItem.w + "," + newMenu.pressedItem.h);
        }
    }

    public void itemFontsAction(MenuItem item) {
        if (newMenu.pressedItem != null && itemFont.isOpen) {
            for (MenuItem i : itemFont.subItems) {
                i.colorReturn();
            }
            item.colorOnClick();
            int index = itemFont.subItems.indexOf(item);
            String[] s = newMenu.pressedItem.text.fontString.split(" ");
            if (index < 3) {
                newMenu.pressedItem.text.createFont(s[0].toString() + " " + item.text.text);
                itemFont.changeText(itemFont.text.buftext + s[0].toString() + " " + item.text.text);
            } else {
                newMenu.pressedItem.text.createFont(item.text.text + " " + s[1].toString());
                itemFont.changeText(itemFont.text.buftext + item.text.text + " " + s[1].toString());
            }
            itemType.changeText(itemType.text.buftext + newMenu.pressedItem.form.toString());
        }
    }

    public void itemAddSubAction() throws NoSuchMethodException {
        new Thread(new Runnable() {
            public void run() {
                itemAddSub.colorOnClick();
                panel.repaint();
                try {
                    Thread.sleep(100);
                    itemAddSub.colorReturn();
                    panel.repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (newMenu.pressedItem != null) {
            MenuItem item = newMenu.pressedItem;
            MenuItem sub = newMenu.addSubCopy(item);
            Class[] types = new Class[1];
            types[0] = MenuItem.class;
            Method m = Game.class.getDeclaredMethod("itemActivate", types);
            newMenu.setPressedAction(sub, m, this, sub);
            Method m1 = Game.class.getDeclaredMethod("item2Activate", types);
            newMenu.set2PressedAction(sub, m1, this, sub);
        }
        //editMenu.pressedItem=null;
    }


    public void itemEditAction(MenuItem item) throws NoSuchMethodException {
        if (item.isOpen) {
            item.isOpen = false;
            if (item == itemEdit) {
                if (itemFont.isOpen)
                    itemFont.changeFillColor(215, 206, 241);
                if (itemType.isOpen)
                    itemType.changeFillColor(215, 206, 241);
            }

            item.colorReturn();
            item.changeFillColor(215, 206, 241);
            for (MenuItem i : item.subItems) {
                subHide(i);
            }

            itemAddSub.isVisiable = false;
        } else {
            item.isOpen = true;
            if (item == itemFont && newMenu.pressedItem != null) {
                //if(newMenu.pressedItem.text.fontFamily)
                for (MenuItem i : itemFont.subItems) {

                }
            }
            if (item == itemFont && itemType.isOpen) {
                itemType.isOpen = false;
                itemType.colorReturn();
                itemType.changeFillColor(215, 206, 241);
                for (MenuItem i : itemType.subItems) {
                    i.isVisiable = false;
                    i.colorReturn();
                }
            } else if (item == itemType && itemFont.isOpen) {
                itemFont.isOpen = false;
                itemFont.colorReturn();
                itemFont.changeFillColor(215, 206, 241);
                for (MenuItem i : itemFont.subItems) {
                    i.isVisiable = false;
                    i.colorReturn();
                }
            }
            item.colorOnClick();
            item.changeFillColor(137, 98, 252);
            for (MenuItem i : item.subItems) {
                i.isVisiable = true;
            }
            if (newMenu.pressedItem != null)
                itemAddSub.isVisiable = true;

        }

    }

    public void editMenu() throws NoSuchMethodException {
        // стандартные настройки defoult
        defoult = new MenuItem("Rectangle", 200, 100, gameField.w / 2 - 100, gameField.h / 2 - 50, "Serif Bold", 20, "item", 116, 37, 151, 83, 130, 200, 0, 0, 0, 5, true);
        defoult.dx = 200;
        defoult.dy = 0;
        System.out.println(minX + " " + minY + " " + maxX + " " + maxY);
        // отладочное меню
        editMenu = new Menu(minX, minY, maxX, maxY);
        editMenu.isVisiable = false;
        // игровое меню
        newMenu = new Menu(minX, minY, maxX, maxY);
        newMenu.isVisiable = true;
        int coord = gameField.w - gameField.lineSize - 82;
        int coordy = gameField.h - gameField.lineSize - 62;
        // создание элементов отладочного меню и привязка действий по нажатию на элемент
        itemAdd = editMenu.add("Rectangle", 80, 60, coord, coordy - 120, "Serif Bold", 25, "Add", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemDelete = editMenu.add("Rectangle", 80, 60, coord, coordy - 60, "Serif Bold", 25, "Delete", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemEdit = editMenu.add("Rectangle", 80, 60, coord, coordy, "Serif Bold", 25, "Edit", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemAddSub = editMenu.add("Rectangle", 80, 60, coord - 80, coordy - 120, "Serif Bold", 16, "Add sub", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemAddSub.isVisiable = false;
        itemType = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130, coordy - 60, "Serif Bold", 16, "Type:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemDelta = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 10, coordy, "Serif Bold", 16, "Delta:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemPosition = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 9, coordy, "Serif Bold", 16, "Coordinates:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemSize = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 8, coordy, "Serif Bold", 16, "Size:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFillColor = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 7, coordy, "Serif Bold", 16, "Fill color:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFrameColor = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 6, coordy, "Serif Bold", 16, "Frame color:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFrameSize = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 5, coordy, "Serif Bold", 16, "Frame size:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemText = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 4, coordy, "Serif Bold", 16, "Text:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFont = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 3, coordy, "Serif Bold", 16, "Font:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontSize = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130 * 2, coordy, "Serif Bold", 16, "Font size:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontColor = editMenu.addSub(itemEdit, "Rectangle", 130, 60, coord - 130, coordy, "Serif Bold", 16, "Font color:\n", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemTypeRec = editMenu.addSub(itemType, "Rectangle", 130, 30, coord - 130 * 2, coordy - 30, "Serif Bold", 16, "Rectangle", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, true);
        itemTypeSquare = editMenu.addSub(itemType, "Rectangle", 130, 30, coord - 130 * 3, coordy - 30, "Serif Bold", 16, "Square", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemTypeCircle = editMenu.addSub(itemType, "Rectangle", 130, 30, coord - 130 * 4, coordy - 30, "Serif Bold", 16, "Circle", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemTypeEllipse = editMenu.addSub(itemType, "Rectangle", 130, 30, coord - 130 * 5, coordy - 30, "Serif Bold", 16, "Ellipse", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemTypeTriangle = editMenu.addSub(itemType, "Rectangle", 130, 30, coord - 130 * 6, coordy - 30, "Serif Bold", 16, "Triangle", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontBold = editMenu.addSub(itemFont, "Rectangle", 130, 30, coord - 130 * 2, coordy - 30, "Serif Bold", 16, "Bold", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontItalic = editMenu.addSub(itemFont, "Rectangle", 130, 30, coord - 130 * 3, coordy - 30, "Serif Italic", 16, "Italic", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontPlain = editMenu.addSub(itemFont, "Rectangle", 130, 30, coord - 130 * 4, coordy - 30, "Serif Plain", 16, "Plain", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontSerif = editMenu.addSub(itemFont, "Rectangle", 130, 30, coord - 130 * 5, coordy - 30, "Serif Plain", 16, "Serif", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontSansSerif = editMenu.addSub(itemFont, "Rectangle", 130, 30, coord - 130 * 6, coordy - 30, "SansSerif Plain", 16, "SansSerif", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontMonospaced = editMenu.addSub(itemFont, "Rectangle", 130, 30, coord - 130 * 7, coordy - 30, "Monospaced Plain", 16, "Monospaced", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontDialog = editMenu.addSub(itemFont, "Rectangle", 130, 30, coord - 130 * 8, coordy - 30, "Dialog Plain", 16, "Dialog", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        itemFontDialogInput = editMenu.addSub(itemFont, "Rectangle", 130, 30, coord - 130 * 9, coordy - 30, "DialogInput Plain", 16, "DialogInput", 53, 139, 137, 215, 206, 241, 0, 0, 0, 1, false);
        Rectangle colFill = new Rectangle(itemFillColor.form.x1Inner + 3, itemFillColor.form.y2Inner - 17, 15, 15, itemFillColor.form.rFill, itemFillColor.form.gFill, itemFillColor.form.bFill, itemFillColor.form.rFill, itemFillColor.form.gFill, itemFillColor.form.bFill, 0);
        Rectangle colFrame = new Rectangle(itemFrameColor.form.x1Inner + 3, itemFrameColor.form.y2Inner - 17, 15, 15, itemFrameColor.form.rFill, itemFrameColor.form.gFill, itemFrameColor.form.bFill, itemFrameColor.form.rFill, itemFrameColor.form.gFill, itemFrameColor.form.bFill, 0);
        Rectangle colFont = new Rectangle(itemFontColor.form.x1Inner + 3, itemFontColor.form.y2Inner - 17, 15, 15, itemFontColor.form.rFill, itemFontColor.form.gFill, itemFontColor.form.bFill, itemFontColor.form.rFill, itemFontColor.form.gFill, itemFontColor.form.bFill, 0);
        itemFillColor.addDrawing(true, colFill);
        itemFrameColor.addDrawing(true, colFrame);
        itemFontColor.addDrawing(true, colFont);
        for (MenuItem i : itemType.subItems)
            i.isVisiable = false;
        for (MenuItem i : itemEdit.subItems)
            i.isVisiable = false;
        for (MenuItem i : itemFont.subItems)
            i.isVisiable = false;
        itemType.isOpen = false;
        itemFont.isOpen = false;
        Class[] types = new Class[1];
        types[0] = MenuItem.class;
        Method mAdd = Game.class.getDeclaredMethod("itemAddAction", null);
        Method mDelete = Game.class.getDeclaredMethod("itemDeleteAction", null);
        Method mEdit = Game.class.getDeclaredMethod("itemEditAction", types);
        editMenu.setPressedAction(itemAdd, mAdd, this);
        editMenu.setPressedAction(itemDelete, mDelete, this);
        editMenu.setPressedAction(itemEdit, mEdit, this, itemEdit);
        editMenu.setPressedAction(itemType, mEdit, this, itemType);
        editMenu.setPressedAction(itemFont, mEdit, this, itemFont);
        Method mAddSubItems = Game.class.getDeclaredMethod("itemAddSubAction", null);
        editMenu.setPressedAction(itemAddSub, mAddSubItems, this);

        Method mEditItems = Game.class.getDeclaredMethod("editItemsAction", types);
        editMenu.setPressedAction(itemFillColor, mEditItems, this, itemFillColor);
        editMenu.setPressedAction(itemFontColor, mEditItems, this, itemFontColor);
        editMenu.setPressedAction(itemFrameColor, mEditItems, this, itemFrameColor);
        editMenu.setPressedAction(itemDelta, mEditItems, this, itemDelta);
        editMenu.setPressedAction(itemPosition, mEditItems, this, itemPosition);
        editMenu.setPressedAction(itemSize, mEditItems, this, itemSize);
        editMenu.setPressedAction(itemFontSize, mEditItems, this, itemFontSize);
        editMenu.setPressedAction(itemText, mEditItems, this, itemText);
        editMenu.setPressedAction(itemFrameSize, mEditItems, this, itemFrameSize);
        Method mFontsItems = Game.class.getDeclaredMethod("itemFontsAction", types);
        editMenu.setPressedAction(itemFontBold, mFontsItems, this, itemFontBold);
        editMenu.setPressedAction(itemFontItalic, mFontsItems, this, itemFontItalic);
        editMenu.setPressedAction(itemFontPlain, mFontsItems, this, itemFontPlain);
        editMenu.setPressedAction(itemFontSerif, mFontsItems, this, itemFontSerif);
        editMenu.setPressedAction(itemFontSansSerif, mFontsItems, this, itemFontSansSerif);
        editMenu.setPressedAction(itemFontMonospaced, mFontsItems, this, itemFontMonospaced);
        editMenu.setPressedAction(itemFontDialog, mFontsItems, this, itemFontDialog);
        editMenu.setPressedAction(itemFontDialogInput, mFontsItems, this, itemFontDialogInput);
        Method mTypesItems = Game.class.getDeclaredMethod("itemTypesAction", types);
        editMenu.setPressedAction(itemTypeRec, mTypesItems, this, itemTypeRec);
        editMenu.setPressedAction(itemTypeCircle, mTypesItems, this, itemTypeCircle);
        editMenu.setPressedAction(itemTypeEllipse, mTypesItems, this, itemTypeEllipse);
        editMenu.setPressedAction(itemTypeTriangle, mTypesItems, this, itemTypeTriangle);
        editMenu.setPressedAction(itemTypeSquare, mTypesItems, this, itemTypeSquare);
        itemEdit.isOpen = false;
        editMenu.isVisiable = false;
        //deleteOn=false;
        System.out.println("em size= " + editMenu.menuList.size());
        System.out.println("em list= " + editMenu.menuList);
    }

    public void editItemsAction(MenuItem item) {
        if (newMenu.pressedItem != null && item.isVisiable) {
            InfoReader inforeader = new InfoReader(item);
            inforeader.execute();
        }

    }

    public void editItemsOpenAction(MenuItem item) {
        if (item.isVisiable) {
            boolean vis = item.subItems.get(0).isVisiable;
            if (newMenu.pressedItem != null && !vis) {
                for (MenuItem i : item.subItems)
                    i.isVisiable = true;
            } else if (newMenu.pressedItem != null && vis) {
                for (MenuItem i : item.subItems)
                    i.isVisiable = false;
            }
        }

    }


    public class InfoReader extends SwingWorker<Boolean, String> {
        String textcur;
        String s;
        boolean enter;
        KeyAdapter key;
        MenuItem item;

        BufferedImage buffer;
        boolean color;
        int n, nbuf;
        ArrayList<String> names, values;

        MouseListener ml;
        JPanel colorWheel;

        public InfoReader(MenuItem item) {
            color = false;
            buffer = createWheelBuffer(50);
            s = "";
            n = 0;
            enter = false;
            names = new ArrayList<>();
            values = new ArrayList<>();
            this.item = item;

            if (item == itemSize) {
                n = 2;
                nbuf = 0;
                names.add("w ");
                names.add("h ");
                values.add(String.valueOf(newMenu.pressedItem.w));
                values.add(String.valueOf(newMenu.pressedItem.h));
            } else if (item == itemPosition) {
                n = 2;
                nbuf = 0;
                names.add("x1 ");
                names.add("y2 ");
                values.add(String.valueOf(newMenu.pressedItem.x1 - gameField.lineSize));
                values.add(String.valueOf(newMenu.pressedItem.y1 - gameField.lineSize));
            } else if (item == itemFontSize) {
                n = 1;
                nbuf = 0;
                names.add("Font size ");
                values.add(String.valueOf(newMenu.pressedItem.text.fontSize));
            } else if (item == itemFrameSize) {
                n = 1;
                nbuf = 0;
                names.add("Frame size ");
                values.add(String.valueOf(newMenu.pressedItem.form.lineSize));
            } else if (item == itemDelta) {
                n = 2;
                nbuf = 0;
                names.add("dx ");
                names.add("dy ");
                values.add(String.valueOf(newMenu.pressedItem.dx));
                values.add(String.valueOf(newMenu.pressedItem.dy));
            } else if (item == itemText) {
                n = 1;
                nbuf = 0;
                names.add("Text ");
                values.add(newMenu.pressedItem.text.text);

            } else {
                if (item == itemFillColor) {
                    n = 3;
                    nbuf = 0;
                    names.add("rFill ");
                    names.add("gFiil ");
                    names.add("bFill ");
                    values.add(String.valueOf(newMenu.pressedItem.form.rFill));
                    values.add(String.valueOf(newMenu.pressedItem.form.gFill));
                    values.add(String.valueOf(newMenu.pressedItem.form.bFill));
                } else if (item == itemFrameColor) {
                    n = 3;
                    nbuf = 0;
                    names.add("rFrame ");
                    names.add("gFrame ");
                    names.add("bFrame ");
                    values.add(String.valueOf(newMenu.pressedItem.bufr));
                    values.add(String.valueOf(newMenu.pressedItem.bufg));
                    values.add(String.valueOf(newMenu.pressedItem.bufb));
                } else if (item == itemFontColor) {
                    n = 3;
                    nbuf = 0;
                    names.add("rFont ");
                    names.add("gFont ");
                    names.add("bFont ");
                    values.add(String.valueOf(newMenu.pressedItem.text.rt));
                    values.add(String.valueOf(newMenu.pressedItem.text.gt));
                    values.add(String.valueOf(newMenu.pressedItem.text.bt));
                }
                ml = new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        int x = e.getX();
                        int y = e.getY();
                        int rgb = buffer.getRGB(x, y);
                        int r = (rgb & 0xff0000) >> 16;
                        int g = ((rgb & 0xFF00) >> 8);
                        int b = ((rgb & 0xFF));
                        values.set(0, String.valueOf(r));
                        values.set(1, String.valueOf(g));
                        values.set(2, String.valueOf(b));
                    }
                };

                colorWheel = new JPanel() {
                    @Override
                    public void paintComponent(Graphics gr) {
                        Graphics2D graphics = (Graphics2D) gr;
                        super.paintComponent(graphics);
                        graphics.drawImage(buffer, 0, 0, this);
                        int r = Integer.valueOf(values.get(0));
                        int g = Integer.valueOf(values.get(1));
                        int b = Integer.valueOf(values.get(2));
                        if (r > 255)
                            r = 255;
                        if (g > 255)
                            g = 255;
                        if (b > 255)
                            b = 255;
                        graphics.setColor(new Color(r, g, b));
                        graphics.fillRect(0, 100, 100, 10);
                    }
                };
                colorWheel.setBounds(gameField.x1Inner + 50, gameField.y2Inner - 122 - 80, 100, 110);
                colorWheel.addMouseListener(ml);
                panel.add(colorWheel);
            }
        }


        @Override
        protected Boolean doInBackground() throws Exception {
            inputField = new Rectangle(gameField.x1Inner + 2, itemDelta.y1 - 150, 400, 150, 53, 139, 137, 215, 206, 241, 1) {
                @Override
                public void draw(Graphics2D g) {
                    super.draw(g);
                    int x = this.x - 50;
                    int y = this.y1Inner + 12;
                    for (int i = 0; i < n; i++) {
                        g.setColor(Color.BLACK);
                        g.drawString(names.get(i), x, y);
                        y += 20;
                        g.setColor(Color.WHITE);
                        if (nbuf == i)
                            g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x, y - 10, 100, 20);
                        g.setColor(Color.BLACK);
                        g.drawString(values.get(i), x, y);
                        y += 20;
                    }
                }
            };
            panel.requestFocus();
            panel.setFocusable(true);
            key = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    System.out.println("key " + e.getKeyChar());
                    if (item == itemText) {
                        if (e.getKeyCode() != KeyEvent.CHAR_UNDEFINED && enter == false && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                            String str = values.get(nbuf) + e.getKeyChar();
                            values.set(nbuf, str);
                            panel.repaint();
                            //publish(text);
                        }
                    } else {
                        if ((e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) && enter == false) {
                            String str = values.get(nbuf) + e.getKeyChar();
                            if ((item == itemFillColor || item == itemFontColor || item == itemFrameColor) && Integer.valueOf(str) > 255) {
                                str = "255";
                            }
                            values.set(nbuf, str);
                            panel.repaint();
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && enter == false) {
                        if (values.get(nbuf).length() > 0) {
                            String str = values.get(nbuf).substring(0, values.get(nbuf).length() - 1);
                            values.set(nbuf, str);
                            panel.repaint();
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_MINUS && enter == false && item == itemDelta && values.get(nbuf).length() == 0) {

                        String str = values.get(nbuf) + e.getKeyChar();
                        values.set(nbuf, str);
                        panel.repaint();

                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if ((item == itemFillColor || item == itemFontColor || item == itemFrameColor) && Integer.valueOf(values.get(nbuf)) > 255) {
                            values.set(nbuf, "255");
                        }
                        nbuf++;
                        panel.repaint();
                        if (nbuf == n) {
                            enter = true;
                            System.out.println("enter=true");
                            done();
                        }
                    }
                }
            };
            panel.addKeyListener(key);
            while (true) {
                if (enter) break;
            }
            return true;
        }

        @Override
        public void done() {
            System.out.println("done");
            panel.removeKeyListener(key);
            if (colorWheel != null) {
                colorWheel.removeMouseListener(ml);
                panel.remove(colorWheel);
            }
            if (item == itemSize) {
                newMenu.pressedItem.changeSize(Integer.valueOf(values.get(0)), Integer.valueOf(values.get(1)));
            } else if (item == itemPosition) {
                int newx = Integer.valueOf(values.get(0)) + gameField.lineSize;
                int newy = Integer.valueOf(values.get(1)) + gameField.lineSize;

                if (newMenu.pressedItem.isSub == false) {
                    MenuItem last = newMenu.findlastUp(newMenu.pressedItem);
                    if (last != null) {

                        newMenu.pressedItem.changeLocation(newx, newy, newMenu, false);
                    } else newMenu.pressedItem.changeLocation(newx, newy);
                } else {
                    MenuItem parent = newMenu.pressedItem.parent;
                    int index = parent.subItems.indexOf(newMenu.pressedItem);
                    if (index > 0) {
                        newMenu.pressedItem.dx = newx - parent.subItems.get(index - 1).x1;
                        newMenu.pressedItem.dy = newy - parent.subItems.get(index - 1).y1;
                        newMenu.pressedItem.changeLocation(newx, newy);
                    } else if (index == 0) {
                        newMenu.pressedItem.dx = newx - parent.x1;
                        newMenu.pressedItem.dy = newy - parent.y1;
                        newMenu.pressedItem.changeLocation(newx, newy);
                    } else {
                        MenuItem last = newMenu.findlastUp(newMenu.pressedItem);
                        if (last != null) {

                            //   int itemx1=last.x1+Integer.valueOf(values.get(0));
                            //   int itemy1=last.y1+Integer.valueOf(values.get(1));
                            newMenu.pressedItem.changeLocation(newx, newy, newMenu, false);
                        } else newMenu.pressedItem.changeLocation(newx, newy);
                    }

                }

            } else if (item == itemFontSize) {
                //newMenu.pressedItem.text.fontSize=Integer.valueOf(values.get(0));
                newMenu.pressedItem.text.createFont(Integer.valueOf(values.get(0)));
            } else if (item == itemFrameSize) {
                newMenu.pressedItem.changeForm(newMenu.pressedItem.form.toString(), newMenu.pressedItem.w, newMenu.pressedItem.h, newMenu.pressedItem.form.rDraw, newMenu.pressedItem.form.gDraw, newMenu.pressedItem.form.bDraw, newMenu.pressedItem.form.rFill, newMenu.pressedItem.form.gFill, newMenu.pressedItem.form.bFill, Integer.valueOf(values.get(0)));

            } else if (item == itemDelta) {
                newMenu.pressedItem.dx = Integer.valueOf(values.get(0));
                newMenu.pressedItem.dy = Integer.valueOf(values.get(1));
                if (newMenu.pressedItem.isSub == false) {
                    MenuItem last = newMenu.findlastUp(newMenu.pressedItem);
                    if (last != null) {

//                        int itemx1 = last.x1 + Integer.valueOf(values.get(0));
//                        int itemy1 = last.y1 + Integer.valueOf(values.get(1));
                        int itemx1 = last.x1 + last.w + Integer.valueOf(values.get(0));
                        int itemy1 = last.y1 + Integer.valueOf(values.get(1));
                        newMenu.pressedItem.changeLocation(itemx1, itemy1);
                    }
                } else {
                    MenuItem parent = newMenu.pressedItem.parent;
                    int index = parent.subItems.indexOf(newMenu.pressedItem);
                    if (index > 0) {
                        int itemx1 = parent.subItems.get(index - 1).x1 + Integer.valueOf(values.get(0));
                        int itemy1 = parent.subItems.get(index - 1).y1 + Integer.valueOf(values.get(1));
                        newMenu.pressedItem.changeLocation(itemx1, itemy1);
                    } else if (index == 0) {
                        int itemx1 = parent.x1 + Integer.valueOf(values.get(0));
                        int itemy1 = parent.y1 + Integer.valueOf(values.get(1));
                        newMenu.pressedItem.changeLocation(itemx1, itemy1);
                    } else {
                        MenuItem last = newMenu.findlastUp(newMenu.pressedItem);
                        if (last != null) {

                            int itemx1 = last.x1 + Integer.valueOf(values.get(0));
                            int itemy1 = last.y1 + Integer.valueOf(values.get(1));
                            newMenu.pressedItem.changeLocation(itemx1, itemy1);
                        }
                    }

                }

            } else if (item == itemText) {
                newMenu.pressedItem.text.text = values.get(0);

            } else if (item == itemFillColor) {
                System.out.println("fill item");
                newMenu.pressedItem.form.rFill = Integer.valueOf(values.get(0));
                newMenu.pressedItem.form.gFill = Integer.valueOf(values.get(1));
                newMenu.pressedItem.form.bFill = Integer.valueOf(values.get(2));
                // itemFillColor.text.text=itemFillColor.text.buftext+values.get(0)+","+values.get(1)+","+values.get(2);
            } else if (item == itemFrameColor) {
                newMenu.pressedItem.bufr = Integer.valueOf(values.get(0));
                newMenu.pressedItem.bufg = Integer.valueOf(values.get(1));
                newMenu.pressedItem.bufb = Integer.valueOf(values.get(2));
            } else if (item == itemFontColor) {
                newMenu.pressedItem.text.rt = Integer.valueOf(values.get(0));
                newMenu.pressedItem.text.gt = Integer.valueOf(values.get(1));
                newMenu.pressedItem.text.bt = Integer.valueOf(values.get(2));
            }
            changeEditInfo(0, newMenu.pressedItem);
            changeEditInfo(1, newMenu.pressedItem);
            colorWheel = null;
            inputField = null;
            panel.repaint();


        }

        BufferedImage createWheelBuffer(int radius) {
            final int diameter = radius * 2;
            final double radius2 = radius * radius;
            float hue, sat;
            final double PI2 = 2 * Math.PI;
            double dist2;
            int rgb;
            BufferedImage buffer = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < diameter; x++) {
                for (int y = 0; y < diameter; y++) {
                    dist2 = distance2(x, y, radius, radius);
                    // if the point is not inside the circle we go to the next point
                    if (dist2 > radius2) continue;
                    hue = (float) (Math.atan2(y - radius, x - radius) / PI2);
                    sat = (float) Math.sqrt((float) dist2) / (float) radius;
                    rgb = Color.HSBtoRGB(hue, sat, 1);
                    buffer.setRGB(x, y, rgb);
                }
            }
            return buffer;
        }

        int distance2(int x1, int y1, int x2, int y2) {
            int a = x2 - x1;
            int b = y2 - y1;
            return a * a + b * b;

        }
    }


    //_____________________________________________________________________________________________________________________________
    public Dimension size(int w, int h) {
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
        return dim;
    }
}
