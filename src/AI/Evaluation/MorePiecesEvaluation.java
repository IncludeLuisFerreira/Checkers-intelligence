package AI.Evaluation;

import Model.Node;
import Model.Position;
import Model.Tabuleiro;

public class MorePiecesEvaluation extends MinMaxEvaluation {


    // Eu clono o tabuleiro que ja vem com a contagem de pecas, como eu melhoro essa funcao?
    @Override
    public int Evaluation(Node node, Tabuleiro tabuleiro) {
        int whiteCount = 0;
        int blackCount = 0;

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {
                int sum = 10;
                Position pos = new Position(i, j);

                if ( tabuleiro.isEmpty(pos)) continue;

                if (tabuleiro.isDama(pos)) sum = 50;

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
