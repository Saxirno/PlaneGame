package Game0_6;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.stream.IntStream;
import java.util.Random;

public class GameFrame extends Frame {
    static Random rand = new Random();
    static redM[] redPok = new redM[20];



    Image bgImg = loadImage("../Image/bg1.jpg");
    airPlane aplane = new airPlane(275,275,50,50,"../Image/plane.png");
    Image planeImg = loadImage(aplane.file);
    Image redImg = loadImage("../Image/img.png");
    //炮弹
    shell aShell = new shell(300,200,10,10);

    public static void main(String[] args) {
        int redSize=0;
        int redX,redY;
        for(int i=0;i<20;i++){
            redSize=rand.nextInt(100)+50;
            redY = rand.nextInt(200)-200-redSize;
            redX = rand.nextInt(600-redSize);
            redPok[i] = new redM(redX,redY,redSize,redSize);
        }
        GameFrame frame = new GameFrame();
        frame.InitialFrame();
    }

    class paintThread extends Thread{
        @Override
        public void run(){
            super.run();
            while(true){
                repaint();
                try{
                    Thread.sleep(40);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Image offScreenImage = null;
    public void update(Graphics g){
        if(offScreenImage == null){
            offScreenImage = this.createImage(600, 600);
        }
        Graphics goff = offScreenImage.getGraphics();
        paint(goff);
        g.drawImage(offScreenImage, 0, 0, null);
    }



    public void InitialFrame() {
        this.setVisible(true);
        this.setTitle("GameFrame0.6");
        this.setSize(600, 600);
        this.setLocation(500,100);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        new paintThread().start();
        addKeyListener(new KeyMonitor());
        aShell.initDegree();
    }

    public Image loadImage(String imagePath){
        URL imageUrl = getClass().getResource(imagePath);
        if(imageUrl != null){
            try {
                return ImageIO.read(imageUrl);
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
        System.err.println("Could not load image " + imagePath);
        return null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //随机红包



        g.drawImage(bgImg,0,0,600,600,null);
        g.drawImage(planeImg,aplane.x,aplane.y,aplane.w,aplane.h,null);
        for(int i=0;i<20;i++)
        {
        g.drawImage(redImg,redPok[i].x,redPok[i].y,redPok[i].w,redPok[i].h,null);
        redPok[i].y+=redPok[i].redSpeed;
        redPok[i].check(600);
        }

        if(aplane.left)aplane.x-=10;
        if(aplane.right)aplane.x+=10;
        if(aplane.up)aplane.y-=10;
        if(aplane.down)aplane.y+=10;


        Color a = g.getColor();
        g.setColor(Color.red);
        g.fillOval(aShell.x,aShell.y,aShell.w,aShell.h);
        aShell.x += 5*Math.cos(aShell.degree);
        aShell.y += 5*Math.sin(aShell.degree);
        g.setColor(a);

        Color c2 = g.getColor();
        g.setColor(Color.red);
        IntStream.range(0,50).forEach(i -> {
            g.fillOval(aShell.x_array[i],aShell.y_array[i],aShell.w,aShell.h);
            aShell.x_array[i] += 7*Math.cos(aShell.degree_array[i]);
            aShell.y_array[i] += 7*Math.sin(aShell.degree_array[i]);
            if(aShell.x_array[i]>600-10||aShell.x_array[i]<10){
                aShell.degree_array[i] = Math.PI - aShell.degree_array[i];
            }
            if(aShell.y_array[i]>600-10||aShell.y_array[i]<10){
                aShell.degree_array[i] = - aShell.degree_array[i];
            }
        });
        g.setColor(c2);
    }


    class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT: aplane.left=true;break;
                case KeyEvent.VK_RIGHT: aplane.right=true;break;
                case KeyEvent.VK_UP: aplane.up=true;break;
                case KeyEvent.VK_DOWN: aplane.down=true;break;
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT: aplane.left=false;break;
                case KeyEvent.VK_RIGHT: aplane.right=false;break;
                case KeyEvent.VK_UP: aplane.up=false;break;
                case KeyEvent.VK_DOWN:aplane.down=false;break;
            }
        }
    }
}

class shell{
    int x=300,y=300,w=10,h=10;
    int[] x_array = new int[50],y_array= new int[50];
    double[] degree_array= new double[50];
    double degree = Math.random()*Math.PI*2;
    public shell(int x1,int y1,int w1,int h1){
        this.x = x1;
        this.y = y1;
        this.w = w1;
        this.h = h1;
    }

    public void initDegree(){
        for(int i=0;i<50;i++){
            x_array[i]=100;
            y_array[i]=100;
            degree_array[i]=Math.random()*Math.PI*2;
        }
    }
}

class airPlane{
    int x,y,w,h;
    String file;
    boolean left=false,right=false,up=false,down=false;
    public  airPlane(int x1,int y1,int w1,int h1,String file1){
        this.x = x1;
        this.y = y1;
        this.w = w1;
        this.h = h1;
        this.file = file1 ;
    }

}

class redM{
    int x,y,w,h;
    int redSpeed;
    Random rand = new Random();
    public redM(int x1,int y1,int w1,int h1){
        this.x = x1;
        this.y = y1;
        this.w = w1;
        this.h = h1;
        this.redSpeed = w*h/500;
    }
    public void check(int y2){
        if(y >= y2){
            w = rand.nextInt(100)+50;
            h = w;
            redSpeed = w*h/500;
            x = rand.nextInt(600-w);
            y = rand.nextInt(200)-200-h;
        }
    }
}
