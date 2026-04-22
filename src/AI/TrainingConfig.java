package AI;

import AI.Evaluation.Evaluation;

/**
 * Configurações personalizadas do modo Treino.
 * Passada do menu de configuração até Engine/AI.
 */
public class TrainingConfig {

    /** true = jogador usa brancas; false = jogador usa pretas. */
    public final boolean playerIsWhite;

    /** Altura real da árvore (mapeada do slider 1-9). */
    public final int treeHeight;

    /**
     * Avaliador escolhido pelo usuário.
     * null → modo aleatório (sem minimax).
     */
    public final Evaluation evaluation;

    /** Nome legível da heurística selecionada (para log/debug). */
    public final String heuristicName;

    public TrainingConfig(boolean playerIsWhite,
                          int treeHeight,
                          Evaluation evaluation,
                          String heuristicName) {
        this.playerIsWhite  = playerIsWhite;
        this.treeHeight     = treeHeight;
        this.evaluation     = evaluation;
        this.heuristicName  = heuristicName;
    }

    /**
     * Converte o nível 1-9 escolhido pelo usuário na altura
     * real da árvore MiniMax (profundidade × 2).
     *
     * <pre>
     *  1 →  2  |  4 →  8  |  7 → 14
     *  2 →  4  |  5 → 10  |  8 → 16
     *  3 →  6  |  6 → 12  |  9 → 18
     * </pre>
     */
    public static int levelToHeight(int level) {
        return level * 2;
    }

    /** Rótulos descritivos para cada nível de dificuldade (índice 1-9). */
    public static final String[] LEVEL_LABELS = {
            "",              // 0 - não usado
            "Iniciante",     // 1
            "Básico",        // 2
            "Fácil",         // 3
            "Moderado",      // 4
            "Médio",         // 5
            "Avançado",      // 6
            "Difícil",       // 7
            "Muito Difícil", // 8
            "Máximo"         // 9
    };
}
