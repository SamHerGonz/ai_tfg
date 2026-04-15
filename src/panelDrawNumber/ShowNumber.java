package panelDrawNumber;

import panelDrawNumber.actionButtons.ClearAction;
import panelDrawNumber.actionButtons.SendAction;

import javax.swing.*;
import java.awt.*;

public class ShowNumber {
    public static final int WIDTHDP = 28;
    public static final int HEIGTHDP = 28;

    public static void main(String[] args) {
        JFrame window = new JFrame("Título");
        DrawingPanel dp = new DrawingPanel(WIDTHDP, HEIGTHDP);
        MouseClick mouse = new MouseClick(window, dp, new Rectangle(0,0,dp.TILESIZE * WIDTHDP,dp.TILESIZE * WIDTHDP), 2, 50);
        JButton clearButton = new JButton("Clear");
        JButton sendButton = new JButton("Send to ai");

        clearButton.addActionListener(new ClearAction(dp));
        sendButton.addActionListener(new SendAction(dp));
        clearButton.setBounds(800,70,200,80);
        sendButton.setBounds(800,300,200,80);


        window.setSize(dp.TILESIZE * WIDTHDP + 300,dp.TILESIZE * WIDTHDP + 100);
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(dp);

        dp.setLayout(null);
        dp.add(clearButton);
        dp.add(sendButton);
        dp.addMouseListener(mouse);
        dp.addMouseMotionListener(mouse);

        dp.startThread();
    }
}
