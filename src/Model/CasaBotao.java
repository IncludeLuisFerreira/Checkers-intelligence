package Entities;

import javax.swing.*;
import java.awt.*;

public class CasaBotao extends JButton {

    private char tipoPeca = 0;


    public void setTipoPeca(char tipo) {
        this.tipoPeca = tipo;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margem = 10;
        // Brancas
        if (tipoPeca == '1' || tipoPeca == '3') {
            g2.setColor(Color.WHITE);
            g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            g2.setColor(Color.BLACK);
            g2.drawOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            // Pretas
        } else if (tipoPeca == '2' || tipoPeca == '4') {
            g2.setColor(Color.BLACK);
            g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
        }

        // Representação de Dama (uma borda dourada)
        if (tipoPeca > '2') {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(margem + 5, margem + 5, getWidth() - 2 * margem - 10, getHeight() - 2 * margem - 10);
        }
    }
}