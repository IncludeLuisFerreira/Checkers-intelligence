package Engine;

import Model.Tabuleiro;
import Model.Position;

public class CaptureManagement {

    private final Tabuleiro tabuleiro;

    public CaptureManagement(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    /* =========== FUNÇÃO DE REGRA DE CAPTURA DE UMA PEÇA SIMPLES ===========*/
    private boolean canSimpleCapture(Position from, Position to) {
        int linhaMeio = (from.getRow() + to.getRow()) / 2;
        int colunaMeio = (from.getCol() + to.getCol()) / 2;
        char piece = tabuleiro.getType(from);
        Position meio = new Position(linhaMeio, colunaMeio);

        return !tabuleiro.isEmpty(meio) && tabuleiro.isEmpty(to) && tabuleiro.isEnemy(from, meio);
    }

    public void execSimpleCapture(Position from, Position middle, Position to) {
      //  tabuleiroLogico.setCanEat(r1, c1, false);

        tabuleiro.setPos(middle, Tabuleiro.EMPTY);
        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);
    }

    public boolean canKingCapture(Position from, Position enemy, Position to) {
        if (tabuleiro.isEmpty(enemy) || !tabuleiro.isEnemy(from, enemy)) return false;

        if (!tabuleiro.isDama(from) || !tabuleiro.isDiagonal(from, enemy)) return false;

//        int colum, row;
//
//        if (from.c() > enemy.c()) {
//            colum = from.c() - 1;
//        }
//        else {
//            colum = from.c() + 1;
//        }
//
//        if (from.r() > enemy.r()) {
//            row = from.r() - 1;
//        }
//        else {
//            row = from.r() + 1;
//        }

        return tabuleiro.isEmpty(to);
    }

    public void execKingCapture(Position from, Position enemy, Position to) {
        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);
        tabuleiro.setPos(enemy, Tabuleiro.EMPTY);
    }


//    public boolean pieceCanEat(int r, int c) {
//        return tabuleiroLogico.getCanEat(r, c);
//    }
//
//    public boolean anyoneCanEat(boolean turn) {
//        return anyKingCanEat(turn) || anySimplePieceCanEat(turn);
//    }
//
//    private boolean anyKingCanEat(boolean turn) {
//        boolean canEat = false;
//        char type = (turn ? '3' : '4');
//
//        for (int i = 0; i < getTam(); i++) {
//            for (int j = 0; j < getTam(); j++) {
//
//                if (!isDama(i, j) || getType(i, j) != type) continue;
//
//                for (int k = 0; k < getTam() - j - 1; k++) {
//
//                    if (!isEmpty(i + 1 + k, j + 1 + k) && isEmpty(i + 2 + k, j + 2 + k)) {
//                        canEat = true;
//                        tabuleiroLogico.setCanEat(i, j, canEat);
//                        continue;
//                    }
//
//                    if (!isEmpty(i - 1 - k, j + 1 + k) && isEmpty(i - 2 - k, j + 2 + k)) {
//                        canEat = true;
//                        tabuleiroLogico.setCanEat(i, j, canEat);
//                    }
//                }
//
//                for (int k = 0; k < j; k++) {
//
//                    if (!isEmpty(i - 1 - k, j - 1 - k) && isEmpty(i - 2 - k, j - 2 - k)) {
//                        canEat = true;
//                        tabuleiroLogico.setCanEat(i, j, canEat);
//                    }
//                }
//            }
//        }
//        return canEat;
//    }
//
//
//    private boolean anySimplePieceCanEat(boolean turn) {
//
//        int direcao;
//        boolean canEat = false;
//        char type = (turn ? '1' : '2');
//
//        for (int i = 0; i < getTam(); i++) {
//            for (int j = 0; j < getTam(); j++) {
//
//                if (isEmpty(i, j) || getType(i, j) != type) continue;
//
//                if (getType(i, j) == '1') direcao = -1;
//                else direcao = 1;
//
//                if (!isEmpty(i + direcao, j + direcao) && isEmpty(i + 2 * direcao, j + 2 * direcao) && isEnemy(i, j, i + direcao, j + direcao)) {
//                    tabuleiroLogico.setCanEat(i, j, true);
//                    canEat = true;
//                }
//
//                if (!isEmpty(i + direcao, j - direcao) && isEmpty(i + 2 * direcao, j - 2 * direcao) && isEnemy(i, j, i + direcao, j - direcao)) {
//                    tabuleiroLogico.setCanEat(i, j, true);
//                    canEat = true;
//                }
//
//            }
//        }
//        return canEat;
//    }
}
