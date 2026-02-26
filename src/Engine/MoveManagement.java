package Engine;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class MoveManagement {

    private final Tabuleiro tabuleiro;
    private final Translator translator;

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
    public void execKingMove(Move move) {
        Position to = translator.getPositionFromChar(move.to());
        Position from = translator.getPositionFromChar(move.from());
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

        addAllMoves(moves, pos, rowDirection);
        return moves;
    }

    private void addAllMoves(List<Move> move ,Position pos,  int rowDirection) {
        int[] colDirection = {1,-1};
        char from = translator.getCharFromPosition(pos);

        for (int col : colDirection) {
            Position newpos = pos.getPosition(pos, rowDirection, col);
            if (tabuleiro.isEmpty(newpos)) {
                move.add(new Move(from, translator.getCharFromPosition(newpos), false));
            }
            else if (tabuleiro.isEnemy(pos, newpos) && tabuleiro.isEmpty(newpos.getPosition(newpos, rowDirection, col))) {
                move.add(new Move(from, translator.getCharFromPosition(newpos.getPosition(newpos, rowDirection, col)), true));
            }
        }
    }

    private List<Move> getMovesKing(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Move> moves = new ArrayList<>();

        addAllMovesKing(moves, pos, from);
        return moves;
    }

    private void addAllMovesKing(List<Move> moves, Position pos, char from) {

        int[][] direction = {{1,1}, {1, -1}, {-1, 1}, {-1, -1}};
        Position current = pos;

        for (int[] dir : direction) {

            while (true) {
                current = current.getPosition(current, dir[0], dir[1]);

                if (tabuleiro.isInvalidParam(current)) break;

                if (!tabuleiro.isEnemy(pos, current)) break;

                if (tabuleiro.isEmpty(current))
                    moves.add(new Move(from, translator.getCharFromPosition(current), false));
               else {
                   Position afterEnemy = current.getPosition(current, dir[0], dir[1]);

                   if (tabuleiro.isEmpty(afterEnemy))
                       moves.add(new Move(from, translator.getCharFromPosition(afterEnemy), true));
                }
            }
        }
    }

    private List<Move> getCaptureMoves(char from, List<Move> moves) {
        Position pos = translator.getPositionFromChar(from);
        int direction = (tabuleiro.getType(pos) == Tabuleiro.WHITEPIECE) ? -1 : 1;
        List<Move> captureMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move.isCapture())
                captureMoves.add(move);
        }
        return captureMoves;
    }

    private List<Move> getCaptureKingMoves(char from, List<Move> moves) {
        List<Move> captureMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move.isCapture()) {
                captureMoves.add(move);
            }
        }

        return captureMoves;
    }

}
