package panelDrawNumber.actionButtons;

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
        try {/*
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/ai.obj"));
            NeuralNetwork ai = (NeuralNetwork) ois.readObject();
            ai.setLearn(false);
            System.out.println(ai.getAnswer(dp.getDrawingBoard(), 0, 255));
            ois.close();*/
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
