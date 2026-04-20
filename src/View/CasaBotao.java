package View;

import AI.LevelAI;

import javax.swing.*;
import java.awt.*;

public class CasaBotao extends JButton {

    private char tipoPeca = 0;

    // --- VARIÁVEIS DE COR DINÂMICAS ---
    private Color corPecaWolf;
    private Color corGlowWolf;
    private Color corBordaInternaWolf;

    private Color corPecaInimigo;
    private Color corBordaInimigo;

    private Color corDama;

    public CasaBotao(LevelAI level) {
        // Define o Hati como padrão ao instanciar o botão

        switch (level) {
            case EASY -> configurarCoresFillHATI();
            case MEDIUM -> configurarCoresFillSKOLL();
            case HARD -> configurarCoresFillFENRIR();
            case DEFAULT -> configurarCoresFillDEFAULT();
        }


    }

    public void setTipoPeca(char tipo) {
        this.tipoPeca = tipo;
        repaint();
    }

    // ==========================================
    // FUNÇÕES DE CONFIGURAÇÃO DE PALETAS
    // ==========================================

    public void configurarCoresFillHATI() {
        this.corPecaWolf = new Color(0x2F8F9D);
        this.corGlowWolf = new Color(0x3BE0FF);
        this.corBordaInternaWolf = new Color(0x5EE6D8);

        this.corPecaInimigo = new Color(0xE8E8E8);
        this.corBordaInimigo = new Color(0x2A2A2A);

        this.corDama = new Color(0x7AF7FF);
    }

    public void configurarCoresFillSKOLL() {

        // LOBO (fogo / magma)
        this.corPecaWolf = new Color(0xC66A2B);
        this.corGlowWolf = new Color(0xFF8C2A);
        this.corBordaInternaWolf = new Color(0xFFD166);

        // INIMIGO (carvão / pedra quente)
        this.corPecaInimigo = new Color(0x3B2F2F);
        this.corBordaInimigo = new Color(0x1E1A1A);

        // DAMA (núcleo quente)
        this.corDama = new Color(0xFFB347);
    }

    public void configurarCoresFillFENRIR() {
        // Cores do Lobo (Roxo Místico das Runas)
        // ==========================================
        // LOBO (OURO ENVELHECIDO + AMBIENTE MÍSTICO)
        // ==========================================

        // Núcleo da peça (ouro base)
        this.corPecaWolf = new Color(0xD6B45A);

        // Glow externo (roxo frio da cena - MUITO importante)
        this.corGlowWolf = new Color(0x7A6FA3);

        // Borda interna (highlight quente — simula luz incidindo)
        this.corBordaInternaWolf = new Color(0xF0D98A);


            // ==========================================
            // INIMIGO (PEDRA FRIA / ARDÓSIA)
            // ==========================================

            this.corPecaInimigo = new Color(0xAEB3BE); // cinza levemente azulado
            this.corBordaInimigo = new Color(0x2D3241); // ardósia escura fria


            // ==========================================
            // DAMA (CONTRASTE DRAMÁTICO)
            // ==========================================

            // Em vez de vermelho puro, use vinho profundo (combina com o roxo do cenário)
            this.corDama = new Color(0x7B1E2B);
    }

    public void configurarCoresFillDEFAULT() {
        // Cores do Jogador 2 (Pretas Clássicas)
        // Usamos um cinza muito escuro em vez de preto puro para o relevo interno aparecer
        this.corPecaWolf = new Color(40, 40, 40);
        this.corGlowWolf = new Color(0, 0, 0); // O "glow" atua como uma sombra natural
        this.corBordaInternaWolf = new Color(100, 100, 100); // Borda interna cinza para dar volume 3D

        // Cores do Inimigo / Jogador 1 (Brancas Clássicas)
        this.corPecaInimigo = new Color(245, 245, 245); // Um branco levemente suave (Off-white)
        this.corBordaInimigo = Color.BLACK;

        // Cor da Dama
        this.corDama = Color.YELLOW; // A clássica coroa amarela/dourada
    }

    // ==========================================
    // RENDERIZAÇÃO
    // ==========================================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margem = 10;

        // Peças do Inimigo (1 e 3)
        if (tipoPeca == '1' || tipoPeca == '3') {
            g2.setColor(corPecaInimigo);
            g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            g2.setColor(corBordaInimigo);
            g2.drawOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);

            // Peças do Lobo / Jogador (2 e 4)
        } else if (tipoPeca == '2' || tipoPeca == '4') {

            // 1. DESENHAR O BRILHO EXTERNO (GLOW)
            int intensidadeBrilho = 5;
            for (int i = intensidadeBrilho; i > 0; i--) {
                // Lógica de transparência extraída do RGB da cor atual
                int r = corGlowWolf.getRed();
                int g_color = corGlowWolf.getGreen();
                int b = corGlowWolf.getBlue();

                // Alpha diminui conforme se afasta do centro (máximo ~25 de 255)
                int alpha = (int) (60f /(i*i));
                g2.setColor(new Color(r, g_color, b, alpha));

                int espalhamento = i * 2;
                g2.fillOval(margem - i, margem - i, getWidth() - 2 * margem + espalhamento, getHeight() - 2 * margem + espalhamento);
            }

            // 2. CORPO DA PEÇA (NÚCLEO)
            float[] dist = {0.0f, 0.7f, 1.0f};

            Color centro = corBordaInternaWolf; // highlight
            Color meio   = corPecaWolf;         // base
            Color borda  = corPecaWolf.darker().darker(); // sombra derivada

            RadialGradientPaint paint = new RadialGradientPaint(
                    new Point(getWidth()/2, getHeight()/2),
                    (getWidth() - 2 * margem)/2f,
                    dist,
                    new Color[]{centro, meio, borda}
            );

            g2.setPaint(paint);
            g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);

            // 3. BORDA INTERNA (EFEITO LUZ DE MONITOR/MAGIA)
            g2.setColor(corBordaInternaWolf);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawOval(margem + 2, margem + 2, getWidth() - 2 * margem - 4, getHeight() - 2 * margem - 4);
        }

        // Representação de Dama (3 e 4 recebem o adorno)
        if (tipoPeca > '2') {
            g2.setColor(corDama);
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(margem + 5, margem + 5, getWidth() - 2 * margem - 10, getHeight() - 2 * margem - 10);
        }
    }
}