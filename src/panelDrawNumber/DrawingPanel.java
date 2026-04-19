package panelDrawNumber;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DrawingPanel extends JPanel implements Runnable {
    public final int TILESIZE = 28;
    Thread thread;
    public int[][] drawingBoard;
    public boolean finished;

    public DrawingPanel(int width, int height) {
        drawingBoard = new int[width][height];
    }

    public double[] getDrawingBoard() {
        double[] drawingBoard;
        int n = 0;
        for (int[] v : this.drawingBoard) {
            n += v.length;
        }
        drawingBoard = new double[n];

        for (int i = 0; i < this.drawingBoard.length; i++) {
            for (int j = 0; j < this.drawingBoard[i].length; j++) {
                drawingBoard[i * drawingBoard.length + j] = this.drawingBoard[i][j];
            }
        }
        return drawingBoard;
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
                g2.fillRect(i * TILESIZE,j * TILESIZE,TILESIZE,TILESIZE);
            }
        }
    }

    public void draw(int posX, int posY, int brushSize, int brushHardness) {
        int x = posX / TILESIZE;
        int y = posY / TILESIZE;
        for (int i = x - (brushSize - 1); i <= x + (brushSize - 1); i++) {
            for (int j = y - (brushSize - 1); j <= y + (brushSize - 1); j++) {
                int minus = (int) Math.sqrt(Math.pow((x + (brushSize - 1)) - i - brushSize + 1, 2) + Math.pow((y + (brushSize - 1)) - j - brushSize + 1, 2));
                if (i >= 0 && j >= 0 && i < drawingBoard.length && j < drawingBoard[0].length && (brushHardness - (minus * brushSize * 3)) >= 0) {
                    drawingBoard[i][j] = Math.min(255, drawingBoard[i][j] + (brushHardness - (minus * brushSize * 3)));
                }
            }
        }
    }

    public void sendData() throws IOException, InterruptedException {
        // TODO: Fix
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "out/production/AI", "MainAI");
        Process p = pb.start();
        p.waitFor();
    }
}
