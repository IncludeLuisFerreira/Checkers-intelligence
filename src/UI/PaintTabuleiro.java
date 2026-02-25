package UI;

import Model.CasaBotao;
import Engine.Engine;
import java.awt.*;

/**
 * @author includeluisferreira
 * <p>
 * Classe PaintTabuleiro é responsável pela parte visual do tabuleiro,
 * manipulando cores e destaques das casas com base no estado lógico
 * fornecido por <b>LogicTabuleiro</b>.
 * <p>
 * Principais funções:
 * <p>
 * - Definir a coloração padrão das casas do tabuleiro.
 * - Destacar visualmente movimentos possíveis e capturas.
 * - Diferenciar casas de destino e peças que podem ser capturadas.
 * - Restaurar a coloração original após cancelar destaques.
 * <p>
 * //TODO
 *  Implementar Position e passar toda a lógica de jogadas possiveis,
 *  retornar List<Intpair> para essa classe e assim colorir
 */
public class PaintTabuleiro {

    private final Engine logic;
    private final CasaBotao[][] tabuleiroInterface;

    public PaintTabuleiro(Engine logic, CasaBotao[][] tabuleiroInterface) {
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
    public void mostrarPossiveisJogadas(int linhaOrigem,int colOrigem, boolean onlyCanEat) {
        char type = logic.getType(linhaOrigem, colOrigem);

        if ("12".contains(String.valueOf(type))) {
            int direcao = (type == '1' ? -1 : (type == '2' ? 1 : 0));
            mostrarJogadasSimples(linhaOrigem, colOrigem, direcao, onlyCanEat);
        }
        else if ("34".contains(String.valueOf(type)))
            mostrarJogadaDama(linhaOrigem, colOrigem, onlyCanEat);
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

    private void setBgLightGreen(int r, int c) {
        if (logic.isNotParamValid(r, c)) return ;
        tabuleiroInterface[r][c].setBackground(new Color(70, 255, 61));
    }

    private void mostrarJogadasSimples(int linhaOrigem, int colOrigem, int direcao, boolean onlyCanEat) {

        if (direcao == 0) return;

        if (logic.isEmpty(linhaOrigem + direcao, colOrigem - 1) && !onlyCanEat) {
            setBgGray(linhaOrigem + direcao, colOrigem - 1);
        }
        else if (logic.isEmpty(linhaOrigem + (2 * direcao), colOrigem - 2) && logic.isEnemy(linhaOrigem, colOrigem, linhaOrigem + direcao, colOrigem - 1) && onlyCanEat) {

            setBgGray(linhaOrigem + (2 * direcao), colOrigem - 2);
            setBgLightGreen(linhaOrigem + direcao, colOrigem - 1);
        }

        if (logic.isEmpty(linhaOrigem + direcao, colOrigem + 1) && !onlyCanEat) {
            setBgGray(linhaOrigem + direcao, colOrigem + 1);
        }
        else if (logic.isEmpty(linhaOrigem + (2 * direcao), colOrigem + 2) && logic.isEnemy(linhaOrigem, colOrigem, linhaOrigem + direcao, colOrigem + 1) && onlyCanEat) {
            setBgGray(linhaOrigem + (2 * direcao), colOrigem + 2);
            setBgLightGreen(linhaOrigem + direcao, colOrigem + 1);
        }
    }

    private void cancelarJogadasSimples(int linhaOrigem, int colOrigem, int direcao) {
            colorirCasaVerde(linhaOrigem + direcao, colOrigem - 1);
            colorirCasaVerde(linhaOrigem + direcao, colOrigem + 1);
            colorirCasaVerde(linhaOrigem + (2*direcao), colOrigem - 2);
            colorirCasaVerde(linhaOrigem + (2*direcao), colOrigem + 2);
    }

    private void mostrarJogadaDama(int linhaOrigem, int colOrigem, boolean onlyCanEat) {

        for (int i = 0; i < logic.getTam() - colOrigem - 1; i++) {
            if (!logic.isEmpty(linhaOrigem + 1 + i, colOrigem + 1 + i)) {

                if (logic.isEmpty(linhaOrigem + 2 + i, colOrigem + 2 + i)) {
                    setBgLightGreen(linhaOrigem + 1 + i, colOrigem + 1 + i);
                    setBgGray(linhaOrigem + 2 + i, colOrigem + 2 + i);
                }

                break;
            }

            if (!onlyCanEat)
                setBgGray(linhaOrigem + 1 + i, colOrigem + 1 + i);
        }

        for (int i = 0; i < logic.getTam() - colOrigem - 1; i++) {
            if (!logic.isEmpty(linhaOrigem -1 - i, colOrigem + 1 + i)) {

                if (logic.isEmpty(linhaOrigem - 2 - i, colOrigem + 2 + i)) {
                    setBgLightGreen(linhaOrigem -1 - i, colOrigem + 1 + i);
                    setBgGray(linhaOrigem - 2 - i, colOrigem + 2 + i);
                }

                break;
            }

            if (!onlyCanEat)
                setBgGray(linhaOrigem - 1 - i, colOrigem + 1 + i);
        }

        for (int i = 0; i < colOrigem; i++) {
            if (!logic.isEmpty(linhaOrigem + 1 + i, colOrigem - 1 - i)) {

                if (logic.isEmpty(linhaOrigem  + 2 + i, colOrigem - 2 - i)) {
                    setBgLightGreen(linhaOrigem + 1 + i, colOrigem - 1 - i);
                    setBgGray(linhaOrigem  + 2 + i, colOrigem - 2 - i);
                }
                break;
            }

            if (!onlyCanEat)
                setBgGray(linhaOrigem + i + 1, colOrigem - 1 - i);
        }

        for (int i = 0; i < linhaOrigem; i++) {
            if (!logic.isEmpty(linhaOrigem - 1 - i, colOrigem - 1 - i)) {

                if (logic.isEmpty(linhaOrigem - 2 - i, colOrigem - 2 - i)) {
                    setBgLightGreen(linhaOrigem - 1 - i, colOrigem - 1 - i);
                    setBgGray(linhaOrigem - 2 - i, colOrigem - 2 - i);
                }
                break;
            }

            if (!onlyCanEat)
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
