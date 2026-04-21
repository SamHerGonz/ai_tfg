package panelDrawNumber.actionButtons;

import ai.NeuralNetwork;
import panelDrawNumber.DrawingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SendAction implements ActionListener {
    DrawingPanel dp;
    public SendAction(DrawingPanel dp) {
        this.dp = dp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/aiMBSize10.obj"))){
            System.out.println(dp.getAnswer());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
