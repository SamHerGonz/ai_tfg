package panelDrawNumber;

import ai.NeuralNetwork;
import panelDrawNumber.actionButtons.GlobalAction;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel implements Runnable {
    public JFrame window;
    public MouseClick mouse;
    public ButtonGroup buttonGroup;
    public GlobalAction globalAction;

    public final int TILESIZE = 28;

    Thread thread;

    public DrawingBoard drawingBoard;
    public boolean finished;

    public NeuralNetwork ai;

    public int expectedAnswer = 0;
    public int answer = 0;
    public int index;

    public DrawingPanel(JFrame window, int width, int height, NeuralNetwork ai, int posPanelX, int posPanelY, int index) {
        Rectangle r = new Rectangle(posPanelX, posPanelY, TILESIZE * width, TILESIZE * height);
        this.globalAction = new GlobalAction(this);
        drawingBoard = new DrawingBoard(new int[width][height], r);
        mouse = new MouseClick(this, r, 1.5, 120);
        this.window = window;
        addKeyListener(new KeyHandler(this));
        this.buttonGroup = new ButtonGroup();
        this.ai = ai;
        this.index = index;

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void setAnswer(boolean show) throws Exception {
        answer = ai.getAnswer(getDrawingBoard(true), 0, 255, show);
    }

    public float[] getDrawingBoard(boolean center) {
        int[][] pos = center ? drawingBoard.centerPosition() : this.drawingBoard.getPositions();
        float[] ret_value;
        int n = 0;
        for (int[] v : pos) {
            n += v.length;
        }
        ret_value = new float[n];

        for (int i = 0; i < pos.length; i++) {
            for (int j = 0; j < pos[i].length; j++) {
                ret_value[i * pos.length + j] = pos[j][i];
            }
        }


        return ret_value;
    }

    public void startThread() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (thread != null && !finished) {
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        drawingBoard.draw(g2);

        // Draw answer expected from the csv
        g2.setFont(new Font("Arial",Font.BOLD,80));
        g2.setColor(new Color(0,0,0));
        g2.drawString("CSV: " + expectedAnswer, 900, 570);

        // Draw answer gave
        g2.setFont(new Font("Arial",Font.BOLD,200));
        g2.drawString(String.valueOf(answer), 950, 870);
    }

    public void addButton(AbstractButton button) {
        add(button);
        button.addActionListener(globalAction);
        buttonGroup.add(button);
    }
}
