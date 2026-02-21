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
        if (isNotParamValid(i, j)) return;

        if ((i + j) % 2 == 0) {
            tabuleiroInterface[i][j].setBackground(new Color(235, 235, 208)); // Bege
        }
        else {
            tabuleiroInterface[i][j].setBackground(new Color(119, 149, 86));  // Verde
        }
    }

    public void colorirCasaVerde(int i, int j) {
        if (isNotParamValid(i, j)) return;
        tabuleiroInterface[i][j].setBackground(new Color(119, 149, 86));  // Verde
    }

    // So funciona para pecas simples
    public void mostrarPossiveisJogadas(int linhaOrigem,int colOrigem) {
        char type = tabuleiroLogico.getMatriz()[linhaOrigem][colOrigem];

        if ("12".contains(String.valueOf(type))) {
            int direcao = (type == '1' ? -1 : (type == '2' ? 1 : 0));
            mostrarJogadasSimples(linhaOrigem, colOrigem, direcao);
        }
        else if ("34".contains(String.valueOf(type)))
            mostrarJogadaDama(linhaOrigem, colOrigem);
    }

    public void cancelarPossiveisJogadas(char type, int linha, int coluna) {

        int direcao = (type == '1' ? -1 : type == '2' ? 1 : 0);
        
        
        if ("12".contains(String.valueOf(type))) {
            cancelarJogadasSimples(linha, coluna, direcao);
        }
        else if ("34".contains(String.valueOf(type))) {
            cancelarJogadasDama(linha, coluna);
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

    private void mostrarJogadasSimples(int linhaOrigem, int colOrigem, int direcao) {

        if (direcao == 0) return;

        if (isEmpty(linhaOrigem + direcao, colOrigem - 1)) {
            setBgGray(linhaOrigem + direcao, colOrigem - 1);
        }

        if (isEmpty(linhaOrigem + direcao, colOrigem + 1)) {
            setBgGray(linhaOrigem + direcao, colOrigem + 1);
        }
    }

    private void cancelarJogadasSimples(int linhaOrigem, int colOrigem, int direcao) {

        if (!isNotParamValid(linhaOrigem + direcao, colOrigem - 1))
            colorirCasaVerde(linhaOrigem + direcao, colOrigem - 1);
        if (!isNotParamValid(linhaOrigem + direcao, colOrigem + 1))
            colorirCasaVerde(linhaOrigem + direcao, colOrigem + 1);
    }

    private void mostrarJogadaDama(int linhaOrigem, int colOrigem) {


        for (int i = 0; i < tabuleiroLogico.getTam() - colOrigem - 1; i++) {
            setBgGray(linhaOrigem + 1 + i, colOrigem + 1 + i);
            setBgGray(linhaOrigem - 1 - i, colOrigem + 1 + i);
        }

        for (int i = 0; i < colOrigem; i++) {
            setBgGray(linhaOrigem + i + 1, colOrigem - 1 - i);
        }

        for (int i = 0; i < linhaOrigem; i++) {
            setBgGray(linhaOrigem - 1 - i, colOrigem - 1 - i);
        }



    }

    private void cancelarJogadasDama(int linhaOrigem, int colOrigem) {

        for (int i = 0; i < tabuleiroLogico.getTam() - colOrigem - 1; i++) {
            colorirCasaVerde(linhaOrigem + 1 + i, colOrigem + 1 + i);
            colorirCasaVerde(linhaOrigem -1 - i, colOrigem + 1 + i);
        }

        for (int i = 0; i < colOrigem; i++) {
            colorirCasaVerde(linhaOrigem + i + 1, colOrigem - 1 - i);
        }
    }

}
