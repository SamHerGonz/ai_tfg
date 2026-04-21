package panelDrawNumber;

import ai.NeuralNetwork;
import panelDrawNumber.actionButtons.ClearAction;
import panelDrawNumber.actionButtons.SelectRandomNumber;
import panelDrawNumber.actionButtons.SendAction;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ShowNumber {
    public static final int WIDTHDP = 28;
    public static final int HEIGTHDP = 28;

    public static void main(String[] args) {
        JFrame window = new JFrame("Título");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/aiMBSize10.obj"))) {
            NeuralNetwork ai = (NeuralNetwork) ois.readObject();
            ai.setLearn(false);

            DrawingPanel dp = new DrawingPanel(WIDTHDP, HEIGTHDP, ai);
            MouseClick mouse = new MouseClick(window, dp, new Rectangle(0, 0, dp.TILESIZE * WIDTHDP, dp.TILESIZE * WIDTHDP), 2, 50);
            JButton clearButton = new JButton("Clear");
            JButton sendButton = new JButton("Send to ai");
            JButton selectRandomNumber = new JButton("Select random number from training data");

            clearButton.addActionListener(new ClearAction(dp));
            sendButton.addActionListener(new SendAction(dp));
            selectRandomNumber.addActionListener(new SelectRandomNumber(dp));
            clearButton.setBounds(800, 70, 200, 80);
            sendButton.setBounds(800, 300, 200, 80);
            selectRandomNumber.setBounds(800, 500, 200, 80);

            window.setSize(dp.TILESIZE * WIDTHDP + 300, dp.TILESIZE * WIDTHDP + 100);
            window.setResizable(false);
            window.setVisible(true);
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.add(dp);

            dp.setLayout(null);
            dp.add(clearButton);
            dp.add(sendButton);
            dp.add(selectRandomNumber);
            dp.addMouseListener(mouse);
            dp.addMouseMotionListener(mouse);

            dp.startThread();
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
