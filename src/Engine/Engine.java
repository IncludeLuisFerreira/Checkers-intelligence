package Engine;

import Model.Position;
import Model.Tabuleiro;


public class Engine {

    private final Tabuleiro tabuleiro;
    private final MoveManagement moveManagement;
    private final CaptureManagement captureManagement;
    private final TurnManagement turnManagement;

    Position position = new Position(-1, -1);

    private int countWhite = 6;
    private int countBlack = 6;

    boolean gameOver = false;
    boolean haveCaptured = false;
    boolean canCapture = false;

    public Engine(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.moveManagement = new MoveManagement(tabuleiro);
        this.captureManagement = new CaptureManagement(tabuleiro);
        this.turnManagement = new TurnManagement();

    }

    public void MainGame(int r, int c) {
        // Lógica do jogo




        // Verifica turno
        turnManagement.decide(haveCaptured, canCapture);
        boolean turn = turnManagement.whoseTurn();

        if (position.getRow() == -1) {


            if (tabuleiro.isWhite(position)  ^ turn) {
                position.setPosition(r, c);


            }
        }

    }

    private void selectPiece(Position pos) {

    }



}
