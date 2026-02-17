package Utils;

import main.Tabuleiro;

/**
 *  Classe com o objetivo de validar os moviemntos e mover as pecas do jogo.
 *
 */
public class LogicTabuleiro {

    private final Tabuleiro tabuleiroLogico;

    public LogicTabuleiro(Tabuleiro tabuleiroLogico) {
        this.tabuleiroLogico = tabuleiroLogico;
    }

    /* =========== FUNÇÃO DE REGRA DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    private boolean movIsValid(int r1, int c1, int r2, int c2) {
        if (r1 == r2 || c1 == c2)
            return false;

        if (Math.abs(r1 - r2) > 1 || Math.abs(c1 - c2) > 1)
            return false;

        if (tabuleiroLogico.getMatriz()[r1][c1] == 1) {
            return r1 > r2;
        }
        else if (tabuleiroLogico.getMatriz()[r1][c1] == 2) {
            return r2 > r1;
        }
        return true;
    }

    public boolean moverPecaLogica(int r1, int c1, int r2, int c2) {


        // A casa de destino deve estar vazia
        if (tabuleiroLogico.getMatriz()[r2][c2] == 0 && movIsValid(r1, c1, r2, c2)) {

            // Transfere o valor (seja 1, 2, 3 ou 4) para a nova posição
            tabuleiroLogico.getMatriz()[r2][c2] = tabuleiroLogico.getMatriz()[r1][c1];
            tabuleiroLogico.getMatriz()[r1][c1] = 0;

            // Promoção simples para Dama (opcional)
            if (tabuleiroLogico.getMatriz()[r2][c2] == 2 && r2 == 5) {
                tabuleiroLogico.getMatriz()[r2][c2] = 4;
            }
            if (tabuleiroLogico.getMatriz()[r2][c2] == 1 && r2 == 0) {
                tabuleiroLogico.getMatriz()[r2][c2] = 3;
            }

            return true;
        }
        return false;
    }


}
