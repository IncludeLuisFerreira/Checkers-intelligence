package Engine;

import Model.CasaBotao;
import Model.Position;
import Model.Tabuleiro;
import Model.Move;
import View.PaintTabuleiro;

import java.util.List;


public class Engine {

    private final Tabuleiro tabuleiro;
    private final MoveManagement moveManagement;
    private final CaptureManagement captureManagement;
    private final TurnManagement turnManagement;
    private final PaintTabuleiro paintTabuleiro;
    private final Translator translator;

    Position from = new Position(-1, -1);

    private final int TAM;
    private int countWhite = 6;
    private int countBlack = 6;

    boolean gameOver = false;
    boolean mustCaptured = false;
    boolean isWhiteTurn = true;

    public Engine(Tabuleiro tabuleiro, CasaBotao[][] boardInterface) {
        this.tabuleiro = tabuleiro;
        this.TAM = tabuleiro.getTam();
        this.turnManagement = new TurnManagement();
        this.translator = new Translator(TAM);
        this.moveManagement = new MoveManagement(tabuleiro, translator);
        this.captureManagement = new CaptureManagement(tabuleiro);
        this.paintTabuleiro = new PaintTabuleiro(tabuleiro,boardInterface, translator);
    }

    public void MainGame(int r, int c) {
        if (checkGameOver())  {
            gameOver = true;
            return; // Fazer uma tela de finalização
        }

        // Verifica se o quadrado clicado é permitido
        Position pos = new Position(r, c);
        if (!isValidSquare(pos)) return;

        // Seleção de peças
        if (from.getRow() == -1) {
            handleSelectedPiece(pos, isWhiteTurn);
            showPlays(from);
        }
        else {

            // Selecionou a mesma peça cancela a seleção
            if (pos.getRow() == from.getRow() || !tabuleiro.isEmpty(pos)) {
                cancelSelectPiece(from);
            }

            handleMove(from, pos);
        }
    }

    private void handleSelectedPiece(Position pos, boolean isWhiteTurn) {
        if (isWhiteTurn != tabuleiro.isWhite(pos)) return;
        if (tabuleiro.isEmpty(pos)) return;
        selectPiece(pos);
        from = pos;
    }

    private void mustCapture(boolean isWhiteTurn) {
    }

    private boolean checkGameOver() {
        if (countBlack == 0)
            System.out.println("Brancos venceram!");
        if (countWhite == 0)
            System.out.println("Pretos venceram!!");

        return countBlack == 0 || countWhite == 0;
    }


    private void selectPiece(Position pos) {
        paintTabuleiro.setBg(pos, paintTabuleiro.YELLOW);
    }

    private void cancelSelectPiece(Position pos) {
        unShowPlays(pos);
        paintTabuleiro.setBg(pos, paintTabuleiro.GREEN);
        from.setPosition(-1, -1);

    }

    private void handleDoublePiceSelected(Position pos) {
        if (tabuleiro.isEnemy(from, pos)) {
            cancelSelectPiece(from);
        }
        else {
            cancelSelectPiece(from);
            from = pos;
            selectPiece(from);
        }
        from.setPosition(-1, -1);
    }

    private boolean isValidSquare(Position pos) {
        return (pos.getRow() + pos.getCol()) % 2 != 0;
    }

    private void handleMove(Position from, Position to) {
        char charFrom = translator.getCharFromPosition(from);
        char toFrom = translator.getCharFromPosition(to);
        Move move = new Move(charFrom, toFrom);

        if (tabuleiro.isDama(from)) {
            moveManagement.canKingMove(move);
        }
        else {
            moveManagement.canMove(move);
        }
    }





    /*******************************************************************************/
    private void showPlays(Position pos) {
        char from = translator.getCharFromPosition(pos);
        List<Move> moves = moveManagement.getMoves(from);

        paintTabuleiro.showPossibleMoves(pos, moves);
    }

    private void unShowPlays(Position pos) {
        char from = translator.getCharFromPosition(pos);

        List<Move> moves = moveManagement.getMoves(from);

        paintTabuleiro.unShowPossibleMoves(pos, moves);
    }

    public void initColorSquares(int i, int j) {
        paintTabuleiro.colorirCasa(i, j);
    }



}
