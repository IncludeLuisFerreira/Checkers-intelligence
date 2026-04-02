package AI.Evaluation;

import Model.Node;
import Model.Position;
import Model.Tabuleiro;

public class MorePiecesEvaluation extends MinMaxEvaluation {


    @Override
    public int Evaluation(Node node, Tabuleiro tabuleiro) {
        int whiteCount = 0;
        int blackCount = 0;

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {
                Position pos = new Position(i, j);

                if ( tabuleiro.isEmpty(pos)) continue;

                if (tabuleiro.isWhite(pos)) whiteCount++;
                else blackCount++;
            }
        }

        // Max value = 6; Min value = -6
        return blackCount - whiteCount;
    }

}
