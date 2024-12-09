package Game0_6;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Random;

public class GameFrame extends Frame {
    static Random rand = new Random();
    static redM[] redPok = new redM[20];//红包对象
    Date startTime = new Date();    //游戏起始时刻
    Date endTime;  //游戏结束时刻

    Image bgImg = loadImage("../Image/bg1.jpg");//背景
    airPlane aplane = new airPlane(275,275,50,50,"../Image/plane.png");//飞机
    Image planeImg = loadImage(aplane.file);
    Image[] redImg = new Image[3];//红包

    static GameFrame frame = new GameFrame();//游戏界面
    static GameFrame loginFrame = new GameFrame();//登录界面


    public static void main(String[] args) {
        int redSize=0;
        int redX,redY;
        Money[] shape = new Money[3];
        shape[0] = new rectMoney();
        shape[1] = new OvalMoney();
        shape[2] = new circleMoney();
        for(int i=0;i<20;i++){//初始化红包
            redSize=rand.nextInt(100)+50;
            redY = rand.nextInt(200)-200-redSize;
            redX = rand.nextInt(600-redSize);
            redPok[i] = new redM(redX,redY,redSize,redSize,redSize/10,i%3,shape[i%3]);
        }

        loginFrame.InitialFrame();
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
        this.setLocation(660,240);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        redImg[0]=loadImage("../Image/rect.png");
        redImg[1]=loadImage("../Image/circle.png");
        redImg[2]=loadImage("../Image/oval.png");
        if(!frame.isVisible()){
            loginShow();
        }
        else {
            new paintThread().start();
            addKeyListener(new KeyMonitor());
        }
    }

    private void loginShow() {
        loginFrame.setSize(300,100);
        loginFrame.setLocation(810,490);
        loginFrame.setLayout(new GridLayout(3, 2));
        TextField userTextField = new TextField(20);
        TextField passwordField = new TextField(20);
        passwordField.setEchoChar('*');
        Button loginButton = new Button("登录");//登录按钮
        Label userLabel = new Label("用户名:");
        Label passwordLabel = new Label("密码:");

        loginFrame.add(userLabel);
        loginFrame.add(userTextField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(new Label()); // 占位符
        loginFrame.add(loginButton);

        // 为登录按钮添加事件监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText();
                String password = passwordField.getText();

                // 这里添加登录验证逻辑
                if ("admin".equals(username) && "123456".equals(password)) {
                    // 登录成功，显示主界面并隐藏登录界面
                    loginFrame.setVisible(false);
                    frame.InitialFrame();
                    loginFrame.dispose(); //释放登录界面的资源
                } else {
                    // 登录失败，显示错误消息
                    Dialog alert = new Dialog(frame,"Warning",true);
                    alert.setLayout(new FlowLayout());
                    Label warning = new Label("密码错误！");
                    alert.add(warning);
                    alert.setSize(200,100);
                    alert.setLocation(860,490);
                    alert.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            super.windowClosing(e);
                            alert.dispose();
                        }
                    });
                    alert.setVisible(true);
                }
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
        //计时
        endTime = new Date();
        int time = (int)((endTime.getTime()-startTime.getTime())/1000);
        g.setColor(Color.red);
        Font f = new Font("宋体",Font.BOLD,20);
        g.setFont(f);

        //随机红包

        g.drawImage(bgImg,0,0,600,600,null);
        g.drawImage(planeImg,aplane.x,aplane.y,aplane.w,aplane.h,null);


        for(int i=0;i<20;i++)
        {
            if(redPok[i].collision(aplane.x,aplane.y,aplane.w,aplane.h)){
                aplane.score +=redPok[i].score;
//                System.out.println(aplane.score);
                redPok[i].check(0);
            }
            g.drawImage(redImg[redPok[i].i],redPok[i].x,redPok[i].y,redPok[i].w,redPok[i].h,null);
            redPok[i].y+=redPok[i].redSpeed;
            redPok[i].check(600);
        }
        g.drawString("时间："+time+"秒",450,50);//显示存活时间
        g.drawString("得分："+aplane.score,20,50);//显示存活时间

        if(aplane.left)aplane.x-=10;
        if(aplane.right)aplane.x+=10;
        if(aplane.up)aplane.y-=10;
        if(aplane.down)aplane.y+=10;
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

class airPlane{
    int x,y,w,h;
    String file;
    int score=0;
    boolean left=false,right=false,up=false,down=false;
    public  airPlane(int x1,int y1,int w1,int h1,String file1){
        this.x = x1;
        this.y = y1;
        this.w = w1;
        this.h = h1;
        this.file = file1 ;
    }

}

abstract class Money{
    public abstract String shape();
}

class OvalMoney extends Money{
    @Override
    public String shape() {
        return "../resources/oval.png";
    }
}

class rectMoney extends Money{
    @Override
    public String shape() {
        return "../resources/rect.png";
    }
}

class circleMoney extends Money{
    @Override
    public String shape() {
        return "../resources/circle.png";
    }
}

class redM{
    int x,y,w,h;
    int i;
    int score;
    int redSpeed;
    Random rand = new Random();
    Money shape;
    String file;
    public redM(int x1,int y1,int w1,int h1,int score1,int i1,Money shape1){
        this.x = x1;
        this.y = y1;
        this.w = w1;
        this.h = h1;
        this.redSpeed = w*h/500;
        this.score = score1;
        i = i1;
        this.shape = shape1;
        file = shape.shape();
    }
    public void check(int y2){//超过然后更新
        if(y >= y2){
            w = rand.nextInt(100)+50;
            h = w;
            redSpeed = w*h/500;
            x = rand.nextInt(600-w);
            y = rand.nextInt(200)-200-h;
            score = w/10;
        }
    }

    public boolean collision(int x2,int y2,int w2,int h2 ){
        return x + w >= x2 && y + h >= y2 && x2 + w2 >= x && y2 + h2 >= y;
    }
}