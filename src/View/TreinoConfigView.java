package View;

import AI.Evaluation.Evaluation;
import AI.Evaluation.OffensiveEvaluation;
import AI.Evaluation.PositionalEvaluation;
import AI.Evaluation.QuantityEvaluation;
import AI.TrainingConfig;
import Engine.Translator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * TreinoConfigView — Diálogo de configuração do Modo Treino.
 *
 * Permite ao jogador definir:
 *  1. Cor das peças  (Brancas ou Pretas)
 *  2. Nível da IA    (profundidade 1-9 da árvore MiniMax)
 *  3. Heurística     (Quantidade · Ofensiva · Posicional · Aleatório)
 *
 * Retorna um {@link TrainingConfig} via {@link #aguardarConfig()}.
 */
public class TreinoConfigView extends JDialog {

    // ── Paleta do tema ──────────────────────────────────────────────────────
    private static final Color BG_TOP    = new Color(0x090914);
    private static final Color BG_BOT    = new Color(0x11112A);
    private static final Color CARD_BG   = new Color(0x0F0F1E);
    private static final Color CARD_SEL  = new Color(0x1A1A30);
    private static final Color BORDER_N  = new Color(0x28284A);
    private static final Color TEXT_PRI  = new Color(0xC0C0D8);
    private static final Color TEXT_SEC  = new Color(0x60607A);
    private static final Color ACCENT    = new Color(0xFBFBFB);   // branco treino
    private static final Color ACCENT2   = new Color(0xA0A0C8);   // azul-cinza

    // ── Definições de heurística ────────────────────────────────────────────
    private record HeuristicDef(String name, String desc, Color color, Evaluation eval) {}

    private static final HeuristicDef[] HEURISTICS = {
            new HeuristicDef("Quantidade", "Conta peças e damas",
                    new Color(0x3BE0FF), new QuantityEvaluation()),
            new HeuristicDef("Ofensiva", "Valoriza avanço e promoção",
                    new Color(0xFF8C2A), new OffensiveEvaluation()),
            new HeuristicDef("Posicional", "Análise completa (5 fatores)",
                    new Color(0xC090FF), new PositionalEvaluation(new Translator(6))),
            new HeuristicDef("Aleatório", "Sem inteligência — jogadas ao acaso",
                    new Color(0x606080), null)
    };

    // ── Estado selecionado ──────────────────────────────────────────────────
    private boolean playerIsWhite  = true;
    private int     selectedLevel  = 5;   // default: Médio (depth 10)
    private int     selectedHeur   = 0;   // default: Quantidade

    // ── Widgets ─────────────────────────────────────────────────────────────
    private final JButton[]      colorBtns   = new JButton[2];
    private final JPanel[]       heurCards   = new JPanel[4];
    private       JLabel         levelLabel;
    private       JSlider        depthSlider;
    private       JButton        btnIniciar;

    private TrainingConfig result = null;

    // ── Construtor ──────────────────────────────────────────────────────────

    public TreinoConfigView() {
        setTitle("Modo Treino — Configurações");
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        try { setIconImage(ImageIO.read(new File("img/icone_desktop.png"))); }
        catch (IOException ignored) {}

        buildUI();
        pack();
        setLocationRelativeTo(null);
    }

    // ── API pública ──────────────────────────────────────────────────────────

    /**
     * Exibe o diálogo e bloqueia até o jogador confirmar.
     * @return {@link TrainingConfig} com as escolhas do jogador.
     */
    public TrainingConfig aguardarConfig() {
        setVisible(true);
        // Diálogo fechado — monta o config com os valores atuais
        HeuristicDef hd = HEURISTICS[selectedHeur];
        return new TrainingConfig(
                playerIsWhite,
                TrainingConfig.levelToHeight(selectedLevel),
                hd.eval(),
                hd.name()
        );
    }

    // ── Construção da UI ────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(true);
        root.setBorder(BorderFactory.createEmptyBorder(28, 36, 24, 36));

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildCenter(),  BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Header ───────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        BufferedImage icone = null;
        try { icone = ImageIO.read(new File("img/treinamento.png")); } catch (IOException ignored) {}
        if (icone != null) {
            Image sc = icone.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            JLabel ic = new JLabel(new ImageIcon(sc));
            ic.setAlignmentX(Component.CENTER_ALIGNMENT);
            ic.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            p.add(ic);
        }

        JLabel title = styledLabel("MODO TREINO", 20, Font.BOLD, TEXT_PRI);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(title);

        JLabel sub = styledLabel("Configure o adversário antes de jogar", 12, Font.ITALIC, TEXT_SEC);
        sub.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(sub);

        return p;
    }

    // ── Painel central ────────────────────────────────────────────────────────

    private JPanel buildCenter() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        p.add(buildSectionTitle("Jogar como"));
        p.add(Box.createRigidArea(new Dimension(0, 8)));
        p.add(buildColorSelector());

        p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(buildSectionTitle("Nível do adversário"));
        p.add(Box.createRigidArea(new Dimension(0, 8)));
        p.add(buildDepthSlider());

        p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(buildSectionTitle("Heurística"));
        p.add(Box.createRigidArea(new Dimension(0, 8)));
        p.add(buildHeuristicCards());

        return p;
    }

    // ── Seção: cor das peças ──────────────────────────────────────────────────

    private JPanel buildColorSelector() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        row.setOpaque(false);

        String[] labels = {"⬜  Brancas (começa)", "⬛  Pretas (IA começa)"};
        boolean[] values = {true, false};

        for (int i = 0; i < 2; i++) {
            final boolean isWhite = values[i];
            colorBtns[i] = buildToggleButton(labels[i]);
            colorBtns[i].addActionListener(_ -> {
                playerIsWhite = isWhite;
                refreshColorButtons();
                refreshDepthSlider();
            });
            row.add(colorBtns[i]);
        }

        refreshColorButtons();
        return row;
    }

    private JButton buildToggleButton(String label) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = Boolean.parseBoolean(getClientProperty("selected").toString());
                g2.setColor(sel ? CARD_SEL : CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(sel ? ACCENT : BORDER_N);
                g2.setStroke(new BasicStroke(sel ? 1.6f : 1.0f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.putClientProperty("selected", "false");
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setForeground(TEXT_PRI);
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(210, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshColorButtons() {
        colorBtns[0].putClientProperty("selected", String.valueOf(playerIsWhite));
        colorBtns[1].putClientProperty("selected", String.valueOf(!playerIsWhite));
        colorBtns[0].setForeground(playerIsWhite  ? ACCENT : TEXT_SEC);
        colorBtns[1].setForeground(!playerIsWhite ? ACCENT : TEXT_SEC);
        colorBtns[0].repaint();
        colorBtns[1].repaint();
    }

    // ── Seção: nível de profundidade ──────────────────────────────────────────

    private JPanel buildDepthSlider() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        // Rótulo dinâmico
        levelLabel = styledLabel(
                levelDescription(selectedLevel), 13, Font.BOLD, ACCENT2);
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Slider 1-9
        depthSlider = new JSlider(1, 9, selectedLevel);
        depthSlider.setOpaque(false);
        depthSlider.setForeground(TEXT_SEC);
        depthSlider.setPaintTicks(true);
        depthSlider.setMinorTickSpacing(1);
        depthSlider.setMajorTickSpacing(4);
        depthSlider.setPaintLabels(true);
        depthSlider.setFont(new Font("SansSerif", Font.PLAIN, 10));
        depthSlider.setPreferredSize(new Dimension(440, 48));
        depthSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        depthSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        depthSlider.addChangeListener(_ -> {
            selectedLevel = depthSlider.getValue();
            levelLabel.setText(levelDescription(selectedLevel));
        });

        // Linha com extremos
        JPanel extremes = new JPanel(new BorderLayout());
        extremes.setOpaque(false);
        extremes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        JLabel lFacil   = styledLabel("Mais Fácil", 10, Font.PLAIN, TEXT_SEC);
        JLabel lDificil = styledLabel("Mais Difícil", 10, Font.PLAIN, TEXT_SEC);
        extremes.add(lFacil,   BorderLayout.WEST);
        extremes.add(lDificil, BorderLayout.EAST);

        col.add(levelLabel);
        col.add(Box.createRigidArea(new Dimension(0, 4)));
        col.add(depthSlider);
        col.add(extremes);

        return col;
    }

    private String levelDescription(int level) {
        return "Nível " + level + " — " + TrainingConfig.LEVEL_LABELS[level]
                + "  (profundidade " + TrainingConfig.levelToHeight(level) + ")";
    }

    /** Habilita ou desabilita o slider quando "Aleatório" está selecionado. */
    private void refreshDepthSlider() {
        boolean random = (HEURISTICS[selectedHeur].eval() == null);
        depthSlider.setEnabled(!random);
        levelLabel.setForeground(random ? TEXT_SEC : ACCENT2);
    }

    // ── Seção: heurística ─────────────────────────────────────────────────────

    private JPanel buildHeuristicCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 10, 0));
        row.setOpaque(false);

        for (int i = 0; i < HEURISTICS.length; i++) {
            final int idx = i;
            heurCards[i] = buildHeurCard(HEURISTICS[i], i == selectedHeur);
            heurCards[i].addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    selectedHeur = idx;
                    refreshHeurCards();
                    refreshDepthSlider();
                }
            });
            row.add(heurCards[i]);
        }
        return row;
    }

    private JPanel buildHeurCard(HeuristicDef hd, boolean selected) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = Boolean.parseBoolean(getClientProperty("selected").toString());
                boolean hov = Boolean.parseBoolean(getClientProperty("hover").toString());
                Color   col = hd.color();

                // Fundo
                g2.setColor(sel ? CARD_SEL : (hov ? new Color(0x121228) : CARD_BG));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Glow quando selecionado
                if (sel) {
                    for (int i = 4; i >= 1; i--) {
                        int alpha = (5 - i) * 14;
                        g2.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), alpha));
                        g2.setStroke(new BasicStroke(i * 1.5f));
                        g2.drawRoundRect(-i, -i, getWidth() + 2*i - 1, getHeight() + 2*i - 1, 14, 14);
                    }
                }

                // Borda
                g2.setStroke(new BasicStroke(sel ? 1.6f : 1.0f));
                g2.setColor(sel ? col : (hov ? BORDER_N.brighter() : BORDER_N));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);

                // Ponto colorido no topo
                int dotR = 5;
                g2.setColor(sel ? col : TEXT_SEC);
                g2.fillOval(getWidth()/2 - dotR, 10, dotR*2, dotR*2);

                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(108, 90));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.putClientProperty("selected", String.valueOf(selected));
        card.putClientProperty("hover",    "false");

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { card.putClientProperty("hover", "true");  card.repaint(); }
            @Override public void mouseExited (MouseEvent e) { card.putClientProperty("hover", "false"); card.repaint(); }
        });

        // Nome
        JLabel nome = styledLabel(hd.name(), 12, Font.BOLD,
                selected ? hd.color() : TEXT_PRI);
        nome.setAlignmentX(Component.CENTER_ALIGNMENT);
        nome.setBorder(BorderFactory.createEmptyBorder(26, 4, 2, 4));
        nome.putClientProperty("tag", "nome");
        nome.setName("nome");

        // Descrição
        JLabel desc = new JLabel("<html><center>" + hd.desc() + "</center></html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 10));
        desc.setForeground(TEXT_SEC);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setBorder(BorderFactory.createEmptyBorder(0, 6, 6, 6));
        desc.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(nome);
        card.add(desc);

        return card;
    }

    private void refreshHeurCards() {
        for (int i = 0; i < HEURISTICS.length; i++) {
            boolean sel = (i == selectedHeur);
            heurCards[i].putClientProperty("selected", String.valueOf(sel));
            // Atualiza cor do label nome
            for (Component c : heurCards[i].getComponents()) {
                if (c instanceof JLabel lbl && "nome".equals(lbl.getName())) {
                    lbl.setForeground(sel ? HEURISTICS[i].color() : TEXT_PRI);
                }
            }
            heurCards[i].repaint();
        }
    }

    // ── Footer ────────────────────────────────────────────────────────────────

    private JPanel buildFooter() {
        btnIniciar = new JButton("INICIAR TREINO") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1C1C36));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(ACCENT2);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIniciar.setForeground(TEXT_PRI);
        btnIniciar.setOpaque(false); btnIniciar.setContentAreaFilled(false);
        btnIniciar.setBorderPainted(false); btnIniciar.setFocusPainted(false);
        btnIniciar.setPreferredSize(new Dimension(200, 42));
        btnIniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIniciar.addActionListener(_ -> dispose());   // fecha → aguardarConfig() retorna

        JPanel fp = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
        fp.setOpaque(false);
        fp.add(btnIniciar);
        return fp;
    }

    // ── Utilitários ──────────────────────────────────────────────────────────

    private JLabel styledLabel(String text, int size, int style, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", style, size));
        lbl.setForeground(color);
        return lbl;
    }

    private JPanel buildSectionTitle(String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        JLabel lbl = styledLabel(title.toUpperCase(), 11, Font.BOLD, TEXT_SEC);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        p.add(lbl);
        return p;
    }
}
