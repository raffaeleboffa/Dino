package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {
    static JFrame frame = new JFrame("Dino");
    static GamePanel gp;

    public static void main(String[] args) {
        frame.setBounds(200, 100, 500, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Main main = new Main();
        frame.addKeyListener(main);

        gp = new GamePanel(frame.getWidth(), frame.getHeight());
        frame.add(gp);

        frame.setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE) if(gp.getCicla() && gp.getJump() == 0) gp.gest_air_time();
        if(keyCode == KeyEvent.VK_ENTER) if(!gp.getCicla()) gp.startGameThread();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
