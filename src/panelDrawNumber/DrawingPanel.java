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

    public int[][] drawingBoard;
    public boolean finished;

    public NeuralNetwork ai;

    public int posPanelX;
    public int posPanelY;

    public int expectedAnswer = 0;
    public int answer = 0;
    public int index;

    public DrawingPanel(JFrame window, int width, int height, NeuralNetwork ai, int posPanelX, int posPanelY, int index) {
        this.globalAction = new GlobalAction(this);
        mouse = new MouseClick(this, new Rectangle(posPanelX, posPanelY, TILESIZE * width, TILESIZE * height), 1.5, 100);
        this.window = window;
        addKeyListener(new KeyHandler(this));
        drawingBoard = new int[width][height];
        this.buttonGroup = new ButtonGroup();
        this.ai = ai;
        this.index = index;
        this.posPanelX = posPanelX;
        this.posPanelY = posPanelY;

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void setAnswer(boolean show) throws Exception {
        answer = ai.getAnswer(getDrawingBoard(), 0, 255, show);
    }

    public double[] getDrawingBoard() {
        double[] ret_value;
        int n = 0;
        for (int[] v : this.drawingBoard) {
            n += v.length;
        }
        ret_value = new double[n];

        for (int i = 0; i < this.drawingBoard.length; i++) {
            for (int j = 0; j < this.drawingBoard[i].length; j++) {
                ret_value[i * this.drawingBoard.length + j] = this.drawingBoard[j][i];
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
        for (int i = 0; i < drawingBoard.length; i++) {
            for (int j = 0; j < drawingBoard[i].length; j++) {
                g2.setColor(new Color(drawingBoard[i][j],drawingBoard[i][j],drawingBoard[i][j]));
                g2.fillRect(posPanelX + i * TILESIZE,posPanelY + j * TILESIZE,TILESIZE,TILESIZE);
            }
        }
        // Draw answer expected from the csv
        g2.setFont(new Font("Arial",Font.BOLD,80));
        g2.setColor(new Color(0,0,0));
        g2.drawString("CSV: " + expectedAnswer, 900, 570);

        // Draw answer gave
        g2.setFont(new Font("Arial",Font.BOLD,200));
        g2.drawString(String.valueOf(answer), 950, 870);
    }

    public void draw(int posX, int posY, double brushSize, int brushHardness) {
        int x = (posX - posPanelX) / TILESIZE;
        int y = (posY - posPanelY) / TILESIZE;
        for (int i = x - (int)(brushSize); i <= x + (brushSize - 1); i++) {
            for (int j = y - (int)(brushSize); j <= y + (brushSize - 1); j++) {
                int minus = (int) Math.sqrt(Math.pow((x + (brushSize - 1)) - i - brushSize + 1, 2) + Math.pow((y + (brushSize - 1)) - j - brushSize + 1, 2));
                if (i >= 0 && j >= 0 && i < drawingBoard.length && j < drawingBoard[0].length && (brushHardness - (minus * brushSize * 3)) >= 0) {
                    drawingBoard[i][j] = Math.min(255, drawingBoard[i][j] + (int)(brushHardness - (minus * brushSize * 3)));
                }
            }
        }
    }

    public void addButton(AbstractButton button) {
        add(button);
        button.addActionListener(globalAction);
        buttonGroup.add(button);
    }
}
