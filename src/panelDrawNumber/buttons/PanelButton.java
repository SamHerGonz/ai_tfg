package panelDrawNumber.buttons;

import javax.swing.*;
import java.awt.*;

public class PanelButton extends JButton {
    private Color colorDefault;
    private Color colorPressed;
    private final BasicStroke stroke;

    public PanelButton(String text) {
        this(text, new Color(198, 195, 195), new Color(143, 140, 140), 8);

    }

    public PanelButton(String text, Color colorDefault, Color colorPressed, int width) {
        super(text);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createBevelBorder(1,new Color(30,30,30), new Color(100,100,100)));
        setColorPressed(colorPressed);
        setColorDefault(colorDefault);
        setContentAreaFilled(false);
        setOpaque(true);

        setFocusPainted(false);
        stroke = new BasicStroke(width);
    }

    public Color getColorDefault() {
        return colorDefault;
    }

    public void setColorDefault(Color colorDefault) {
        this.colorDefault = colorDefault;
    }

    public Color getColorPressed() {
        return colorPressed;
    }

    public void setColorPressed(Color colorPressed) {
        this.colorPressed = colorPressed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(stroke);
        if (getModel().isPressed()) {
            setBackground(getColorPressed());
        }
        else {
            setBackground(getColorDefault());
        }
        g.fillRect(0,0,getWidth(),getHeight());
        super.paintComponent(g);

        super.paintComponent(g);
    }
}
