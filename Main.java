import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class Main extends JComponent {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Паучок");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(dim.width/2-width/2,dim.height/2-height/2, width,height);
        frame.setVisible(true);
        Spider spider = new Spider();
        frame.add(spider);

    }
    static int width = 400, height=400;//размеры окна

    static class Spider extends Canvas{
        public void paint(Graphics graphics){
            Graphics2D g2 = (Graphics2D) graphics; //преобразуем Graphics в Graphics2D (для сглаживания)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); //сглаживание
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            try {
                URLConnection openConnection = new URL("https://img-winapps.lisisoft.com/imgmic/6/0/2006-1-spider-exterminator-e1cbd0ff42b8.png").openConnection();
                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                BufferedImage image = ImageIO.read(openConnection.getInputStream());
                int imageWidth = image.getWidth(), imageHeight=image.getHeight();
                int y = 0, x = 0;
                while (true) {//двигаемся по диагонали, с очисткой области через паузу
                    if (x>width||y>height) {//если вышли за пределы окна, то все заново
                        y = 0;
                        x = 0;
                    }
                    g2.drawImage(image, x, y, this);
                    TimeUnit.MILLISECONDS.sleep(30);
                    g2.clearRect(x, y, imageWidth, imageHeight);
                    x+=3;
                    y+=3;
                }
            } catch (Exception ex){
                graphics.drawString("Error",5,5);
            }
        }
    }


}