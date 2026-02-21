package UI;

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

    // So funciona para pecas simples
    public void mostrarPossiveisJogadas(int linhaOrigem,int colOrigem, char type) {
        int direcao = (type == '1' ? -1 : (type == '2' ? 1 : 0));

        if (direcao == 0) return;

        if (isEmpty(linhaOrigem + direcao, colOrigem - 1)) {
            setBgGray(linhaOrigem + direcao, colOrigem - 1);
        }

        if (isEmpty(linhaOrigem + direcao, colOrigem + 1)) {
            setBgGray(linhaOrigem + direcao, colOrigem + 1);
        }
    }

    public void cancelarPossiveisJogadas(char type, int linha, int coluna) {

        int offsetRow = 0;

        switch (type) {
            case '1' -> offsetRow = -1;
            case '2' -> offsetRow = 1;
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


    /*======================= TESTING FUNCTIONS =======================*/
    private boolean isNotParamValid(int r, int c) {
        return r < 0 || r > 5 || c < 0 || c > 5;
    }

    private boolean isEmpty(int r, int c) {
        if (isNotParamValid(r, c)) return false;
        return tabuleiroLogico.getMatriz()[r][c] == '0';
    }

    private void setBgGray(int r, int c) {
        if (isNotParamValid(r, c)) return;
        tabuleiroInterface[r][c].setBackground(new Color(128, 128, 128));
    }

}
