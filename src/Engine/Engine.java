package Engine;

import View.CasaBotao;
import Model.Node;
import Model.Position;
import Model.Tabuleiro;
import View.PaintTabuleiro;

import java.util.List;

public class Engine {

    private final Tabuleiro tabuleiro;
    private final MoveManagement moveManagement;
    private final CaptureManagement captureManagement;
    private final TurnManagement turnManagement;
    private final PaintTabuleiro paintTabuleiro;
    private final Translator translator;

    Position origin = new Position(-1, -1);

    private List<Node> movimentos = null;
    private final int TAM;
    private int countWhite = 6;
    private int countBlack = 6;

    boolean gameOver = false;
    boolean mustCaptured = false;
    boolean isWhiteTurn = true;

    public Engine(Tabuleiro tabuleiro, CasaBotao[][] boardInterface ) {
        this.tabuleiro = tabuleiro;
        this.TAM = tabuleiro.getTam();
        this.turnManagement = new TurnManagement();
        this.translator = new Translator(TAM);
        this.moveManagement = new MoveManagement(tabuleiro, translator);
        this.captureManagement = new CaptureManagement(tabuleiro);
        this.paintTabuleiro = new PaintTabuleiro(tabuleiro,boardInterface, translator);
    }

    public void handleClick(int i, int j) {
        Position clicked = new Position(i, j);

        if (origin.getRow() == -1) {

            movimentos = moveManagement.getMoves(
                    translator.getCharFromPosition(clicked)
            );

            origin = clicked;
            destacarMovimentos(movimentos);
        }
        else {

            if (isDoubleClick(clicked)) {
                cancelarDestaque(movimentos);
                origin.setPosition(-1,-1);
                return;
            }

            Node move = new Node (
                    translator.getCharFromPosition(origin),
                    translator.getCharFromPosition(clicked)
            );

            if (isValidMove(move)) {
                cancelarDestaque(movimentos);
                moveManagement.execMove(move);
                sincronizarView();
            }


        }
    }

    private boolean isValidMove(Node move) {
       return movimentos.contains(move);
    }


    private void destacarMovimentos(List<Node> movimentos) {
        paintTabuleiro.showPossibleMoves(origin, movimentos);
    }

    private void cancelarDestaque(List<Node> movimentos) {
        paintTabuleiro.unShowPossibleMoves(origin, movimentos);
    }

    private boolean isDoubleClick(Position clicked) {
        if (tabuleiro.isEmpty(clicked)) return false;
        return clicked.getRow() == origin.getRow();
    }


    public void paint(int i, int j) {
        paintTabuleiro.colorirCasa(i,j);
    }

    // SINCRONIZAR INTERFACE COM A MATRIZ
    public void sincronizarView() {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                char piece = tabuleiro.getType(i, j);
                paintTabuleiro.atualizarCasa(i, j, piece);
            }
        }
    }
}
