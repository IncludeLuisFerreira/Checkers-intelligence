package View;

import Engine.Engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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

        try {
            setIconImage(ImageIO.read(new File("img/icone_desktop.png")));
        } catch (IOException e) {
            System.err.println("Interface: ícone não encontrado — " + e.getMessage());
        }
    }

    private void montarLayout(CasaBotao[][] boardInterface) {
        setLayout(new GridLayout(LENGTH, LENGTH));
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                engine.paint(i, j);

                final int row = i;
                final int col = j;
                add(boardInterface[i][j]);
                if (((i+j) & 1) == 1) {
                    boardInterface[i][j].addActionListener(_ -> engine.handleClick(row, col));
                }
            }
        }
    }

    public void declararVencedor(boolean isWhite) {
        String vencedor = (isWhite ? "Brancos venceram" : "Pretos venceram");

        JOptionPane.showMessageDialog(
                null,
                vencedor,
                "Fim de jogo",
                JOptionPane.INFORMATION_MESSAGE
        );
    }



}