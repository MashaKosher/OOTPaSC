import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;

public class MyComponent extends JComponent {
    private static final long serialVersionUID = 1L;
    private Font f = new Font("TimesRoman", Font.PLAIN, 42);
    private String str = "replica: Hello world";
    public static  void main(String[] args) {
        JFrame fr=new JFrame();
        //this.str = String.format("%s%n%s", "Ля-ля-ля ля-ля-ля", "Ля-ля-ля ля-ля-ля");
        MyComponent my=new MyComponent();


        fr.add(my);
        fr.setVisible(true);
    }
    public MyComponent() {
    }
    @Override
    public void paint(Graphics g) {
        float x = 10, y = 60;
        Graphics2D g2 = (Graphics2D) g;

        // очищаем фон
        Dimension d = getSize(null);
        g2.setBackground(Color.white);
        g2.clearRect(0, 0, d.width, d.height);

        // получаем контекст отображения шрифта
        FontRenderContext frc = g2.getFontRenderContext();
        // получаем метрики шрифта
        LineMetrics lm = f.getLineMetrics(str, frc);

        // устанавливаем шрифт
        g2.setFont(f);
        // вывод базовой линия шрифта
        double width = f.getStringBounds(str, frc).getWidth();
        g2.setColor(Color.cyan);
        g2.draw(new Line2D.Double(x, y, x + width, y));
        // вывод верхней границы
        g2.setColor(Color.red);
        g2.draw(new Line2D.Double(x, y - lm.getAscent(), x + width, y
                - lm.getAscent()));
        // вывод нижней границы
        g2.setColor(Color.green);
        g2.draw(new Line2D.Double(x, y + lm.getDescent(), x + width, y
                + lm.getDescent()));
        // вывод межстрочного расстояния
        g2.setColor(Color.blue );
        g2.draw(new Line2D.Double(x, y + lm.getDescent()
                + lm.getLeading(), x + width, y + lm.getDescent()
                + lm.getLeading()));
        g2.setColor(Color.black );
        g2.drawString(str, x, y);
    }


}
