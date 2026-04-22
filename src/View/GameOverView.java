package View;

import AI.LevelAI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * GameOverView — Tela de fim de jogo temática.
 *
 * {@code jogadorVenceu = true}  → VITÓRIA
 * {@code jogadorVenceu = false} → DERROTA
 * {@code jogadorVenceu = null}  → EMPATE  (duas damas, sem captura)
 */
public class GameOverView extends JDialog {

    /**
     * null  = empate
     * true  = jogador venceu
     * false = IA venceu
     */
    private final Boolean jogadorVenceu;
    private final LevelAI  level;
    private final Runnable onRestart;

    private BufferedImage imgLobo;
    private Color  corPrimaria;
    private Color  corGlow;
    private String nomeLobo;
    private String imagemCaminho;

    // ── Construtor ───────────────────────────────────────────────────────────

    public GameOverView(Boolean jogadorVenceu, LevelAI level, Runnable onRestart) {
        this.jogadorVenceu = jogadorVenceu;
        this.level         = level;
        this.onRestart     = onRestart;

        resolverTema();
        carregarImagem();

        setTitle("Fim de Jogo");
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        buildUI();
        pack();
        setLocationRelativeTo(null);
    }

    // ── Tema por nível e resultado ───────────────────────────────────────────

    private void resolverTema() {
        if (jogadorVenceu == null) {
            // Empate — tema neutro dourado
            nomeLobo      = "Empate";
            corPrimaria   = new Color(0xD4AF37);
            corGlow       = new Color(0xD4AF37);
            imagemCaminho = "img/treinamento.png";
            return;
        }

        switch (level) {
            case EASY -> {
                nomeLobo      = "Hati";
                corPrimaria   = new Color(0x3BE0FF);
                corGlow       = new Color(0x3BE0FF);
                imagemCaminho = jogadorVenceu ? "img/HATI_escolhido.png" : "img/HATI_.png";
            }
            case MEDIUM -> {
                nomeLobo      = "Sköll";
                corPrimaria   = new Color(0xFF8C2A);
                corGlow       = new Color(0xFF8C2A);
                imagemCaminho = "img/skoll_escolhido.png";
            }
            case HARD -> {
                nomeLobo      = "Fenrir";
                corPrimaria   = new Color(0xC090FF);
                corGlow       = new Color(0x9140FF);
                imagemCaminho = jogadorVenceu ? "img/fenrir_02_escolhido.png" : "img/fenrir_01.png";
            }
            default -> {
                nomeLobo      = "Lobo";
                corPrimaria   = new Color(0xFBFBFB);
                corGlow       = new Color(0xDDDDDD);
                imagemCaminho = "img/treinamento.png";
            }
        }
    }

    private void carregarImagem() {
        try {
            imgLobo = ImageIO.read(new File(imagemCaminho));
        } catch (IOException e) {
            System.err.println("GameOverView: imagem não encontrada — " + e.getMessage());
        }
    }

    // ── Construção da UI ─────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(0x07070F),
                        0, getHeight(), new Color(0x0F0F22)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(true);
        root.setBackground(new Color(0x07070F));
        root.setBorder(BorderFactory.createEmptyBorder(36, 48, 32, 48));

        root.add(buildImagemLobo(), BorderLayout.NORTH);
        root.add(buildTextos(),     BorderLayout.CENTER);
        root.add(buildBotoes(),     BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel buildImagemLobo() {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imgLobo == null) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int size = 160, cx = getWidth() / 2, cy = getHeight() / 2;

                for (int i = 8; i >= 1; i--) {
                    int radius = size / 2 + i * 6;
                    int alpha  = Math.max(0, (9 - i) * 6);
                    g2.setColor(new Color(corGlow.getRed(), corGlow.getGreen(), corGlow.getBlue(), alpha));
                    g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
                }

                double scale = Math.min((double) size / imgLobo.getWidth(),
                        (double) size / imgLobo.getHeight());
                int drawW = (int)(imgLobo.getWidth()  * scale);
                int drawH = (int)(imgLobo.getHeight() * scale);

                // Empate: opacidade média; derrota: opacidade reduzida
                float opacity = (jogadorVenceu == null) ? 0.8f
                        : (jogadorVenceu ? 1.0f : 0.6f);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.drawImage(imgLobo, cx - drawW / 2, cy - drawH / 2, drawW, drawH, null);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(240, 200));
        return panel;
    }

    private JPanel buildTextos() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 0, 24, 0));

        String tituloTexto;
        Color  tituloColor;
        String subTexto;

        if (jogadorVenceu == null) {
            tituloTexto = "EMPATE";
            tituloColor = corPrimaria;
            subTexto    = "Os guerreiros descansam em paz.";
        } else if (jogadorVenceu) {
            tituloTexto = "VITÓRIA";
            tituloColor = corPrimaria;
            subTexto    = "Você domou " + nomeLobo + ".";
        } else {
            tituloTexto = "DERROTA";
            tituloColor = new Color(0x606080);
            subTexto    = nomeLobo + " foi implacável.";
        }

        JLabel titulo = new JLabel(tituloTexto, SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 34));
        titulo.setForeground(tituloColor);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(subTexto, SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.ITALIC, 14));
        subtitulo.setForeground(new Color(0x606080));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel.add(titulo);
        panel.add(subtitulo);
        return panel;
    }

    private JPanel buildBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        panel.setOpaque(false);

        JButton btnReiniciar = criarBotao("Jogar Novamente", true);
        JButton btnSair      = criarBotao("Sair",            false);

        btnReiniciar.addActionListener(_ -> { dispose(); if (onRestart != null) onRestart.run(); });
        btnSair.addActionListener(_ -> System.exit(0));

        panel.add(btnReiniciar);
        panel.add(btnSair);
        return panel;
    }

    private JButton criarBotao(String texto, boolean primario) {
        Color corBorda = primario ? corPrimaria           : new Color(0x303050);
        Color corFundo = primario ? new Color(
                corPrimaria.getRed() / 6, corPrimaria.getGreen() / 6, corPrimaria.getBlue() / 6)
                : new Color(0x0E0E1C);
        Color corTexto = primario ? corPrimaria           : new Color(0x505070);

        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? corFundo.brighter() : corFundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(getModel().isRollover() ? corBorda.brighter() : corBorda);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(corTexto);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(primario ? 180 : 100, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── API pública ──────────────────────────────────────────────────────────

    public void exibir() { setVisible(true); }
}
