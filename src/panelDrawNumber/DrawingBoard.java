package panelDrawNumber;

import java.awt.*;

public class DrawingBoard {
    private int[][] positions;
    private Rectangle board;

    public DrawingBoard(int[][] positions, Rectangle board) {
        setPositions(positions);
        setBoard(board);
    }


    public int[][] getPositions() {
        return positions;
    }

    public void setPositions(int[][] positions) {
        this.positions = positions;
    }

    public Rectangle getBoard() {
        return board;
    }

    public void setBoard(Rectangle board) {
        this.board = board;
    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < getPositions().length; i++) {
            for (int j = 0; j < getPositions()[i].length; j++) {
                g2.setColor(new Color(getPositions()[i][j],getPositions()[i][j],getPositions()[i][j]));
                g2.fillRect(board.x + i * board.width / positions[j].length,board.y + j * board.height / positions.length,board.width / positions[j].length,board.height / positions.length);
            }
        }
    }

    public void draw(int posX, int posY, double brushSize, int brushHardness) {
        int x = (posX - board.x) / (board.width / positions[0].length);
        int y = (posY - board.y) / (board.height / positions.length);
        for (int i = x - (int)(brushSize); i < x + (brushSize - 1); i++) {
            for (int j = y - (int)(brushSize); j < y + (brushSize - 1); j++) {
                int minus = (int) Math.sqrt(Math.pow((x + (brushSize - 1)) - i - brushSize + 1, 2) + Math.pow((y + (brushSize - 1)) - j - brushSize + 1, 2));
                if (i >= 0 && j >= 0 && i < getPositions().length && j < getPositions()[0].length && (brushHardness - (minus * brushSize * 3)) >= 0) {
                    getPositions()[i][j] = Math.min(255, getPositions()[i][j] + (int)(brushHardness - (minus * brushSize * 3)));
                }
            }
        }
    }


    public int[][] move(String direction, int[][] pos) {
        int[][] ret_value = new int[pos.length][];
        switch (direction.toUpperCase()) {
            case "UP":
                for (int i = 0; i < pos.length; i++) {
                    ret_value[i] = new int[pos[i].length];
                    if (pos[i].length - 1 >= 0)
                        System.arraycopy(pos[i], 1, ret_value[i], 0, pos[i].length - 1);
                }
                break;
            case "DOWN":
                for (int i = 0; i < pos.length; i++) {
                    ret_value[i] = new int[pos[i].length];
                    if (pos[i].length - 1 >= 0)
                        System.arraycopy(pos[i], 0, ret_value[i], 1, pos[i].length - 1);
                }
                break;
            case "LEFT":
                if (pos.length - 1 >= 0)
                    System.arraycopy(pos, 1, ret_value, 0, pos.length - 1);
                ret_value[ret_value.length - 1] = new int[pos[pos.length - 1].length];
                break;
            case "RIGHT":
                ret_value[0] = new int[pos[0].length];
                System.arraycopy(pos, 0, ret_value, 1, pos.length - 1);
                break;
        }
        return ret_value;
    }

    public void move(String direction) {
        positions = move(direction, positions);
    }

    public boolean canMove(String direction, int[][] pos) {
        boolean b = true;
        switch (direction.toUpperCase()) {
            case "LEFT":
                for (int i: pos[0]) {
                    if (i != 0) {
                        b = false;
                        break;
                    }
                }
                break;
            case "RIGHT":
                for (int i: pos[pos.length - 1]) {
                    if (i != 0) {
                        b = false;
                        break;
                    }
                }
                break;
            case "UP":
                for (int[] ints : pos) {
                    if (ints[0] != 0) {
                        b = false;
                        break;
                    }
                }
                break;
            case "DOWN":
                for (int[] position : pos) {
                    if (position[pos.length - 1] != 0) {
                        b = false;
                        break;
                    }
                }
                break;
        }
        return b;
    }

    public int[][] centerPosition() {
        int[][] ret_value = positions.clone();

        int counter = 0, posX = 0, posY = 0;
        while(canMove("UP", ret_value) && counter < 28) {
            ret_value = move("UP", ret_value);
            counter++;
        }
        counter = 0;
        while(canMove("LEFT", ret_value) && counter < 28) {
            ret_value = move("LEFT", ret_value);
            counter++;
        }
        counter = 0;
        while(canMove("DOWN", ret_value) && counter < 28) {
            ret_value = move("DOWN", ret_value);
            posY++;
            counter++;
        }
        while(canMove("RIGHT", ret_value) && counter < 28) {
            ret_value = move("RIGHT", ret_value);
            posX++;
            counter++;
        }
        for (int i = 0; i < posX / 2; i++) {
            ret_value = move("LEFT", ret_value);
        }
        for (int i = 0; i < posY / 2; i++) {
            ret_value = move("UP", ret_value);
        }
        return ret_value;
    }
}
