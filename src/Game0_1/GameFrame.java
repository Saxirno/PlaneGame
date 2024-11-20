package Game0_1;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends Frame {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        frame.setVisible(true);
        frame.setTitle("GameFrame0.1");
        frame.setSize(600, 600);
        frame.setLocation(500,100);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                 windowClosing(e);
                System.exit(0);
            }
        });
    }
}
