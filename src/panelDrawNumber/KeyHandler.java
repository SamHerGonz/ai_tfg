package panelDrawNumber;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public DrawingPanel dp;
    public boolean isKeyPressed;
    public KeyHandler(DrawingPanel dp) {
        this.dp = dp;
        isKeyPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent event) {

    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (!isKeyPressed){
            isKeyPressed = true;
            int code = event.getKeyCode();
            switch (code) {
                case KeyEvent.VK_UP:
                    ((JButton) dp.getComponent(0)).doClick();
                    break;
                case KeyEvent.VK_DOWN:
                    ((JButton) dp.getComponent(1)).doClick();
                    break;
                case KeyEvent.VK_LEFT:
                    ((JButton) dp.getComponent(2)).doClick();
                    break;
                case KeyEvent.VK_RIGHT:
                    ((JButton) dp.getComponent(3)).doClick();
                    break;
                case KeyEvent.VK_R:
                    ((JButton) dp.getComponent(4)).doClick();
                    break;
                case KeyEvent.VK_ENTER:
                    ((JButton) dp.getComponent(5)).doClick();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isKeyPressed = false;
    }
}
