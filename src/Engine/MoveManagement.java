package Engine;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class MoveManagement {

    private final Tabuleiro tabuleiro;
    private final Translator translator;
    private final PromotionManagement promotionManagement;

    public MoveManagement(Tabuleiro tabuleiro, Translator converter) {

        this.tabuleiro = tabuleiro;
        this.translator = converter;
        this.promotionManagement = new PromotionManagement(tabuleiro);
    }

    /* =========== FUNÇÃO DE REGRA DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    public boolean canMove(List<Node> moves, Node move) {
        return moves.contains(move);
    }

    /* =========== FUNÇÃO DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    public void execMove(Node move) {
        Position to = translator.getPositionFromChar(move.getOrigin());
        Position from = translator.getPositionFromChar(move.getDest());

        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);

        promotionManagement.canBePromoted(to);
    }

    /* =========== FUNÇÃO DE REGRA DE UMA DAMA ===========*/
    public boolean canKingMove(Node move) {
        Position to = translator.getPositionFromChar(move.getOrigin());
        Position from = translator.getPositionFromChar(move.getDest());

        if (!tabuleiro.isEmpty(to)) return false;
        return tabuleiro.isDiagonal(from, to);
    }

    /* =========== FUNÇÃO DE MOVIMENTO DE UMA DAMA ===========*/
    public void execKingMove(Node move) {
        Position to = translator.getPositionFromChar(move.getOrigin());
        Position from = translator.getPositionFromChar(move.getDest());
        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);
    }

    public List<Node> getMoves(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Node> moves = new ArrayList<>();

        if (tabuleiro.isDama(pos))
            moves.addAll(getMovesKing(from));
        else
            moves.addAll(getMovesPiece(from));

        return moves;
    }

    private List<Node> getMovesPiece(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Node> moves = new ArrayList<>();
        int rowDirection = (tabuleiro.isWhite(pos) ? -1 : 1);

        addAllMoves(moves, pos, rowDirection);
        return moves;
    }

    private void addAllMoves(List<Node> move ,Position pos,  int rowDirection) {
        int[] colDirection = {1,-1};
        char from = translator.getCharFromPosition(pos);

        for (int col : colDirection) {
            Position newpos = pos.getPosition(pos, rowDirection, col);
            if (tabuleiro.isEmpty(newpos)) {
                move.add(new Node(from, translator.getCharFromPosition(newpos)));
            }
            else if (tabuleiro.isEnemy(pos, newpos) && tabuleiro.isEmpty(newpos.getPosition(newpos, rowDirection, col))) {
                move.add(new Node(from, translator.getCharFromPosition(newpos.getPosition(newpos, rowDirection, col))));
            }
        }
    }

    private List<Node> getMovesKing(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Node> moves = new ArrayList<>();

        addAllMovesKing(moves, pos, from);
        return moves;
    }

    private void addAllMovesKing(List<Node> moves, Position pos, char from) {

        int[][] direction = {{1,1}, {1, -1}, {-1, 1}, {-1, -1}};
        Position current = pos;

        for (int[] dir : direction) {

            while (true) {
                current = current.getPosition(current, dir[0], dir[1]);

                if (tabuleiro.isInvalidParam(current)) break;

                if (!tabuleiro.isEnemy(pos, current)) break;

                if (tabuleiro.isEmpty(current))
                    moves.add(new Node(from, translator.getCharFromPosition(current)));
               else {
                   Position afterEnemy = current.getPosition(current, dir[0], dir[1]);

                   if (tabuleiro.isEmpty(afterEnemy))
                       moves.add(new Node(from, translator.getCharFromPosition(afterEnemy)));
                }
            }
        }
    }

    private List<Node> getCaptureMoves(char from, List<Node> moves) {
        Position pos = translator.getPositionFromChar(from);
        int direction = (tabuleiro.getType(pos) == Tabuleiro.WHITEPIECE) ? -1 : 1;
        List<Node> captureMoves = new ArrayList<>();

        for (Node move : moves) {
        }
        return captureMoves;
    }

    private List<Node> getCaptureKingMoves(char from, List<Node> moves) {
        List<Node> captureMoves = new ArrayList<>();

        for (Node move : moves) {
        }

        return captureMoves;
    }

    // TODO - Fazer uma função para pegar todos os movimentos de um determinado time

}
