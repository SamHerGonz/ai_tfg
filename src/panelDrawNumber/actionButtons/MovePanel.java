package panelDrawNumber.actionButtons;

import panelDrawNumber.DrawingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

enum Direction {
    up,
    down,
    left,
    right
}

public class MovePanel implements ActionListener {
    DrawingPanel dp;
    Direction direction;

    public MovePanel(DrawingPanel dp, String direction) {
        this.dp = dp;
        switch (direction.toUpperCase()) {
            case "UP":
                this.direction = Direction.up;
                break;
            case "DOWN":
                this.direction = Direction.down;
                break;
            case "LEFT":
                this.direction = Direction.left;
                break;
            case "RIGHT":
                this.direction = Direction.right;
                break;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        dp.drawingBoard = move();
    }

    public int[][] move() {
        int[][] ret_value = new int[dp.drawingBoard.length][];
        switch (direction) {
            case Direction.up:
                for (int i = 0; i < dp.drawingBoard.length; i++) {
                    ret_value[i] = new int[dp.drawingBoard[i].length];
                    if (dp.drawingBoard[i].length - 1 >= 0)
                        System.arraycopy(dp.drawingBoard[i], 1, ret_value[i], 0, dp.drawingBoard[i].length - 1);
                }
                break;
            case Direction.down:
                for (int i = 0; i < dp.drawingBoard.length; i++) {
                    ret_value[i] = new int[dp.drawingBoard[i].length];
                    if (dp.drawingBoard[i].length - 1 >= 0)
                        System.arraycopy(dp.drawingBoard[i], 0, ret_value[i], 1, dp.drawingBoard[i].length - 1);
                }
                break;
            case Direction.left:
                if (dp.drawingBoard.length - 1 >= 0)
                    System.arraycopy(dp.drawingBoard, 1, ret_value, 0, dp.drawingBoard.length - 1);
                ret_value[ret_value.length - 1] = new int[dp.drawingBoard[dp.drawingBoard.length - 1].length];
                break;
            case Direction.right:
                ret_value[0] = new int[dp.drawingBoard[0].length];
                System.arraycopy(dp.drawingBoard, 0, ret_value, 1, dp.drawingBoard.length - 1);
                break;
        }
        return ret_value;
    }
}
