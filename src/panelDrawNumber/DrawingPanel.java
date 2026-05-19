package panelDrawNumber;

import ai.NeuralMath;
import ai.NeuralNetwork;
import panelDrawNumber.buttons.actions.GlobalAction;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class DrawingPanel extends JPanel implements Runnable {
    public JFrame window;
    public MouseClick mouse;
    public ButtonGroup buttonGroup;
    public GlobalAction globalAction;

    public static final int TILESIZE = 18;

    Thread thread;

    public DrawingBoard drawingBoard;
    public boolean finished;

    public NeuralNetwork ai;

    public int expectedAnswer = 0;
    public String answer = "";
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

    public void setAnswer(boolean show) {
        double[] answer = ai.getAnswer(getDrawingBoard(false));
        this.answer = String.valueOf(NeuralMath.getMaxPosition(answer));
        if (show) {
            System.out.println(Arrays.toString(answer).replace(',','\n'));
        }
    }

    public double[] getDrawingBoard(boolean center) {
        int[][] pos = center ? drawingBoard.centerPosition() : this.drawingBoard.getPositions();
        double[] ret_value;
        int n = 0;
        for (int[] v : pos) {
            n += v.length;
        }
        ret_value = new double[n];

        for (int i = 0; i < pos.length; i++) {
            for (int j = 0; j < pos[i].length; j++) {
                ret_value[i * pos.length + j] = pos[j][i];
            }
        }
        for (int i = 0; i < ret_value.length; i++) {
            ret_value[i] /= 255;
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

        Graphics2D g2 = (Graphics2D) g;

        super.paintComponent(g);

        // Dibujar el panel
        drawingBoard.draw(g2);

        // Dibujar la respuesta dada por la IA
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial",Font.BOLD,150));
        g2.drawString(answer, 1050, 700);
    }

    public void addButton(AbstractButton button) {
        add(button);
        button.addActionListener(globalAction);
        buttonGroup.add(button);
    }
}
