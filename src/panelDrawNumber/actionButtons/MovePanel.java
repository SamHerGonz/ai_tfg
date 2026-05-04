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
        switch (direction) {
            case Direction.up:
                dp.drawingBoard.move("UP");
                break;
            case Direction.down:
                dp.drawingBoard.move("DOWN");
                break;
            case Direction.left:
                dp.drawingBoard.move("LEFT");
                break;
            case Direction.right:
                dp.drawingBoard.move("RIGHT");
                break;
        }
    }
}
