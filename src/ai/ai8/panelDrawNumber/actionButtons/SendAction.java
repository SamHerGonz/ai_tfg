package ai.ai8.panelDrawNumber.actionButtons;

import ai.ai8.panelDrawNumber.DrawingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class SendAction implements ActionListener {
    DrawingPanel dp;
    public SendAction(DrawingPanel dp) {
        this.dp = dp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            dp.sendData();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
