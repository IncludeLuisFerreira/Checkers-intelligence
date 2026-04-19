package AI.Evaluation;

import Model.Position;
import Model.Tabuleiro;

public class QuantityEvaluation extends Evaluation {

    private final int SIMPLES_PIECES = 10;
    private final int KING = 50;

    @Override
    public int avaliation(Tabuleiro tabuleiro) {
        int whiteCount = 0;
        int blackCount = 0;

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {
                int sum = SIMPLES_PIECES;
                Position pos = new Position(i, j);

                if ( tabuleiro.isEmpty(pos)) continue;

                if (tabuleiro.isDama(pos)) sum = KING;

                if (tabuleiro.isWhite(pos))  {
                    whiteCount += sum;
                }
                else  {
                    blackCount += sum;
                }
            }
        }

        // Max value = 6; Min value = -6
        return blackCount - whiteCount;
    }
}
