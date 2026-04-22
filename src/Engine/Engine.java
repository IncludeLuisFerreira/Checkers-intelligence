package Engine;

import AI.AI;
import AI.LevelAI;
import AI.TrainingConfig;
import View.CasaBotao;
import Model.Node;
import Model.Position;
import Model.Tabuleiro;
import View.PaintTabuleiro;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Engine {

    private final Tabuleiro       tabuleiro;
    private final MoveManagement  moveManagement;
    private final TurnManagement  turnManagement;
    private final PaintTabuleiro  paintTabuleiro;
    private final Translator      translator;
    private final AI              ai;
    private final LevelAI         level;
    private final int             TAM;

    /**
     * true  = jogador usa peças brancas (padrão).
     * false = jogador usa peças pretas → IA inicia como brancas.
     */
    private final boolean playerIsWhite;

    /** Cor da IA (sempre o oposto do jogador). */
    private final boolean iaIsWhite;

    Position origin = new Position(-1, -1);

    private List<Node> movimentos = null;
    boolean gameOver    = false;
    boolean isWhiteTurn = true;

    private GameOverListener gameOverListener;

    // ── Construtor padrão (níveis fixos) ────────────────────────────────────

    public Engine(Tabuleiro tabuleiro, CasaBotao[][] boardInterface, LevelAI level) {
        this(tabuleiro, boardInterface, level, null);
    }

    // ── Construtor com suporte a TrainingConfig ──────────────────────────────

    public Engine(Tabuleiro tabuleiro, CasaBotao[][] boardInterface,
                  LevelAI level, TrainingConfig config) {
        this.level          = level;
        this.tabuleiro      = tabuleiro;
        this.TAM            = tabuleiro.getTam();
        this.turnManagement = new TurnManagement();
        this.translator     = new Translator(TAM);
        this.moveManagement = new MoveManagement(tabuleiro, translator);
        this.paintTabuleiro = new PaintTabuleiro(tabuleiro, boardInterface, translator, level);

        if (config != null) {
            this.playerIsWhite = config.playerIsWhite;
            this.ai            = new AI(tabuleiro, translator, config);
        } else {
            this.playerIsWhite = true;   // padrão: jogador = brancas
            this.ai            = new AI(tabuleiro, translator, level);
        }

        this.iaIsWhite = !playerIsWhite;
    }

    // ── Inicialização pós-construção ─────────────────────────────────────────

    /**
     * Deve ser chamado após {@link #setGameOverListener}.
     * Se o jogador escolheu pretas, a IA (brancas) executa a primeira jogada.
     */
    public void iniciar() {
        if (!playerIsWhite) {
            SwingUtilities.invokeLater(() -> {
                ai.montarArvore(iaIsWhite);
                executarJogadaIA();
            });
        }
    }

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    // ── Entrada do jogador ───────────────────────────────────────────────────

    public void handleClick(int i, int j) {
        checkGameOver();
        if (gameOver) return;

        isWhiteTurn = turnManagement.whoseTurn();

        // BUG CORRIGIDO: bloqueia cliques durante a vez da IA
        if (isWhiteTurn != playerIsWhite) return;

        Position clicked = new Position(i, j);

        if (origin.getRow() == -1) {
            // Selecionar peça
            if (tabuleiro.isEmpty(clicked) || isWhiteTurn != tabuleiro.isWhite(clicked)) return;

            List<Node> possibleMoves = moveManagement.getMoves(
                    translator.getCharFromPosition(clicked));

            if (moveManagement.teamHasCaptures(isWhiteTurn)) {
                if (possibleMoves.isEmpty() || !moveManagement.hasCaptures(possibleMoves)) return;
            }

            movimentos = possibleMoves;
            origin     = clicked;
            destacarMovimentos(movimentos);

        } else {
            // Mover peça já selecionada
            if (isDoubleClick(clicked)) {
                cancelarDestaque(movimentos);
                origin.setPosition(-1, -1);
                return;
            }

            Node move = new Node(
                    translator.getCharFromPosition(origin),
                    translator.getCharFromPosition(clicked));

            if (isValidMove(move)) {
                cancelarDestaque(movimentos);
                boolean wasCapture = moveManagement.isCapture(move);

                moveManagement.execMove(move, isWhiteTurn);
                sincronizarView();
                checkGameOver();
                if (gameOver) return;

                // Dupla captura: mantém o turno do jogador
                if (wasCapture && verificarSePodeCapturar(clicked)) return;

                origin.setPosition(-1, -1);
                turnManagement.changeTurn();

                // IA joga na sequência
                SwingUtilities.invokeLater(() -> {
                    ai.montarArvore(iaIsWhite);
                    executarJogadaIA();
                });
            }
        }
    }

    // ── Execução da IA ───────────────────────────────────────────────────────

    /**
     * Executa a melhor jogada ou uma aleatória (quando sem avaliador).
     * Usa Timer em vez de Thread.sleep para não bloquear a EDT.
     *
     * BUG CORRIGIDO (NPE): versão anterior não retornava ao obter null.
     * BUG CORRIGIDO (EDT): Thread.sleep substituído por javax.swing.Timer.
     */
    private void executarJogadaIA() {
        Node move = ai.usesMinimax() ? ai.getBestMove() : null;
        if (move == null) move = ai.getRandomMove(iaIsWhite);   // fallback / modo aleatório

        if (move == null) {
            // IA sem jogadas → jogador vence
            gameOver = true;
            if (gameOverListener != null) gameOverListener.onGameOver(playerIsWhite);
            return;
        }

        boolean wasCapture = moveManagement.isCapture(move);
        moveManagement.execMove(move, iaIsWhite);
        sincronizarView();
        checkGameOver();
        if (gameOver) return;

        if (wasCapture && moveManagement.verifyDoubleCapture(move.getDest())) {
            // Dupla captura da IA: pausa visual sem bloquear a EDT
            Node captured = move;
            Timer delay = new Timer(300, _ -> {
                ai.montarArvore(iaIsWhite);
                executarJogadaIA();
            });
            delay.setRepeats(false);
            delay.start();
        } else {
            turnManagement.changeTurn();
        }
    }

    // ── Verificação de fim de jogo ───────────────────────────────────────────

    private void checkGameOver() {
        if (gameOver) return;

        // 1. Algum lado ficou sem peças
        if (tabuleiro.isOver()) {
            gameOver = true;
            if (gameOverListener != null) {
                gameOverListener.onGameOver(tabuleiro.getWhiteCount() > 0);
            }
            return;
        }

        // 2. Empate: exatamente uma dama branca + uma dama preta, sem capturas possíveis
        if (isDrawCondition()) {
            gameOver = true;
            if (gameOverListener != null) gameOverListener.onDraw();
            return;
        }

        // 3. Jogador atual sem jogadas → perde
        boolean currentTurn = turnManagement.whoseTurn();
        if (!temJogadas(currentTurn)) {
            gameOver = true;
            if (gameOverListener != null) {
                gameOverListener.onGameOver(!currentTurn); // outro lado vence
            }
        }
    }

    /**
     * Detecta condição de empate: somente uma dama de cada cor restante
     * e nenhum dos lados pode realizar captura.
     */
    private boolean isDrawCondition() {
        if (tabuleiro.getWhiteCount() != 1 || tabuleiro.getBlackCount() != 1) return false;

        boolean whiteKing = false, blackKing = false;
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                Position pos = new Position(i, j);
                if (tabuleiro.isEmpty(pos)) continue;
                if (tabuleiro.isWhite(pos) && tabuleiro.isDama(pos)) whiteKing = true;
                if (!tabuleiro.isWhite(pos) && tabuleiro.isDama(pos)) blackKing = true;
            }
        }
        if (!whiteKing || !blackKing) return false;

        return !moveManagement.teamHasCaptures(true)
                && !moveManagement.teamHasCaptures(false);
    }

    /**
     * Verifica se a cor indicada possui pelo menos uma jogada válida.
     *
     * BUG CORRIGIDO: versão original verificava apenas as brancas (hardcoded).
     * Agora o parâmetro determina qual lado é verificado.
     */
    private boolean temJogadas(boolean isWhite) {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                Position pos = new Position(i, j);
                if (!tabuleiro.isEmpty(pos) && tabuleiro.isWhite(pos) == isWhite) {
                    List<Node> moves = moveManagement.getMoves(
                            translator.getCharFromPosition(pos));
                    if (!moves.isEmpty()) return true;
                }
            }
        }
        return false;
    }

    // ── Dupla captura do jogador ──────────────────────────────────────────────

    private boolean verificarSePodeCapturar(Position clicked) {
        List<Node> allMoves = moveManagement.getAllMovesForPiece(
                translator.getCharFromPosition(clicked));
        List<Node> captureMoves = new ArrayList<>();

        for (Node m : allMoves) {
            if (moveManagement.isCapture(m)) captureMoves.add(m);
        }

        if (!captureMoves.isEmpty()) {
            origin    = clicked;
            movimentos = captureMoves;
            destacarMovimentos(movimentos);
            return true;
        }
        return false;
    }

    // ── Auxiliares de UI ─────────────────────────────────────────────────────

    private boolean isValidMove(Node move)   { return movimentos.contains(move); }
    private boolean isDoubleClick(Position p){ return !tabuleiro.isEmpty(p) && p.equals(origin); }

    private void destacarMovimentos(List<Node> mov)   { paintTabuleiro.showPossibleMoves(origin, mov); }
    private void cancelarDestaque(List<Node> mov)     { paintTabuleiro.unShowPossibleMoves(origin, mov); }

    public void paint(int i, int j) { paintTabuleiro.colorirCasa(i, j); }

    public void sincronizarView() {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                paintTabuleiro.atualizarCasa(i, j, tabuleiro.getType(i, j));
            }
        }
    }
}
