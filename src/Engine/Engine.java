package Engine;

import AI.AI;
import AI.Evaluation.Evaluation;
import AI.Evaluation.QuantityEvaluation;
import View.CasaBotao;
import Model.Node;
import Model.Position;
import Model.Tabuleiro;
import View.PaintTabuleiro;
import View.PopUp;

import javax.swing.*;
import java.util.ArrayList;
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

    boolean gameOver = false;
    boolean mustCaptured = false;
    boolean isWhiteTurn = true;
    boolean popupShown = false;
    
    private GameOverListener gameOverListener;

    // Raiz da árvore do conhecimento da IA
    private Node root;
    private AI ai;
    private Evaluation evaluation;

    public Engine(Tabuleiro tabuleiro, CasaBotao[][] boardInterface) {
        this.tabuleiro = tabuleiro;
        this.TAM = tabuleiro.getTam();
        this.turnManagement = new TurnManagement();
        this.translator = new Translator(TAM);
        this.moveManagement = new MoveManagement(tabuleiro, translator);
        this.paintTabuleiro = new PaintTabuleiro(tabuleiro,boardInterface, translator);
        this.evaluation = new QuantityEvaluation();
        this.ai = new AI(tabuleiro, evaluation);
    }
    
    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public void handleClick(int i, int j) {
        if (tabuleiro.isOver()) {
            gameOver = true;
            if (gameOverListener != null) {
                gameOverListener.onGameOver(tabuleiro.getWhiteCount() > 0);
            }
        }
        else if (tabuleiro.getWhiteCount() == 1 && !popupShown) {
            ganharPecaGratis();
            popupShown = true;
        }

        // Verificação se o jogador tem pelo menos um movimento
        if (!verificarSeTemJogadas()) {
            gameOver = true;
            gameOverListener.onGameOver(false);
            return;
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

//                if (wasCapture) {
//                   tabuleiro.capture(isWhiteTurn);
//                }
                
                moveManagement.execMove(move, isWhiteTurn);
                sincronizarView();

                // Verifica dupla captura impedindo de mudar de turno
                if (wasCapture && verificarSePodeCapturar(clicked)) {
                    return;
                }

                // Mudança de turno
                origin.setPosition(-1,-1);
                turnManagement.changeTurn();
                
                // Após jogada do jogador, IA joga
                SwingUtilities.invokeLater(() -> {
                    ai.montarArvore(false);     //  Joga pelas pretas
                    executarMelhorJogada();
                });
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
       return clicked.equals(origin);
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
                        tabuleiro.incrementWhite();
                        added = true;
                    }
                }

            sincronizarView();
        }

    }


    private void executarMelhorJogada() {
        Node bestMove = ai.getBestMove();
        if (bestMove != null) {
            SwingUtilities.invokeLater(() -> {
                boolean wasCapture = moveManagement.isCapture(bestMove);
                moveManagement.execMove(bestMove, false);
                sincronizarView();
                
                if (wasCapture && moveManagement.verifyDoubleCapture(bestMove.getDest())) {
                    try {
                        Thread.sleep(300);  // Tempo para ver as capturas múltiplas
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    ai.montarArvore(false);
                    executarMelhorJogada();
                } else {
                    turnManagement.changeTurn();
                }
            });
        }
        // Sem jogadas, a IA perde!
        else {
            gameOver = true;
            if (gameOverListener != null) {
                gameOverListener.onGameOver(true);
            }
        }
    }


    private boolean verificarSeTemJogadas() {
        ArrayList<Node> jogadasPossiveis = new ArrayList<>();

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {

                if (!tabuleiro.isEmpty(new Position(i, j)) && tabuleiro.isWhite(new Position(i, j)))  {
                    jogadasPossiveis.addAll(moveManagement.getMoves(
                            translator.getCharFromPosition(new Position(i, j))
                    ));

                    if (!jogadasPossiveis.isEmpty()) return true;
                }
            }
        }

        return false;
    }
}
