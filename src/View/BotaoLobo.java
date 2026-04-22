package View;

import AI.LevelAI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * View.BotaoLobo — Card de seleção de dificuldade temático (mitologia nórdica).
 *
 * Estados visuais:
 *  - Normal:     imagem com opacidade reduzida, borda sutil
 *  - Hover:      imagem em opacidade plena, borda realçada (sem animação pesada)
 *  - Selecionado: imagem _escolhido, glow suave, indicador de seleção
 */
public class BotaoLobo extends JPanel {

    private final String nome;
    private final String subtitulo;
    private final LevelAI nivel;

    private BufferedImage imgEscolhido;

    private boolean selecionado = false;
    private boolean hover = false;

    private Runnable onSelect;

    private final Color corDestaque;
    private final Color corGlow;

    public BotaoLobo(String nome, String subtitulo, LevelAI nivel,
                     String caminhoEscolhido,
                     Color corDestaque, Color corGlow) {
        this.nome = nome;
        this.subtitulo = subtitulo;
        this.nivel = nivel;
        this.corDestaque = corDestaque;
        this.corGlow = corGlow;

        try {
            File fEscolhido = new File(caminhoEscolhido);
            if (fEscolhido.exists()) imgEscolhido = ImageIO.read(fEscolhido);
        } catch (IOException e) {
            System.err.println("View.BotaoLobo: erro ao carregar imagem — " + e.getMessage());
        }

        setPreferredSize(new Dimension(210, 330));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
            @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
            @Override public void mouseClicked(MouseEvent e) { if (onSelect != null) onSelect.run(); }
        });
    }

    // ── API pública ──────────────────────────────────────────────────────────

    public void setSelecionado(boolean s) { this.selecionado = s; repaint(); }
    public boolean isSelecionado()        { return selecionado; }
    public LevelAI getNivel()             { return nivel; }
    public void setOnSelect(Runnable r)   { this.onSelect = r; }

    // ── Renderização ─────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,       RenderingHints.VALUE_RENDER_QUALITY);

        int w   = getWidth();
        int h   = getHeight();
        int arc = 16;
        int pad = 6;

        // — Fundo do card —
        Color bgCard;
        if (selecionado)        bgCard = new Color(0x18182E);
        else if (hover)         bgCard = new Color(0x14142A);
        else                    bgCard = new Color(0x0F0F1E);

        g2.setColor(bgCard);
        g2.fillRoundRect(pad, pad, w - 2*pad, h - 2*pad, arc, arc);

        // — Glow ao redor do card quando selecionado —
        if (selecionado) {
            for (int i = 5; i >= 1; i--) {
                int alpha = Math.min(20 + (6 - i) * 12, 100);
                g2.setColor(new Color(corGlow.getRed(), corGlow.getGreen(), corGlow.getBlue(), alpha));
                g2.setStroke(new BasicStroke(i * 2.0f));
                g2.drawRoundRect(pad - i, pad - i,
                        w - 2*pad + 2*i, h - 2*pad + 2*i,
                        arc + i * 2, arc + i * 2);
            }
        }

        // — Borda do card —
        Color borderColor;
        if (selecionado)   borderColor = corDestaque;
        else if (hover)    borderColor = corDestaque.darker();
        else               borderColor = new Color(0x28284A);

        g2.setStroke(new BasicStroke(selecionado ? 1.8f : 1.0f));
        g2.setColor(borderColor);
        g2.drawRoundRect(pad, pad, w - 2*pad, h - 2*pad, arc, arc);

        // — Imagem do lobo —
        BufferedImage img = imgEscolhido;

        if (img != null) {
            int imgAreaX = pad + 14;
            int imgAreaY = pad + 14;
            int imgAreaW = w - 2*pad - 28;
            int imgAreaH = (int)(h * 0.60);

            float opacity = selecionado ? 1.0f : (hover ? 0.88f : 0.65f);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            double scale = Math.min(
                    (double) imgAreaW / img.getWidth(),
                    (double) imgAreaH / img.getHeight()
            );
            int drawW = (int)(img.getWidth()  * scale);
            int drawH = (int)(img.getHeight() * scale);
            int drawX = imgAreaX + (imgAreaW - drawW) / 2;
            int drawY = imgAreaY + (imgAreaH - drawH) / 2;

            g2.drawImage(img, drawX, drawY, drawW, drawH, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // — Separador horizontal decorativo —
        int sepY = (int)(h * 0.68);
        g2.setStroke(new BasicStroke(1.0f));
        g2.setColor(selecionado ? corDestaque.darker() : new Color(0x28284A));
        g2.drawLine(pad + 24, sepY, w - pad - 24, sepY);

        // — Nome do lobo —
        int nomeY = (int)(h * 0.75);
        Font fonteNome = new Font("SansSerif", Font.BOLD, 18);
        g2.setFont(fonteNome);
        FontMetrics fmNome = g2.getFontMetrics();
        int nomeX = (w - fmNome.stringWidth(nome)) / 2;

        g2.setColor(selecionado ? corDestaque : new Color(0xC8C8DE));
        g2.drawString(nome, nomeX, nomeY);

        // — Subtítulo (dificuldade) —
        Font fonteSub = new Font("SansSerif", Font.PLAIN, 12);
        g2.setFont(fonteSub);
        FontMetrics fmSub = g2.getFontMetrics();
        int subX = (w - fmSub.stringWidth(subtitulo)) / 2;
        g2.setColor(selecionado ? new Color(
                Math.min(corDestaque.getRed()   + 40, 255),
                Math.min(corDestaque.getGreen() + 40, 255),
                Math.min(corDestaque.getBlue()  + 40, 255)
        ) : new Color(0x606080));
        g2.drawString(subtitulo, subX, nomeY + 20);

        // — Ponto indicador de seleção —
        if (selecionado) {
            int dotD = 7;
            int dotX = (w - dotD) / 2;
            int dotY = h - pad - 20;
            g2.setColor(corDestaque);
            g2.fillOval(dotX, dotY, dotD, dotD);
        }

        g2.dispose();
    }
}
