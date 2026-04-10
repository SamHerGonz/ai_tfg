package panelDrawNumber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseClick implements MouseMotionListener, MouseListener {
    JFrame window;
    DrawingPanel dp;
    Rectangle board;
    int brushSize;
    int brushHardness;
    boolean paint = false;

    public MouseClick(JFrame window, DrawingPanel dp, Rectangle board, int brushSize, int brushHardness) {
        this.window = window;
        this.dp = dp;
        this.board = board;
        this.brushSize = brushSize;
        this.brushHardness = brushHardness;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        paint = true;
        if (checkRange()) {
            dp.draw(window.getMousePosition().x - 7, window.getMousePosition().y - 30, brushSize, brushHardness);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        paint = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        paint = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (paint && checkRange()) {
            dp.draw(window.getMousePosition().x - 7, window.getMousePosition().y - 30, brushSize, brushHardness);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public boolean checkRange() {
        return board.contains(window.getMousePosition().x - 7, window.getMousePosition().y - 30);
    }
}
