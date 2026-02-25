package Engine;

import Model.CasaBotao;
import Model.Position;
import Model.Tabuleiro;
import Model.Move;
import View.PaintTabuleiro;

import java.util.List;
import java.awt.*;


public class Engine {

    private final Tabuleiro tabuleiro;
    private final MoveManagement moveManagement;
    private final CaptureManagement captureManagement;
    private final TurnManagement turnManagement;
    private final PaintTabuleiro paintTabuleiro;
    private final Translator translator;

    Position position = new Position(-1, -1);

    private final int TAM;
    private int countWhite = 6;
    private int countBlack = 6;

    boolean gameOver = false;
    boolean haveCaptured = false;
    boolean canCapture = false;
    boolean havePlayed = false;

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
        // Lógica do jogo

        // Verifica turno
        if (havePlayed)
            turnManagement.decide(haveCaptured, canCapture);

        boolean turn = turnManagement.whoseTurn();

        Position pos = new Position(r,c);

        if (position.getRow() == -1) {


            if (tabuleiro.isWhite(pos) == turn) {
                position.setPosition(r, c);
                selectPiece(position);
                showPlays(position);

            }
        }
        else  {
            if (position.getRow() == r && position.getCol() == c)  {
                cancelSelectPiece(position);
                return;
            }



        }

    }

    private void selectPiece(Position pos) {
        paintTabuleiro.setBg(pos, paintTabuleiro.YELLOW);
    }

    private void cancelSelectPiece(Position pos) {
        unShowPlays(pos);
        paintTabuleiro.setBg(pos, paintTabuleiro.GREEN);
        position.setPosition(-1, -1);

    }

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
