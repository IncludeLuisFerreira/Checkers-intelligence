package View;

import Engine.Engine;

import javax.swing.*;
import java.awt.*;

public final class Interface extends JFrame {

    private final int LENGTH = 6;
    private CasaBotao[][] boardInterface;
    private Engine engine;


    public Interface(CasaBotao[][] botoes, Engine engine) {
        boardInterface = botoes;
        this.engine = engine;

        setInterface();
        montarLayout(boardInterface);
        setVisible(true);

    }

    private void setInterface() {
        setTitle("DISCIPLINA - IA - MINI JOGO DE DAMA");
        setSize(800, 800);
        setLayout(new GridLayout(LENGTH, LENGTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void montarLayout(CasaBotao[][] boardInterface) {
        setLayout(new GridLayout(LENGTH, LENGTH));
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {

                engine.paint(i, j);

                final int row = i;
                final int col = j;
                add(boardInterface[i][j]);
                boardInterface[i][j].addActionListener(_ -> engine.handleClick(row, col));
            }
        }
    }



}
