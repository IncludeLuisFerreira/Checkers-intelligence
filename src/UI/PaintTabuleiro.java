package UI;

import Entities.CasaBotao;
import Logic.LogicTabuleiro;


import java.awt.*;

public class PaintTabuleiro {

    private final LogicTabuleiro logic;
    private final CasaBotao[][] tabuleiroInterface;

    public PaintTabuleiro(LogicTabuleiro logic, CasaBotao[][] tabuleiroInterface) {
        this.tabuleiroInterface = tabuleiroInterface;
        this.logic = logic;
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
        if (logic.isNotParamValid(i, j)) return;
        tabuleiroInterface[i][j].setBackground(new Color(119, 149, 86));  // Verde
    }

    // So funciona para pecas simples
    public void mostrarPossiveisJogadas(int linhaOrigem,int colOrigem) {
        char type = logic.getType(linhaOrigem, colOrigem);

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




    private void setBgGray(int r, int c) {
        if (logic.isNotParamValid(r, c)) return;
        tabuleiroInterface[r][c].setBackground(new Color(128, 128, 128));
    }

    private void setBgRed(int r, int c) {
        if (logic.isNotParamValid(r, c)) return ;
        tabuleiroInterface[r][c].setBackground(new Color(255, 128, 128));
    }

    private void mostrarJogadasSimples(int linhaOrigem, int colOrigem, int direcao) {

        if (direcao == 0) return;

        if (logic.isEmpty(linhaOrigem + direcao, colOrigem - 1)) {
            setBgGray(linhaOrigem + direcao, colOrigem - 1);
        }
        else if (logic.isEmpty(linhaOrigem + (2 * direcao), colOrigem - 2) && logic.isEnemy(linhaOrigem, colOrigem, linhaOrigem + direcao, colOrigem - 1)) {

            setBgGray(linhaOrigem + (2 * direcao), colOrigem - 2);
            setBgRed(linhaOrigem + direcao, colOrigem - 1);
        }

        if (logic.isEmpty(linhaOrigem + direcao, colOrigem + 1)) {
            setBgGray(linhaOrigem + direcao, colOrigem + 1);
        }
        else if (logic.isEmpty(linhaOrigem + (2 * direcao), colOrigem + 2)) {
            setBgGray(linhaOrigem + (2 * direcao), colOrigem + 2);
            setBgRed(linhaOrigem + direcao, colOrigem + 1);
        }
    }

    private void cancelarJogadasSimples(int linhaOrigem, int colOrigem, int direcao) {
            colorirCasaVerde(linhaOrigem + direcao, colOrigem - 1);
            colorirCasaVerde(linhaOrigem + direcao, colOrigem + 1);
            colorirCasaVerde(linhaOrigem + (2*direcao), colOrigem - 2);
            colorirCasaVerde(linhaOrigem + (2*direcao), colOrigem + 2);
    }

    private void mostrarJogadaDama(int linhaOrigem, int colOrigem) {

        for (int i = 0; i < logic.getTam() - colOrigem - 1; i++) {
            if (!logic.isEmpty(linhaOrigem + 1 + i, colOrigem + 1 + i)) {

                if (logic.isEmpty(linhaOrigem + 2 + i, colOrigem + 2 + i)) {
                    setBgRed(linhaOrigem + 1 + i, colOrigem + 1 + i);
                    setBgGray(linhaOrigem + 2 + i, colOrigem + 2 + i);
                }

                break;
            }

            setBgGray(linhaOrigem + 1 + i, colOrigem + 1 + i);
        }

        for (int i = 0; i < logic.getTam() - colOrigem - 1; i++) {
            if (!logic.isEmpty(linhaOrigem -1 - i, colOrigem + 1 + i)) {

                if (logic.isEmpty(linhaOrigem - 2 - i, colOrigem + 2 + i)) {
                    setBgRed(linhaOrigem -1 - i, colOrigem + 1 + i);
                    setBgGray(linhaOrigem - 2 - i, colOrigem + 2 + i);
                }

                break;
            }

            setBgGray(linhaOrigem - 1 - i, colOrigem + 1 + i);
        }

        for (int i = 0; i < colOrigem; i++) {
            if (!logic.isEmpty(linhaOrigem + 1 + i, colOrigem - 1 - i)) {

                if (logic.isEmpty(linhaOrigem  + 2 + i, colOrigem - 2 - i)) {
                    setBgRed(linhaOrigem + 1 + i, colOrigem - 1 - i);
                    setBgGray(linhaOrigem  + 2 + i, colOrigem - 2 - i);
                }
                break;
            }

            setBgGray(linhaOrigem + i + 1, colOrigem - 1 - i);
        }

        for (int i = 0; i < linhaOrigem; i++) {
            if (!logic.isEmpty(linhaOrigem - 1 - i, colOrigem - 1 - i)) {

                if (logic.isEmpty(linhaOrigem - 2 - i, colOrigem - 2 - i)) {
                    setBgRed(linhaOrigem - 1 - i, colOrigem - 1 - i);
                    setBgGray(linhaOrigem - 2 - i, colOrigem - 2 - i);
                }
                break;
            }

            setBgGray(linhaOrigem - 1 - i, colOrigem - 1 - i);
        }
    }

    private void cancelarJogadasDama(int linhaOrigem, int colOrigem) {

        for (int i = 0; i <logic.getTam() - colOrigem - 1; i++) {
            colorirCasaVerde(linhaOrigem + 1 + i, colOrigem + 1 + i);
            colorirCasaVerde(linhaOrigem -1 - i, colOrigem + 1 + i);
        }

        for (int i = 0; i < colOrigem; i++) {
            colorirCasaVerde(linhaOrigem + i + 1, colOrigem - 1 - i);
        }

        for (int i = 0; i < linhaOrigem; i++) {
            colorirCasaVerde(linhaOrigem - 1 - i, colOrigem - 1 - i);
        }
    }

}
