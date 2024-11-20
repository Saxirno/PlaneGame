package Game0_4;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class GameFrame extends Frame {
    Image bgImg = loadImage("../Image/bg1.jpg");
    Image planeImg = loadImage("../Image/t6.png");
    int x = 275, y = 275;
    boolean left,right,up,down;

    public static void main(String[] args) {
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
        this.setTitle("GameFrame0.4");
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


        g.drawImage(bgImg,0,0,600,600,null);
        g.drawImage(planeImg,x,y,50,50,null);
//        x+=2;

//        System.out.println(("中。。。"));
        if(left)x-=10;
        if(right)x+=10;
        if(up)y-=10;
        if(down)y+=10;
    }


    class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT: left=true;break;
                case KeyEvent.VK_RIGHT: right=true;break;
                case KeyEvent.VK_UP: up=true;break;
                case KeyEvent.VK_DOWN: down=true;break;
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT: left=false;break;
                case KeyEvent.VK_RIGHT: right=false;break;
                case KeyEvent.VK_UP: up=false;break;
                case KeyEvent.VK_DOWN: down=false;break;
            }
        }
    }
}
