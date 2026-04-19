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

    /* =========== FUNÇÃO DE MOVIMENTO DE UMA PEÇA ===========*/
    public void execMove(Node move, boolean isWhiteTurn) {
        Position to = translator.getPositionFromChar(move.getDest());
        Position from = translator.getPositionFromChar(move.getOrigin());

        if (isCapture(move)) {
            tabuleiro.capture(isWhiteTurn);     // Faz a contagem das pecas
            removeCapturedPiece(from, to);
        }

        tabuleiro.setPos(to, tabuleiro.getPos(from));
        tabuleiro.setPos(from, Tabuleiro.EMPTY);

        promotionManagement.canBePromoted(to);
    }

    private void removeCapturedPiece(Position from, Position to) {
        int rowDir = (to.getRow() - from.getRow()) > 0 ? 1 : -1;
        int colDir = (to.getCol() - from.getCol()) > 0 ? 1 : -1;
        
        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;
        
        while (currentRow != to.getRow() && currentCol != to.getCol()) {
            Position current = new Position(currentRow, currentCol);
            
            if (!tabuleiro.isEmpty(current) && tabuleiro.isEnemy(from, current)) {
                tabuleiro.setPos(current, Tabuleiro.EMPTY);
                break;
            }
            
            currentRow += rowDir;
            currentCol += colDir;
        }
    }

    public List<Node> getMoves(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Node> allMoves = new ArrayList<>();
        List<Node> captureMoves = new ArrayList<>();

        if (tabuleiro.isDama(pos))
            allMoves.addAll(getMovesKing(from));
        else
            allMoves.addAll(getMovesPiece(from));

        for (Node move : allMoves) {
            if (isCapture(move)) {
                captureMoves.add(move);
//                System.out.println("Detectado a captura!");
            }
        }


        return captureMoves.isEmpty() ? allMoves : captureMoves;
    }
    
    public List<Node> getAllMovesForPiece(char from) {
        Position pos = translator.getPositionFromChar(from);
        List<Node> allMoves = new ArrayList<>();

        if (tabuleiro.isDama(pos))
            allMoves.addAll(getMovesKing(from));
        else
            allMoves.addAll(getMovesPiece(from));

        return allMoves;
    }

    public boolean isCapture(Node move) {
        Position from = translator.getPositionFromChar(move.getOrigin());
        Position to = translator.getPositionFromChar(move.getDest());
        
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        if (rowDiff >= 2 && colDiff >= 2) {
            return hasEnemyBetween(from, to);
        }
        
        return false;
    }

    private boolean hasEnemyBetween(Position from, Position to) {
        int rowDir = (to.getRow() - from.getRow()) > 0 ? 1 : -1;
        int colDir = (to.getCol() - from.getCol()) > 0 ? 1 : -1;
        
        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;
        
        boolean foundEnemy = false;
        
        while (currentRow != to.getRow() && currentCol != to.getCol()) {
            Position current = new Position(currentRow, currentCol);
            
            if (!tabuleiro.isEmpty(current)) {
                if (tabuleiro.isEnemy(from, current) && !foundEnemy) {
                    foundEnemy = true;
                } else {
                    return false; // Há outra peça no caminho
                }
            }
            
            currentRow += rowDir;
            currentCol += colDir;
        }
        
        return foundEnemy;
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
            if (tabuleiro.isInvalidParam(newpos)) continue;
            
            if (tabuleiro.isEmpty(newpos)) {
                move.add(new Node(from, translator.getCharFromPosition(newpos)));
            }
            else if (tabuleiro.isEnemy(pos, newpos)) {
                Position afterEnemy = newpos.getPosition(newpos, rowDirection, col);
                if (!tabuleiro.isInvalidParam(afterEnemy) && tabuleiro.isEmpty(afterEnemy)) {
                    move.add(new Node(from, translator.getCharFromPosition(afterEnemy)));
                }
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

        for (int[] dir : direction) {
            Position current = pos;
            boolean foundEnemy = false;

            while (true) {
                current = current.getPosition(current, dir[0], dir[1]);

                if (tabuleiro.isInvalidParam(current)) break;

                if (tabuleiro.isEmpty(current) && foundEnemy) {
                    moves.add(new Node(from, translator.getCharFromPosition(current)));
                    break;
                }
                else if (tabuleiro.isEmpty(current)) {
                    moves.add(new Node(from, translator.getCharFromPosition(current)));
                }
                else if (tabuleiro.isEnemy(pos, current) && !foundEnemy) {
                    foundEnemy = true;
                }
                else {
                    break;
                }
            }
        }
    }

    public boolean hasCaptures(List<Node> nextMoves) {
        for (Node move : nextMoves) {
            if (isCapture(move)) return true;
        }
        return false;
    }

    public boolean teamHasCaptures(boolean isWhite) {
        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {
                Position pos = new Position(i, j);
                if (!tabuleiro.isEmpty(pos) && tabuleiro.isWhite(pos) == isWhite) {
                    List<Node> moves = getMoves(translator.getCharFromPosition(pos));
                    if (hasCaptures(moves)) return true;
                }
            }
        }
        return false;
    }

    private void print(List<Node> l) {
        for (Node node : l) {
            System.out.println(node.getOrigin() + " " + node.getDest());
        }
    }
    /* =========== FUNÇÕES EM FASE DE TESTE ===========*/

    // Vou tentar fazer uma nova versão do verificar capturas duplas focado para a IA, mas pode servir tb
    // para engine caso desista do destacar movimento
    public boolean verifyDoubleCapture(char origin) {
        List<Node> allMoves = getAllMovesForPiece(origin);

        for (Node move : allMoves) {
            if (isCapture(move)) {
                return true;
            }
        }
        return false;
    }

    // Adição para a nova heurística
    public Position findCapturedPosition(Position from, Position to) {
        int rowDir = (to.getRow() - from.getRow()) > 0 ? 1 : -1;
        int colDir = (to.getCol() - from.getCol()) > 0 ? 1 : -1;

        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;

        while (currentRow != to.getRow() && currentCol != to.getCol()) {
            Position current = new Position(currentRow, currentCol);

            if (!tabuleiro.isEmpty(current) && tabuleiro.isEnemy(from, current)) {
                return current;
            }

            currentRow += rowDir;
            currentCol += colDir;
        }
        return null; // não deveria ocorrer se isCapture() retornou true
    }
}
