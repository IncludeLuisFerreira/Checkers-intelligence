package Utils;

import Entities.CasaBotao;
import Entities.Tabuleiro;


import java.awt.*;

public class PaintTabuleiro {

    private final Tabuleiro tabuleiroLogico;
    private final CasaBotao[][] tabuleiroInterface;

    public PaintTabuleiro(Tabuleiro tabuleiroLogico, CasaBotao[][] tabuleiroInterface) {
        this.tabuleiroInterface = tabuleiroInterface;
        this.tabuleiroLogico = tabuleiroLogico;
    }

    public void colorirCasa(int i, int j) {

        if ((i + j) % 2 == 0) {
            tabuleiroInterface[i][j].setBackground(new Color(235, 235, 208)); // Bege
        }
        else {
            tabuleiroInterface[i][j].setBackground(new Color(119, 149, 86));  // Verde
        }
    }

    public void colorirCasaVerde(int i, int j) {
        tabuleiroInterface[i][j].setBackground(new Color(119, 149, 86));  // Verde
    }

    public void mostrarPossiveisJogadas(int linhaOrigem,int colOrigem, char type) {

        int offsetColum = 0;
        int offseRow = 0;

        switch (type) {
            case '1' -> {
                offsetColum = -1;
                offseRow = -1;
                break;
            }
            case '2' -> {
                offsetColum = +1;
                offseRow = +1;
                break;
            }
            default -> System.out.println(type + " Do not known that type");
        }

        if (linhaOrigem >= 0 && colOrigem > 0 && colOrigem < 5) {

            if (tabuleiroLogico.getMatriz()[linhaOrigem + offseRow][colOrigem - 1] == '0') { // Verifica se existe peca na esquerda
                tabuleiroInterface[linhaOrigem + offseRow][colOrigem - 1].setBackground(new Color(128, 128, 128)); // cinza
            }

            if (tabuleiroLogico.getMatriz()[linhaOrigem + offsetColum][colOrigem + 1] == '0') { // verifica se existe peca na direita
                tabuleiroInterface[linhaOrigem + offseRow][colOrigem + 1].setBackground(new Color(128, 128, 128)); // cinza
            }
        }
        else if (linhaOrigem >= 0 && colOrigem == 5) {
            if (tabuleiroLogico.getMatriz()[linhaOrigem + offseRow][colOrigem - 1] == '0')
                tabuleiroInterface[linhaOrigem + offseRow][colOrigem - 1].setBackground(new Color(128, 128, 128)); // cinza
        }
        else if (linhaOrigem >= 0 && colOrigem == 0) {
            if (tabuleiroLogico.getMatriz()[linhaOrigem + offseRow][colOrigem + 1] == '0')
                tabuleiroInterface[linhaOrigem + offseRow][colOrigem + 1].setBackground(new Color(128, 128, 128)); // cinza
        }

    }

    public void cancelarPossiveisJogadas(char type, int linha, int coluna) {

        int offsetRow = 0;

        switch (type) {
            case '1' -> offsetRow = -1;
            case '2' -> offsetRow = +1;
        }

       if (linha >= 0 && linha <= 5) {

           if (coluna > 0 && coluna < 5) {
               colorirCasaVerde(linha + offsetRow, coluna - 1);
               colorirCasaVerde(linha + offsetRow, coluna + 1);
           }
           else if (coluna == 0) {
               colorirCasaVerde(linha + offsetRow, coluna + 1);
           }
           else if (coluna == 5) {
               colorirCasaVerde(linha + offsetRow, coluna - 1);
           }
       }
    }
}
