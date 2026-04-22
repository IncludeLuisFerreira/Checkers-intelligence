package AI;

import AI.Evaluation.Evaluation;
import AI.Evaluation.OffensiveEvaluation;
import AI.Evaluation.PositionalEvaluation;
import AI.Evaluation.QuantityEvaluation;
import Engine.Translator;
import Model.Node;
import Model.Tabuleiro;

public class AI {

    private final Tree       tree;
    private final Node       root;
    private final Tabuleiro  tabuleiro;
    private final Evaluation evaluation;   // null = modo aleatório
    private final Translator translator;

    // ── Construtor para níveis fixos (EASY / MEDIUM / HARD / DEFAULT) ────────

    public AI(Tabuleiro tabuleiro, Translator translator, LevelAI level) {
        this.tabuleiro  = tabuleiro;
        this.translator = translator;

        // BUG CORRIGIDO: configuração feita em um único ponto.
        // O código original tinha dois switches que se sobrescreviam,
        // causando OffensiveEvaluation sempre retornar 0 em MEDIUM.
        int    height = resolveHeight(level);
        Evaluation eval = resolveEvaluation(level, translator);

        this.evaluation = eval;
        this.tree       = new Tree(tabuleiro.getTam(), eval, height);
        this.root       = new Node();
    }

    // ── Construtor para modo treino (configuração personalizada) ─────────────

    public AI(Tabuleiro tabuleiro, Translator translator, TrainingConfig config) {
        this.tabuleiro  = tabuleiro;
        this.translator = translator;
        this.evaluation = config.evaluation;          // null → aleatório
        this.tree       = new Tree(tabuleiro.getTam(), config.evaluation, config.treeHeight);
        this.root       = new Node();
    }

    // ── Configuração por nível ───────────────────────────────────────────────

    /** Retorna a altura correta para o nível. Mínimo 10 nós conforme requisito. */
    private static int resolveHeight(LevelAI level) {
        return switch (level) {
            case EASY   -> 10;   // mínimo exigido (era 8 — BUG CORRIGIDO)
            case MEDIUM -> 10;
            case HARD   -> 14;
            default     ->  0;   // DEFAULT: árvore não usada (random)
        };
    }

    /** Retorna o avaliador correto para o nível. Único ponto de atribuição. */
    private static Evaluation resolveEvaluation(LevelAI level, Translator translator) {
        return switch (level) {
            case EASY   -> new QuantityEvaluation();
            case MEDIUM -> new OffensiveEvaluation();   // era retornada mas nunca configurada — BUG CORRIGIDO
            case HARD   -> new PositionalEvaluation(translator);
            default     -> null;                        // DEFAULT: random
        };
    }

    // ── API pública ──────────────────────────────────────────────────────────

    /**
     * Monta a árvore MiniMax com poda alfa-beta a partir do estado atual.
     * Não faz nada se o avaliador for null (modo aleatório).
     *
     * @param iaIsWhite true se a IA joga com as brancas nesta rodada.
     */
    public void montarArvore(boolean iaIsWhite) {
        root.clear();
        if (evaluation != null) {
            tree.montarArvoreIA(root, 0, tabuleiro, iaIsWhite,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    /** Retorna o nó filho com melhor pontuação MiniMax. Null se árvore vazia. */
    public Node getBestMove() {
        return tree.BestMove(root);
    }

    /** Retorna um movimento aleatório válido para a cor indicada. */
    public Node getRandomMove(boolean iaIsWhite) {
        return tree.RandomMove(tabuleiro, iaIsWhite);
    }

    /** true se este AI usa MiniMax (avaliação não-nula). */
    public boolean usesMinimax() {
        return evaluation != null;
    }
}
