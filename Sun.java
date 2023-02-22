import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Sun {
    static int width = 1000, height = 1000;//задаем размеры формы
    static JFrame frame = new JFrame();//создаем форму

    public static class RunPlanet extends Thread {//класс для запуска движения планеты в паралелльном потоке
        private final int sleep;//время задержки для таймера
        CreatePlanet planet;//созданная планета, для которой осуществляем движение
        public RunPlanet(CreatePlanet planet,int sleep) {//конструктор
            this.planet = planet;
            this.sleep = sleep;
        }
        @Override
        public void run() {
            for (int t = 0; t < 360;) {//идем по кругу
                try {//сперва работает таймер
                    Thread.sleep(sleep);
                    planet.l.setBounds((int) (planet.r*Math.cos(t*Math.PI/180))+planet.dX, (int) (planet.r*Math.sin(t*Math.PI/180))+planet.dY, planet.w, planet.h);//перемещаем картинку в нужную точку, которую вычисляем по формуле параметрических уравнений линий, приводя градусы t к радианам
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                t=t==359?0:t+1;//этой формулой обеспечиваем постоянный цикл и обнуление t при 359
            }
        }
    }

    public static class CreatePlanet{//класс для создания планеты
        private String path;//имя файла с картинкой
        public int w, h;//длина и ширина картинки
        public int r;//радиус от солнца
        public int dX,dY;//константы для смещения картинки (т.к. координаты - это ее верхний левый угол, а не центр)
        public JLabel l;
        public CreatePlanet(String path, int r) throws IOException {//конструктор
            this.path = path;
            this.r=r;
            BufferedImage im = ImageIO.read(new URL(path));
            w = im.getWidth();
            h = im.getHeight();
            l = new JLabel(new ImageIcon(im));//создаем объект с картинкой, который будем размещать и двигать
            if (r==0) l.setBounds(width / 2 - w / 2, height / 2 - h / 2, w, h);//если это Солнце, то выставляем ему начальные координаты
            dX=width/2-w/2;
            dY=height/2-h/2;
            frame.add(l);//добавляем картинку на форму
        }
    }

    public static void main(String[] args) throws IOException {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//способ выхода из формы
        frame.setTitle("Солнечная система");//заголовок формы
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//определяем разрешение монитора
        frame.setBounds(dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height + 30);//выставляем размеры окна
        frame.getContentPane().setLayout(null);//выравнивание, чтобы координаты объектов в дальнейшем считались от верхнего левого угла
        frame.getContentPane().setBackground(Color.black);//фон окна
        new CreatePlanet("https://sun6-20.userapi.com/s/v1/if1/mK3Yvn7KHcUA6szowg6KaH7TvjmXK-5tMNfM1M0fyN0DgEQ0HhcocnHqwMsywPkmK1d98w.jpg?size=300x300&quality=96&crop=0,0,300,300&ava=1",0);//создаем солнце
        frame.setVisible(true);//делаем форму видимой
        String[][] planets = new String[][]{new String []{"https://author.today/content/2021/02/12/w/b8f946bda82c4fbc875ee19451b8252d.png?width=250&height=420&mode=max","140","5"},
                {"https://i.pinimg.com/236x/9b/06/d9/9b06d9ac6b5980da0979c79d55f48b68.jpg","160","10"},
                {"https://85.img.avito.st/user-logo/300x200/14732138685.png","190","15"},
                {"https://webstockreview.net/images250_/clipart-globe-brown-9.png","220","20"},
                {"https://azanov.lt/wp-content/uploads/elementor/thumbs/29348-4-jupiter-photos-ot16hbvzxo47lxugmlqgqw9yts2ig41xw27fawg3p4.png","270","30"},
                {"https://www.vinaybajrangi.com/upload/calculator-img/sadhesati-calculator1.png","340","40"},
                {"https://mir-s3-cdn-cf.behance.net/user/276/a0eec4507635477.60140324c0280.jpg","410","60"}};//заносим в массив названия планет, радиус от солнца и тайм-аут при движении
        //в цикле запускаем планеты
        for (String[] planet : planets)
            new RunPlanet(new CreatePlanet(planet[0], Integer.parseInt(planet[1])), Integer.parseInt(planet[2])).start();
    }
}