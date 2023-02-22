import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class DVD {
    public static void main(String[] args) throws IOException {
        int width = 800, height=600;//задаем размеры формы
        JFrame frame = new JFrame();//создаем форму
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//способ выхода из формы
        frame.setTitle("dvd");//заголовок формы
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//определяем разрешение монитора
        frame.setBounds(dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height + 30);//выставляем размеры окна
        frame.getContentPane().setLayout(null);//выравнивание, чтобы координаты объектов в дальнейшем считались от верхнего левого угла
        frame.getContentPane().setBackground(Color.BLACK);//фон окна
        BufferedImage previewImage = ImageIO.read(new URL("http://2.bp.blogspot.com/-ZMXk2o0a5Qc/Ttd0-XLWYlI/AAAAAAAAEas/HZ_pd21X8C0/s200/logo_dvd.JPG"));//скачиваем картинку
        int picWidth = previewImage.getWidth(), picHeight = previewImage.getHeight();//запоминаем ее размер (понадобится, когда будем двигать JLabel)
        JLabel wIcon = new JLabel(new ImageIcon(previewImage));//создаем объект с картинкой, который будем размещать и двигать
        int startX= (int) (Math.random() * (width-picWidth)), startY= (int) (Math.random() * (height-picHeight));//рандомно задаем новые координаты
        wIcon.setBounds(startX, startY, picWidth, picHeight);//устанавливаем начальное положение картинки
        frame.add(wIcon);//добавляем картинку на форму
        frame.setVisible(true);//делаем форму видимой
        while (true) {//делаем бесконечный цикл перемещения картинки
            int newX= (int) (Math.random() * (width-picWidth)), newY= (int) (Math.random() * (height-picHeight));//задаем новую точку
            int [][] path = drawBresenhamLine(startX,startY,newX,newY);//по алгоритму Брезенхэма получаем путь (список координат к новой точке)
            startX=newX;//обновляем точки
            startY=newY;
            for (int i = 5; i <path.length; i+=5) {//идем по массиву точек c нужным шагом
                try {//сперва работает таймер
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                wIcon.setBounds(path[i][0],path[i][1], picWidth, picHeight);//перемещаем картинку в нужную точку
                wIcon.repaint();//перерисовываем картинку
            }
        }
    }

    public static int[][] drawBresenhamLine(int xstart, int ystart, int xend, int yend)//реализация подсчета координат линии между двумя точками в пространстве, по алгоритму Брезенхэма, взято с https://ru.wikibooks.org/wiki/%D0%A0%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8_%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%BE%D0%B2/%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC_%D0%91%D1%80%D0%B5%D0%B7%D0%B5%D0%BD%D1%85%D1%8D%D0%BC%D0%B0#%D0%A0%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F_%D0%BD%D0%B0_Java
    /**
     * xstart, ystart - начало;
     * xend, yend - конец;
     * "g.drawLine (x, y, x, y);" используем в качестве "setPixel (x, y);"
     * Можно писать что-нибудь вроде g.fillRect (x, y, 1, 1);
     */
    {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

        dx = xend - xstart;//проекция на ось икс
        dy = yend - ystart;//проекция на ось игрек

        incx = sign(dx);
        /*
         * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
         * справа налево по иксу, то incx будет равен -1.
         * Это будет использоваться в цикле постороения.
         */
        incy = sign(dy);
        /*
         * Аналогично. Если рисуем отрезок снизу вверх -
         * это будет отрицательный сдвиг для y (иначе - положительный).
         */

        if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
        if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
        //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

        if (dx > dy)
        //определяем наклон отрезка:
        {
            /*
             * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
             * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
             * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
             * по y сдвиг такой отсутствует.
             */
            pdx = incx;    pdy = 0;
            es = dy;   el = dx;
        }
        else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0;   pdy = incy;
            es = dx;   el = dy;//тогда в цикле будем двигаться по y
        }
        x = xstart;
        y = ystart;
        err = el/2;
        int [][] a = new int[el+1][2];//создаем массив для хранения точек
        a[0][0]=x;//заносим первую точку
        a[0][1]=y;
        //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

        for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
        {
            err -= es;
            if (err < 0)
            {
                err += el;
                x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy;//или сместить влево-вправо, если цикл проходит по y
            }
            else
            {
                x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }
            a[t+1][0]=x;//добавляем точку в массив
            a[t+1][1]=y;
        }
        return a;
    }
    // Метод для drawBresenhamLine - этот код "рисует" все 9 видов отрезков. Наклонные (из начала в конец и из конца в начало каждый), вертикальный и горизонтальный - тоже из начала в конец и из конца в начало, и точку.
    private static int sign(int x) {
        return Integer.compare(x, 0);
        //возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
    }


}