package View;

import AI.LevelAI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * MenuDificuldadeView — Tela de seleção de dificuldade temática.
 *
 * Exibe três cards (BotaoLobo) correspondentes aos lobos nórdicos:
 *   Hati   → EASY
 *   Sköll  → MEDIUM
 *   Fenrir → HARD
 *
 * Bloqueia o fluxo (modal) até o jogador confirmar com "Iniciar".
 */
public class MenuDificuldadeView extends JDialog {

    private LevelAI nivelSelecionado = null;
    private BotaoLobo[] botoes;
    private JButton btnIniciar;

    /** Ícone carregado uma vez, reutilizado no header e como ícone da janela. */
    private BufferedImage icone;

    public MenuDificuldadeView() {
        setTitle("Damas — Escolha a Dificuldade");
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Carrega o ícone e aplica na barra de título / taskbar
        try {
            icone = ImageIO.read(new File("img/icone_desktop.png"));
            setIconImage(icone);
        } catch (IOException e) {
            System.err.println("MenuDificuldadeView: ícone não encontrado — " + e.getMessage());
        }

        buildUI();
        pack();
        setLocationRelativeTo(null);
    }

    // ── Construção da interface ──────────────────────────────────────────────

    private void buildUI() {
        // Painel raiz com background escuro pintado manualmente
        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0x090914),
                        0, getHeight(), new Color(0x11112A)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(true);
        root.setBackground(new Color(0x090914));
        root.setBorder(BorderFactory.createEmptyBorder(30, 36, 28, 36));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildWolvesPanel(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 22, 0));

        // — Ícone centralizado no topo do header —
        if (icone != null) {
            int size = 72;
            Image scaled = icone.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaled));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
            panel.add(iconLabel);
        }

        JLabel title = new JLabel("ESCOLHA SEU ADVERSÁRIO");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(0xC0C0D8));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lore = new JLabel("Os lobos aguardam na névoa...");
        lore.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lore.setForeground(new Color(0x50507A));
        lore.setAlignmentX(Component.CENTER_ALIGNMENT);
        lore.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        panel.add(title);
        panel.add(lore);
        return panel;
    }

    private JPanel buildWolvesPanel() {
        // Hati  = EASY  | Sköll = MEDIUM | Fenrir = HARD
        // Cores: azul glacial | laranja brasa | roxo místico
        botoes = new BotaoLobo[]{
                new BotaoLobo(
                        "Trainamento","Relembre", LevelAI.DEFAULT,
                        "img/treinamento.png",
                        new Color(0xFBFBFB),  new Color(0xFBFBFB)
                ),
                new BotaoLobo(
                        "Hati", "● Fácil", LevelAI.EASY,
                         "img/HATI_escolhido.png",
                        new Color(0x3BE0FF), new Color(0x3BE0FF)
                ),
                new BotaoLobo(
                        "Sköll", "●● Médio", LevelAI.MEDIUM,
                         "img/skoll_escolhido.png",
                        new Color(0xFF8C2A), new Color(0xFF8C2A)
                ),
                new BotaoLobo(
                        "Fenrir", "●●● Difícil", LevelAI.HARD,
                         "img/fenrir_02_escolhido.png",
                        new Color(0xC090FF), new Color(0x9140FF)
                )
        };

        JPanel panel = new JPanel(new GridLayout(1, 3, 18, 0));
        panel.setOpaque(false);

        for (BotaoLobo botao : botoes) {
            botao.setOnSelect(() -> selecionarLobo(botao));
            panel.add(botao);
        }
        return panel;
    }

    private JPanel buildFooter() {
        btnIniciar = criarBotaoIniciar();
        btnIniciar.setEnabled(false);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
        panel.setOpaque(false);
        panel.add(btnIniciar);
        return panel;
    }

    private JButton criarBotaoIniciar() {
        JButton btn = new JButton("INICIAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isEnabled()) {
                    // Fundo ativo
                    g2.setColor(new Color(0x22224A));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    // Borda ativa
                    g2.setColor(new Color(0x7070C8));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                } else {
                    // Fundo inativo
                    g2.setColor(new Color(0x111120));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    // Borda inativa
                    g2.setColor(new Color(0x25253A));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(new Color(0x7070A0)); // começa apagado
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(_ -> dispose());
        return btn;
    }

    // ── Lógica de seleção ────────────────────────────────────────────────────

    private void selecionarLobo(BotaoLobo selecionado) {
        nivelSelecionado = selecionado.getNivel();

        for (BotaoLobo b : botoes) {
            b.setSelecionado(b == selecionado);
        }

        // Ativa o botão Iniciar e clareia sua cor
        btnIniciar.setEnabled(true);
        btnIniciar.setForeground(new Color(0xD8D8FF));
        btnIniciar.repaint();
    }

    // ── API pública ──────────────────────────────────────────────────────────

    /**
     * Mostra o menu e aguarda a escolha do jogador.
     * @return o LevelAI selecionado, ou DEFAULT se o dialog foi fechado sem escolha.
     */
    public LevelAI aguardarEscolha() {
        setVisible(true); // bloqueia aqui por ser modal
        return nivelSelecionado != null ? nivelSelecionado : LevelAI.DEFAULT;
    }
}