package View;

import Model.CasaBotao;
import Model.Tabuleiro;
import Engine.Engine;

import javax.swing.*;
import java.awt.*;

public final class Interface extends JFrame {

    private final int LENGHT = 6;
    private final CasaBotao[][] boardInterface = new CasaBotao[LENGHT][LENGHT];
    private final Engine engine;
    private final Tabuleiro tabuleiro;

    public Interface() {
        tabuleiro = new Tabuleiro();
        engine = new Engine(tabuleiro, boardInterface);

        setInterface();
        initComponents();
        sincronize();

        setVisible(true);
    }

    private void setInterface() {
        setTitle("DISCIPLINA - IA - MINI JOGO DE DAMA");
        setSize(800, 800);
        setLayout(new GridLayout(LENGHT, LENGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        for (int i = 0; i< LENGHT; i++) {
            for (int j = 0; j < LENGHT; j++) {
                boardInterface[i][j] = new CasaBotao();
                engine.initColorSquares(i, j);

                final int row = i;
                final int col = j;
                boardInterface[i][j].addActionListener(_ -> engine.MainGame(row,col));
                add(boardInterface[i][j]);
            }
        }
    }

    private void sincronize() {
        for  (int i = 0; i < LENGHT; i++) {
            for (int j = 0; j < LENGHT; j++) {
                char piece = tabuleiro.getType(i, j);
                boardInterface[i][j].setTipoPeca(piece);
            }
        }
    }


}
