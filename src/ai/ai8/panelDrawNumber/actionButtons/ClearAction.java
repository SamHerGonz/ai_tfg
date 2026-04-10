package ai.ai8.panelDrawNumber.actionButtons;

import ai.ai8.panelDrawNumber.DrawingPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ClearAction implements ActionListener {
    DrawingPanel dp;
    public ClearAction(DrawingPanel dp) {
        this.dp = dp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < dp.drawingBoard.length; i++) {
            Arrays.fill(dp.drawingBoard[i], 0);
        }
    }
}
