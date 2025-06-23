package Main;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GamePanel extends JPanel implements Runnable {
    int width, height;
    Thread gameThread;
    float points;
    Dino dino = new Dino();
    Vector<Cactus> cactusVector = new Vector<Cactus>();

    boolean cicla = false;

    Rectangle dino_rec;
    Vector<Rectangle> cactus_rec;

    int jump, air_time;
    /* Gestione 'jump'
    0 = corre
    1 = jump sale
    2 = jump scende
     */

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setBackground(Color.WHITE);
        for(int i = 0; i < 5; i++) cactusVector.addElement(new Cactus());
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        cicla = true;

        dino = new Dino();
        cactusVector = new Vector<Cactus>();
        cactus_rec = new Vector<Rectangle>();

        for(int i = 0; i < 5; i++) cactusVector.addElement(new Cactus());

        points = 0;
        jump = 0;
        air_time = 0;

        dino_rec = new Rectangle(dino.getX(), dino.getY(), dino.getWidth(), dino.getHeight());

        for(int i = 0; i < cactusVector.size(); i++) {
            if(i == 0) cactusVector.elementAt(i).setX(width+(int)(Math.random()*300));
            else cactusVector.elementAt(i).setX(cactusVector.elementAt(i-1).getX()+250+(int)(Math.random()*300));

            cactus_rec.addElement(new Rectangle(
                    cactusVector.elementAt(i).getX(),
                    cactusVector.elementAt(i).getY(),
                    cactusVector.elementAt(i).getWidth(),
                    cactusVector.elementAt(i).getHeight()
            ));
        }
        while(cicla) {
            repaint();

            if (jump != 0) {
                air_time++;
                if (air_time > 8) gest_air_time();
                else gest_jump();
                dino_rec.setLocation(dino.getX(), dino.getY());
            }

            for (int i = 0; i < cactusVector.size(); i++) {
                cactusVector.elementAt(i).setX(cactusVector.elementAt(i).getX() - 10);
                cactus_rec.elementAt(i).setLocation(cactusVector.elementAt(i).getX(), cactusVector.elementAt(i).getY());
            }

            points += 0.5;

            for (int i = 0; i < cactusVector.size(); i++) {
                if (cactusVector.elementAt(i).getX() < 0 - cactusVector.elementAt(i).getWidth()) {
                    generaCactus(i, cactusVector);
                    cactus_rec.elementAt(i).setLocation(cactusVector.elementAt(i).getX(), cactusVector.elementAt(i).getY());
                }
            }

            for(int i = 0; i < cactus_rec.size(); i++) if(dino_rec.intersects(cactus_rec.elementAt(i))) cicla = false;

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void generaCactus(int i, Vector<Cactus> cactusVector) {
        if(i == 0) cactusVector.elementAt(i).setX(cactusVector.elementAt(cactusVector.size()-1).getX()+200+(int)(Math.random()*200));
        else cactusVector.elementAt(i).setX(cactusVector.elementAt(i-1).getX()+250+(int)(Math.random()*300));
    }

    public void gest_jump() {
        if(jump == 1)  dino.setY(dino.getY()-9);
        else dino.setY(dino.getY()+9);
    }

    public void gest_air_time() {
        if (jump == 0) {
            jump = 1;
            air_time = 0;
        } else if (jump == 1) {
            jump = 2;
            air_time = 0;
        } else {
            jump = 0;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Serif", Font.PLAIN, 15));
        g2.drawString("Punti: " + (int)points, 50, 50);
        g2.drawLine(0, 300, width, 300);

        if(!cicla) {
            g2.setFont(new Font("Serif", Font.BOLD, 25));
            g2.drawString("Press ENTER to Start", 120, 200);
        }

        dino.draw(g2);
        for(int i = 0; i < cactusVector.size(); i++) cactusVector.elementAt(i).draw(g2);

        g2.dispose();
    }

    public int getJump() {
        return jump;
    }

    public boolean getCicla() {
        return cicla;
    }
}
