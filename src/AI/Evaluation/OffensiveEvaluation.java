package AI.Evaluation;

import Model.Position;
import Model.Tabuleiro;

/**
 * Heurística Ofensiva.
 *
 * Estratégia: pressionar o adversário avançando peças rapidamente
 * e priorizando a promoção a dama.
 *
 * Fatores avaliados:
 *  1. Material       — valor base de cada peça (damas valem mais)
 *  2. Avanço         — bônus por cada fileira avançada em direção ao lado inimigo
 *  3. Pré-promoção   — bônus extra para peças na penúltima fileira
 *
 * Convenção: positivo = vantagem das pretas (MAX / IA).
 */
public class OffensiveEvaluation extends Evaluation {

    private static final int PIECE_VALUE      = 10;  // valor base de peça simples
    private static final int KING_VALUE       = 40;  // valor base de dama
    private static final int ADVANCE_WEIGHT   =  3;  // bônus por fileira avançada
    private static final int PROMO_BONUS      =  8;  // bônus penúltima fileira (pré-dama)

    @Override
    public int avaliation(Tabuleiro tabuleiro) {
        int score = 0;
        int tam   = tabuleiro.getTam();

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                Position pos = new Position(i, j);
                if (tabuleiro.isEmpty(pos)) continue;

                boolean isWhite = tabuleiro.isWhite(pos);
                boolean isKing  = tabuleiro.isDama(pos);

                int material = isKing ? KING_VALUE : PIECE_VALUE;

                // Pretas avançam de i=0 → i=tam-1 (i cresce = mais avançado)
                // Brancas avançam de i=tam-1 → i=0 (i decresce = mais avançado)
                int advancement = isKing ? 0
                        : (isWhite ? (tam - 1 - i) : i);

                // Bônus extra para peça na penúltima fileira (prestes a virar dama)
                boolean nearPromotion = !isKing &&
                        ((isWhite && i == 1) || (!isWhite && i == tam - 2));
                int promoBonus = nearPromotion ? PROMO_BONUS : 0;

                int pieceScore = material + advancement * ADVANCE_WEIGHT + promoBonus;

                // Pretas = MAX (positivo); Brancas = MIN (negativo)
                score += isWhite ? -pieceScore : pieceScore;
            }
        }
        return score;
    }
}
