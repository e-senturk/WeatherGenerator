package tools;

import javax.swing.*;
import java.awt.*;

//Verilen 2 renk ile gradiyent arkaplan oluşturan panel
public class GradientPanel extends JPanel {
    private final Color color1;
    private final Color color2;

    public GradientPanel(Color color1, Color color2) {
        super();
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}