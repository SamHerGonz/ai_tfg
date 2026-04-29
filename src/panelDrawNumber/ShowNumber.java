package panelDrawNumber;

import ai.NeuralNetwork;
import panelDrawNumber.actionButtons.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ShowNumber {
    public static final int WIDTHDP = 28;
    public static final int HEIGTHDP = 28;

    public static void main(String[] args) {
        JFrame window = new JFrame("Título");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("fixedDefAi.obj"))) {
            NeuralNetwork ai = (NeuralNetwork) ois.readObject();
            ai.setLearn(false);

            DrawingPanel dp = new DrawingPanel(window, WIDTHDP, HEIGTHDP, ai,80,80,0);

            JButton moveUp = new JButton("^");
            JButton moveDown = new JButton("v");
            JButton moveLeft = new JButton("<");
            JButton moveRight = new JButton(">");

            JButton clearButton = new JButton("Clear");
            JButton sendButton = new JButton("Send to ai");
            JButton selectNumber = new JButton("Select next number from training data");

            JButton ok = new JButton("OK");
            JButton notOk = new JButton("NO");

            moveUp.addActionListener(new MovePanel(dp, "up"));
            moveDown.addActionListener(new MovePanel(dp, "down"));
            moveLeft.addActionListener(new MovePanel(dp, "left"));
            moveRight.addActionListener(new MovePanel(dp, "right"));

            clearButton.addActionListener(new ClearAction(dp));
            sendButton.addActionListener(new SendAction(dp));
            selectNumber.addActionListener(new SelectNumber(dp));

            ok.addActionListener(new WriteVerificationAction(dp, "V"));
            notOk.addActionListener(new WriteVerificationAction(dp, "X"));

            moveUp.setBounds(442, 0, 80, 80);
            moveDown.setBounds(442, 864, 80, 80);
            moveLeft.setBounds(0, 442, 80, 80);
            moveRight.setBounds(864, 442, 80, 80);

            clearButton.setBounds(900, 510, 200, 80);
            sendButton.setBounds(900, 240, 200, 250);
            selectNumber.setBounds(900, 610, 200, 80);

            ok.setBounds(1150, 610, 80, 80);
            notOk.setBounds(1150, 700, 80, 80);

            window.setSize(dp.TILESIZE * WIDTHDP + 600, dp.TILESIZE * WIDTHDP + 200);
            window.setResizable(false);
            window.setVisible(true);
            window.setTitle("Inteligencia artificial");
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.add(dp);
            window.setLocation(300,0);
            dp.setLayout(null);

            dp.addButton(moveUp);
            dp.addButton(moveDown);
            dp.addButton(moveLeft);
            dp.addButton(moveRight);
            // C
            dp.addButton(clearButton);
            // Enter
            dp.addButton(sendButton);
            // R
            dp.addButton(selectNumber);

            dp.addButton(ok);
            dp.addButton(notOk);

            dp.startThread();
            dp.requestFocusInWindow();
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
