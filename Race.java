import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Race {
    static int width = 1000, height = 800;//задаем размеры формы
    static JFrame frame = new JFrame();//создаем форму
    static int num=-1;//номер победившей машины

    public static class RunCar extends Thread {//класс для запуска движения машины в паралелльном потоке
        CreateCar car;//созданная машина, для которой осуществляем движение
        public RunCar(CreateCar car) {//конструктор
            this.car = car;
        }
        @Override
        public void run() {
            try {//сперва работает таймер задержки старта, 3 секунды (по условиям задачи)
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            while (num==-1) {//пока нет победившей машины
                try {//сперва работает небольшой таймер, чтобы машины не моментально перемещались
                    Thread.sleep(4);
                    car.l.setBounds(car.l.getX()+(int) (Math.random() * 5), car.l.getY(), car.l.getWidth(), car.l.getHeight());//перемещаем машину рандомно не более чем на 5 символов по горизонтали
                    if (car.l.getX()+car.l.getWidth()>=width-10) {//если машина достигла финиша
                        if (num==-1) num=car.num+1;//то записываем номер победившей машины, но в случае, если его еще нет, т.к. у нас много потоков, и этим останавливаем все циклы потоков
                        JLabel rez = new JLabel();//выводим номер победившей машины
                        rez.setText(String.valueOf(num));
                        rez.setFont(new Font("Arial", Font.BOLD, 50));
                        rez.setBounds(width / 2, height / 2, 140, 40);
                        frame.add(rez);
                        rez.repaint();//перерисовываем
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static class CreateCar{//класс для создания машинки
        public int num;//номер машины (здесь от 0, при печати прибавим +1)
        public JLabel l;
        public CreateCar(BufferedImage im, int y, int num) {//конструктор
            this.num=num;
            l = new JLabel(new ImageIcon(im));//создаем объект с картинкой, который будем размещать и двигать
            l.setBounds(10, y, im.getWidth(), im.getHeight());//ставим машину на старт на высоте y
            frame.add(l);//добавляем картинку на форму
        }
    }

    public static void main(String[] args) throws IOException {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//способ выхода из формы
        frame.setTitle("Гонки");//заголовок формы
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//определяем разрешение монитора
        frame.setBounds(dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height + 30);//выставляем размеры окна
        frame.getContentPane().setLayout(null);//выравнивание, чтобы координаты объектов в дальнейшем считались от верхнего левого угла
        frame.getContentPane().setBackground(Color.white);//фон окна
        BufferedImage im = ImageIO.read(new URL("https://naves74.ru/images/png_araba_resimleri-wwwnisanboard_94.png?crc=4145026359"));//скачиваем машинку
        frame.setVisible(true);//делаем форму видимой
        int car = 5; //задаем количество машин в гонке
        for (int i = 0; i < car; i++) //в цикле создаем машины
            new RunCar (new CreateCar(im,height/car*i,i)).start();
    }
}