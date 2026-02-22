package Logic;

import Entities.Tabuleiro;

import javax.management.StringValueExp;

/**
 *  Classe para validar os movimentos e mover as pecas do jogo.
 *
 */
public class LogicTabuleiro {

    private final Tabuleiro tabuleiroLogico;
    boolean turn = true;

    public LogicTabuleiro(Tabuleiro tabuleiroLogico) {
        this.tabuleiroLogico = tabuleiroLogico;
    }

    /* =========== FUNÇÃO DE REGRA DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    private boolean simpleMove(int r1, int c1, int r2, int c2) {
        if (tabuleiroLogico.getMatriz()[r2][c2] != '0')
            return false;

        if (tabuleiroLogico.getMatriz()[r1][c1] == '1' && r1 <= r2) {
            return false;
        }
        else if (tabuleiroLogico.getMatriz()[r1][c1] == '2' && r1 >= r2) {
            return false;
        }

        tabuleiroLogico.getMatriz()[r2][c2] = tabuleiroLogico.getMatriz()[r1][c1];
        tabuleiroLogico.getMatriz()[r1][c1] = '0';
        return true;
    }

    private boolean verifyCapture(int r1, int c1, int r2, int c2) {
        int linhaMeio = (r1 + r2) / 2;
        int colunaMeio = (c1 + c2) / 2;
        char otherPiece = tabuleiroLogico.getMatriz()[linhaMeio][colunaMeio];
        char piece = tabuleiroLogico.getMatriz()[r1][c1];

        String teamPieces = ("13".contains(String.valueOf(piece)) ? "13" : "24");


        if (!teamPieces.contains(String.valueOf(otherPiece))) {
            tabuleiroLogico.getMatriz()[linhaMeio][colunaMeio] = '0';
            tabuleiroLogico.getMatriz()[r2][c2] = piece;
            tabuleiroLogico.getMatriz()[r1][c1] = '0';
            return true;
        }

        return false;
    }

    private boolean isDama(int r1, int c1) {
        return "14".contains(String.valueOf(tabuleiroLogico.getMatriz()[r1][c1]));
    }

    private boolean isDiagonal(int r1, int c1, int r2, int c2) {
        return ((r1 + c1) == (r2 + c2) || (r1 - c1) == (r2 - c2));
    }

    // funcao errada, mas por enquanto funciona para teste
    private boolean moveDama(int r1, int c1, int r2, int c2) {

        if (tabuleiroLogico.getMatriz()[r2][c2] == '0')
            return false;

        tabuleiroLogico.getMatriz()[r2][c2] = tabuleiroLogico.getMatriz()[r1][c1];
        tabuleiroLogico.getMatriz()[r1][c1] = '0';



        return true;
    }

    public boolean moverPecaLogica(int r1, int c1, int r2, int c2) {

        boolean mov = false;

        // Movimento simples
        if (Math.abs(r1 - r2) == 1 && Math.abs(c1 - c2) == 1)
            mov =  simpleMove(r1, c1, r2, c2);

        // Movimento de captura
        if (Math.abs(r1 - r2) == 2 && Math.abs(c1 - c2) == 2)
            mov = verifyCapture(r1, c1, r2, c2);

        if (isDama(r1, c1) && isDiagonal(r1, c2, r2, c2))
            mov =  moveDama(r1, c1, r2, c2);

        if (mov) {
            if (tabuleiroLogico.getMatriz()[r2][c2] == '2' && r2 == 5) {
                tabuleiroLogico.getMatriz()[r2][c2] = '4';
            }
            if (tabuleiroLogico.getMatriz()[r2][c2] == '1' && r2 == 0) {
                tabuleiroLogico.getMatriz()[r2][c2] = '3';
            }
        }

        return mov;
    }


    // True -> white; False -> black
    public boolean whoseTurn() {
      return turn;
    }

    public void changeTurn() {
        turn = !turn;
    }

    public boolean isEmpty(int linha, int coluna) {
        return tabuleiroLogico.getMatriz()[linha][coluna] == '0';
    }


    /*======================= TESTING FUNCTIONS =======================*/


}
