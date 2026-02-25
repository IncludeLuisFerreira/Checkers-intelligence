package Engine;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class MoveManagement {

    private final Tabuleiro tabuleiro;
    private final Translator translator;
    private final Position temp = new Position(-1, -1);

    public MoveManagement(Tabuleiro tabuleiro, Translator converter) {

        this.tabuleiro = tabuleiro;
        this.translator = converter;
    }

    /* =========== FUNÇÃO DE REGRA DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    public boolean canMove(Move move) {

        Position to =  translator.getPositionFromChar(move.to());
        Position from = translator.getPositionFromChar(move.from());

        if (!tabuleiro.isEmpty(to)) return false;

        if (tabuleiro.getType(from) == Tabuleiro.WHITEPIECE && from.getRow() <= to.getRow()) return false;

        if (tabuleiro.getType(to) == Tabuleiro.BLACKPIECE && from.getRow() >= to.getRow()) return false;

        return true;
    }

    /* =========== FUNÇÃO DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    public void execMove(Move move) {
        Position to = translator.getPositionFromChar(move.to());
        Position from = translator.getPositionFromChar(move.from());

        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);

        if (tabuleiro.getType(to) == Tabuleiro.WHITEPIECE && to.getRow() == 5) {
            tabuleiro.setPos(to, Tabuleiro.WHITEKING);
        }
        else if (tabuleiro.getType(to) == Tabuleiro.BLACKPIECE && from.getRow() == 0) {
            tabuleiro.setPos(to, Tabuleiro.BLACKKING);
        }
    }

    /* =========== FUNÇÃO DE REGRA DE UMA DAMA ===========*/
    public boolean canKingMove(Move move) {
        Position to = translator.getPositionFromChar(move.to());
        Position from = translator.getPositionFromChar(move.from());

        if (!tabuleiro.isEmpty(to)) return false;
        return tabuleiro.isDiagonal(from, to);
    }

    /* =========== FUNÇÃO DE MOVIMENTO DE UMA DAMA ===========*/
    public void execKingMove(Position from, Position to) {
        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);
    }

    public List<Move> getMoves(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Move> moves = new ArrayList<>();

        if (tabuleiro.isDama(pos))
            moves.addAll(getMovesKing(from));
        else
            moves.addAll(getMovesPiece(from));

        return moves;
    }

    private List<Move> getMovesPiece(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Move> moves = new ArrayList<>();

        int rowDirection = (tabuleiro.isWhite(pos) ? -1 : 1);

        Position right = pos.getPosition(pos, rowDirection, 1);
        Position left = pos.getPosition(pos, rowDirection, -1);

        char to;

        if (tabuleiro.isEmpty(right)) {
            to = translator.getCharFromPosition(right);
            moves.add(new Move(from, to));
        }
        else if (tabuleiro.isEmpty(right.getPosition(pos, 2*rowDirection, 2))) {
            to = translator.getCharFromPosition(right.getPosition(pos, 2*rowDirection, 2));
            moves.add(new Move(from, to));
        }

        if (tabuleiro.isEmpty(left)) {
            to = translator.getCharFromPosition(left);
            moves.add(new Move(from, to));
        }
        else if  (tabuleiro.isEmpty(left.getPosition(pos, 2*rowDirection, -2))) {
            to = translator.getCharFromPosition(left.getPosition(pos, 2*rowDirection, -2));
            moves.add(new Move(from, to));
        }

        return moves;
    }

    private List<Move> getMovesKing(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Move> moves = new ArrayList<>();

        return null;
    }


}
