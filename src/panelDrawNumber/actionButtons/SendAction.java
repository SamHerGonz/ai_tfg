package panelDrawNumber.actionButtons;

import panelDrawNumber.DrawingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendAction implements ActionListener {
    DrawingPanel dp;
    public SendAction(DrawingPanel dp) {
        this.dp = dp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            dp.drawingBoard.centerPosition();
            dp.setAnswer(true);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
