package Engine;

import View.CasaBotao;
import Model.Node;
import Model.Position;
import Model.Tabuleiro;
import View.PaintTabuleiro;
import View.PopUp;

import java.util.List;

public class Engine {

    private final Tabuleiro tabuleiro;
    private final MoveManagement moveManagement;
    private final TurnManagement turnManagement;
    private final PaintTabuleiro paintTabuleiro;
    private final Translator translator;

    Position origin = new Position(-1, -1);

    private List<Node> movimentos = null;
    private final int TAM;
    private int countWhite = 1;
    private int countBlack = 6;

    boolean gameOver = false;
    boolean mustCaptured = false;
    boolean isWhiteTurn = true;
    boolean popupShown = false;
    
    private GameOverListener gameOverListener;

    public Engine(Tabuleiro tabuleiro, CasaBotao[][] boardInterface) {
        this.tabuleiro = tabuleiro;
        this.TAM = tabuleiro.getTam();
        this.turnManagement = new TurnManagement();
        this.translator = new Translator(TAM);
        this.moveManagement = new MoveManagement(tabuleiro, translator);
        this.paintTabuleiro = new PaintTabuleiro(tabuleiro,boardInterface, translator);
    }
    
    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public void handleClick(int i, int j) {
        if (turnManagement.isGameOver(countWhite, countBlack)) {
            gameOver = true;
            if (gameOverListener != null) {
                gameOverListener.onGameOver(countWhite > 0);
            }
        }
        else if (countWhite == 1 && !popupShown) {
            ganharPecaGratis();
            popupShown = true;
        }

        if (gameOver)
            return;

        isWhiteTurn = turnManagement.whoseTurn();
        Position clicked = new Position(i, j);

        if (origin.getRow() == -1) {
            if (tabuleiro.isEmpty(clicked) || isWhiteTurn != tabuleiro.isWhite(clicked)) return;

            List<Node> possibleMoves = moveManagement.getMoves(translator.getCharFromPosition(clicked));

            if (moveManagement.teamHasCaptures(isWhiteTurn)) {
                if (possibleMoves.isEmpty() || !moveManagement.hasCaptures(possibleMoves)) return;
            }

            movimentos = possibleMoves;
            print(movimentos);
            origin = clicked;
            destacarMovimentos(movimentos);
        }
        else {
            if (isDoubleClick(clicked)) {
                cancelarDestaque(movimentos);
                origin.setPosition(-1,-1);
                return;
            }

            Node move = new Node(
                    translator.getCharFromPosition(origin),
                    translator.getCharFromPosition(clicked)
            );

            if (isValidMove(move)) {
                cancelarDestaque(movimentos);
                boolean wasCapture = moveManagement.isCapture(move);
                
                if (wasCapture) {
                    if (isWhiteTurn)
                        countBlack--;
                    else
                        countWhite--;
                }
                
                moveManagement.execMove(move);
                sincronizarView();

                if (wasCapture && verificarSePodeCapturar(clicked)) {
                    return;
                }

                origin.setPosition(-1,-1);
                turnManagement.changeTurn();
            }
        }
    }

    private boolean verificarSePodeCapturar(Position clicked) {
        List<Node> allMoves = moveManagement.getAllMovesForPiece(translator.getCharFromPosition(clicked));
        List<Node> captureMoves = new java.util.ArrayList<>();
        
        for (Node move : allMoves) {
            if (moveManagement.isCapture(move)) {
                captureMoves.add(move);
            }
        }

        if (!captureMoves.isEmpty()) {
            origin = clicked;
            movimentos = captureMoves;
            destacarMovimentos(movimentos);
            return true;
        }
        return false;
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

    // Debug
    private void print(List<Node> l) {
        for (Node node : l) {
            System.out.println(node.getOrigin() + " " + node.getDest());
        }
    }

    // Isso não entra no projeto de entrega
    private void ganharPecaGratis() {
        PopUp popup = new PopUp();

        if (popup.getClicked() == 1) {
            boolean added = false;
                for (int col = 0; col < TAM && !added; col++) {
                    if ((5 + col) % 2 != 0 && tabuleiro.isEmpty(new Position(5, col))) {
                        tabuleiro.setPos(new Position(5, col), Tabuleiro.WHITEPIECE);
                        countWhite++;
                        added = true;
                    }
                }

            sincronizarView();
        }

    }
}
