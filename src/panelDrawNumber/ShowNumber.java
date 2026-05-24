package panelDrawNumber;

import ai.NeuralNetwork;
import panelDrawNumber.buttons.PanelButton;
import panelDrawNumber.buttons.actions.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ShowNumber {
    public static final int WIDTHDP = 28;
    public static final int HEIGTHDP = 28;

    public static final int POSBUTTON = 720;

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("Tiene que tener un argumento con el archivo de la IA");
        }
        JFrame window = new JFrame("Título");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args[0]));
            NeuralNetwork ai = (NeuralNetwork) ois.readObject();
            ois.close();
            System.out.println(ai);

            DrawingPanel dp = new DrawingPanel(window, WIDTHDP, HEIGTHDP, ai,100,100,0);

            PanelButton moveUp = new PanelButton("^");
            PanelButton moveDown = new PanelButton("v");
            PanelButton moveLeft = new PanelButton("<");
            PanelButton moveRight = new PanelButton(">");

            PanelButton clearButton = new PanelButton("Clear");
            PanelButton sendButton = new PanelButton("Send to ai");

            moveUp.addActionListener(new MovePanel(dp, "up"));
            moveDown.addActionListener(new MovePanel(dp, "down"));
            moveLeft.addActionListener(new MovePanel(dp, "left"));
            moveRight.addActionListener(new MovePanel(dp, "right"));

            clearButton.addActionListener(new ClearAction(dp));
            sendButton.addActionListener(new SendAction(dp));

            moveUp.setBounds(DrawingPanel.TILESIZE * WIDTHDP / 2 + 50, 10, 80, 80);
            moveDown.setBounds(DrawingPanel.TILESIZE * HEIGTHDP / 2 + 50, DrawingPanel.TILESIZE * WIDTHDP + 110, 80, 80);
            moveLeft.setBounds(10, DrawingPanel.TILESIZE * WIDTHDP / 2 + 50, 80, 80);
            moveRight.setBounds(DrawingPanel.TILESIZE * WIDTHDP + 110, DrawingPanel.TILESIZE * HEIGTHDP / 2 + 50, 80, 80);

            clearButton.setBounds(POSBUTTON, 310, 400, 180);
            sendButton.setBounds(POSBUTTON, 40, 400, 250);

            window.setSize(DrawingPanel.TILESIZE * WIDTHDP + 700, DrawingPanel.TILESIZE * WIDTHDP + 250);
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

            dp.startThread();
            dp.requestFocusInWindow();
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
