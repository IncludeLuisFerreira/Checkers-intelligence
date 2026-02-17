package Utils;

import Entities.CasaBotao;
import  main.Tabuleiro;


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

    public void mostrarPossiveisJogadas(int linhaOrigem,int colOrigem, int type) {

        if (type == 1) {

            if (linhaOrigem > 0 && colOrigem > 0 && colOrigem < 5) {
                if (tabuleiroLogico.getMatriz()[linhaOrigem - 1][colOrigem - 1] == 0) { // Verifica se existe peca na esquerda
                    tabuleiroInterface[linhaOrigem - 1][colOrigem-1].setBackground(new Color(128, 128, 128)); // cinza
                }
                if (tabuleiroLogico.getMatriz()[linhaOrigem - 1][colOrigem + 1] == 0) { // verifica se existe peca na direita
                    tabuleiroInterface[linhaOrigem - 1][colOrigem+1].setBackground(new Color(128, 128, 128)); // cinza
                }
            }
            else if (linhaOrigem > 0 && colOrigem == 5) {
                if (tabuleiroLogico.getMatriz()[linhaOrigem - 1][colOrigem - 1] == 0) { // Verifica se existe peca na esquerda
                    tabuleiroInterface[linhaOrigem - 1][colOrigem-1].setBackground(new Color(128, 128, 128)); // cinza
                }
            }
            else if (linhaOrigem > 0 && colOrigem == 0) {
                if (tabuleiroLogico.getMatriz()[linhaOrigem - 1][colOrigem + 1] == 0) { // verifica se existe peca na direita
                    tabuleiroInterface[linhaOrigem - 1][colOrigem+1].setBackground(new Color(128, 128, 128)); // cinza
                }
            }
        }
        else {
            // eh foda mas to com preguica
        }

    }

    public void cancelarPossiveisJogadas(int type, int linha, int coluna) {

        if (type == 1) {
            if (linha > 0 && coluna > 0 && coluna < 5) {
                colorirCasaVerde(linha-1, coluna-1);
                colorirCasaVerde(linha-1, coluna+1);
            }
            else if (linha > 0 && coluna == 0) {
                colorirCasaVerde(linha-1, coluna+1);
            }
            else if (linha> 0 && coluna == 5) {
                colorirCasaVerde(linha-1, coluna-1);
            }
        }
        else {

        }
    }
}
