package view;

import model.Mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class View extends JPanel implements Runnable, ActionListener {

    private int WIDTH = 850;
    private int HEIGHT = 850;
    private Mouse mouse;
    private ArrayList<Integer> xTrace = new ArrayList<>();
    private ArrayList<Integer> yTrace = new ArrayList<>();
    private Timer ticker = new Timer(10, this);

    public static void main(String[] args) {
        View view = new View();
        SwingUtilities.invokeLater(view);
    }

    public void run() {
        JFrame frame = new JFrame("Cat and Mouse");
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        mouse = new Mouse();
        ticker.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // Background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setColor(Color.BLUE);
        g2.draw(new Ellipse2D.Double(5, 5, WIDTH - 10, HEIGHT - 10));
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;
        g2.drawLine(centerX - 5, centerY, centerX + 5, centerY);
        g2.drawLine(centerX, centerY - 5, centerX, centerY + 5);
        // Mouse
        g2.setColor(Color.BLACK);
        int mouseX = WIDTH / 2 + (int) (mouse.getX() * (WIDTH / 2 - 5));
        int mouseY = HEIGHT / 2 + (int) (mouse.getY() * (HEIGHT / 2 - 5));
        g2.fillOval(mouseX - 4, mouseY - 4, 8, 8);
        xTrace.add(mouseX);
        yTrace.add(mouseY);
        int xStart = xTrace.get(0);
        int yStart = yTrace.get(0);
        for (int i = 2; i < xTrace.size(); i+=2) {
            int xEnd = xTrace.get(i);
            int yEnd = yTrace.get(i);
            g2.drawLine(xStart, yStart, xEnd, yEnd);
            xStart = xEnd;
            yStart = yEnd;
        }
        // Cat
        g2.setColor(Color.RED);
        int catX = WIDTH / 2 + (int) (mouse.getCatX() * (WIDTH / 2 - 5));
        int catY = HEIGHT / 2 + (int) (mouse.getCatY() * (HEIGHT / 2 - 5));
        g2.fillOval(catX - 4, catY - 4, 8, 8);
        // If mouse crosses this line, cat will change direction.
        g2.setColor(Color.GRAY);
        g2.drawLine(catX, catY, WIDTH - catX, HEIGHT - catY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mouse.optimalMove();
        if(mouse.getDistanceToEdge() <= 0.0) {
            ticker.stop();
        }
        repaint();
    }
}
