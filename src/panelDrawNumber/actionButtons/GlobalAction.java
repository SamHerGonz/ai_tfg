package panelDrawNumber.actionButtons;

import panelDrawNumber.DrawingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GlobalAction implements ActionListener {
    DrawingPanel dp;

    public GlobalAction(DrawingPanel dp) {
        this.dp = dp;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        dp.requestFocusInWindow();
    }
}
