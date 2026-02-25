package Engine;

import Model.Tabuleiro;


public class Engine {

    private final Tabuleiro tabuleiro;
    private final MoveManagement moveManagement;
    private final CaptureManagement captureManagement;
    private final TurnManagement turnManagement;

    private int rowOrigin = -1;
    private int colOrigin = -1;

    public Engine(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.moveManagement = new MoveManagement(tabuleiro);
        this.captureManagement = new CaptureManagement(tabuleiro);
        this.turnManagement = new TurnManagement();

    }

    public void MainGame(int r, int c) {
        // Lógica do jogo

        // Verifica turno
        boolean turn = turnManagement.whoseTurn();



    }



}
