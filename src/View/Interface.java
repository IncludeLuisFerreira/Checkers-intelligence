package View;

import AI.LevelAI;
import Engine.Engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public final class Interface extends JFrame {

    private static final int LENGTH = 6;

    private final CasaBotao[][] boardInterface;
    private final Engine        engine;
    private final LevelAI       level;
    private final Runnable      onRestart;

    public Interface(CasaBotao[][] botoes, Engine engine, LevelAI level, Runnable onRestart) {
        this.boardInterface = botoes;
        this.engine         = engine;
        this.level          = level;
        this.onRestart      = onRestart;

        setInterface();
        montarLayout(boardInterface);
        setVisible(true);
    }

    private void setInterface() {
        setTitle("DISCIPLINA - IA - MINI JOGO DE DAMA");
        setSize(800, 800);
        setLayout(new GridLayout(LENGTH, LENGTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            setIconImage(ImageIO.read(new File("img/icone_desktop.png")));
        } catch (IOException e) {
            System.err.println("Interface: ícone não encontrado — " + e.getMessage());
        }
    }

    private void montarLayout(CasaBotao[][] botoes) {
        setLayout(new GridLayout(LENGTH, LENGTH));
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                engine.paint(i, j);
                final int row = i, col = j;
                add(botoes[i][j]);
                if (((i + j) & 1) == 1) {
                    botoes[i][j].addActionListener(_ -> engine.handleClick(row, col));
                }
            }
        }
    }

    // ── Callbacks de fim de jogo ─────────────────────────────────────────────

    /** Chamado pelo GameOverListener quando um lado vence. */
    public void declararVencedor(boolean whiteWon) {
        new GameOverView(whiteWon, level, () -> { dispose(); onRestart.run(); }).exibir();
    }

    /** Chamado pelo GameOverListener quando ocorre empate (duas damas). */
    public void declararEmpate() {
        new GameOverView(null, level, () -> { dispose(); onRestart.run(); }).exibir();
    }
}
