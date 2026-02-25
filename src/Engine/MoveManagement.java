package Engine;

import Model.*;

public class MoveManagement {

    private final Tabuleiro tabuleiro;

    public MoveManagement(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    /* =========== FUNÇÃO DE REGRA DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    public boolean canMove(Position from, Position to) {
        if (!tabuleiro.isEmpty(to)) return false;

        if (tabuleiro.getType(from) == Tabuleiro.WHITEPIECE && from.r() <= to.r()) return false;

        if (tabuleiro.getType(to) == Tabuleiro.BLACKPIECE && from.r() >= to.r()) return false;

        return true;
    }

    /* =========== FUNÇÃO DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    public void execMove(Position from, Position to) {
        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);

        if (tabuleiro.getType(to) == Tabuleiro.WHITEPIECE && to.r() == 5) {
            tabuleiro.setPos(to, Tabuleiro.WHITEKING);
        }
        else if (tabuleiro.getType(to) == Tabuleiro.BLACKPIECE && from.r() == 0) {
            tabuleiro.setPos(to, Tabuleiro.BLACKKING);
        }
    }

    /* =========== FUNÇÃO DE REGRA DE UMA DAMA ===========*/
    public boolean canKingMove(Position from, Position to) {
        if (!tabuleiro.isEmpty(to)) return false;
        return tabuleiro.isDiagonal(from, to);
    }

    /* =========== FUNÇÃO DE MOVIMENTO DE UMA DAMA ===========*/
    public void execKingMove(Position from, Position to) {
        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);
    }
}
