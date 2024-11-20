package Game0_3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class GameFrame extends Frame {
    Image bgImg = loadImage("../Image/bg1.jpg");
    Image planeImg = loadImage("../Image/t6.png");

    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        frame.InitialFrame();

//        Image bgImg =frame.loadImage("../Image/bg1.jpg");
//        Image planeImg = frame.loadImage("../Image/t6.png");
//        Graphics g = frame.getGraphics();
//        g.drawImage(bgImg,0,0,600,600,null);
//        g.drawImage(planeImg,275,275,50,50,null);

    }

    public void InitialFrame() {
        this.setVisible(true);
        this.setTitle("GameFrame0.3");
        this.setSize(600, 600);
        this.setLocation(500,100);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
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
        g.drawImage(planeImg,275,275,50,50,null);
    }
}
