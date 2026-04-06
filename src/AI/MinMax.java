package AI;

import AI.Evaluation.MinMaxEvaluation;
import Model.Node;
import Model.Tabuleiro;

// Classe de implementação do MinMax
public class MinMax {

    private final MinMaxEvaluation evaluator;

    public MinMax(MinMaxEvaluation minMaxEvaluation) {
        this.evaluator = minMaxEvaluation;
    }

    public void MinMaxCheckersGame(Node root) {
        if (root.getChildren().isEmpty()) {
            Tabuleiro tabuleiro = new Tabuleiro();
            tabuleiro.setMatriz(root.getMatriz());
            root.setMinMax(evaluator.Evaluation(root, tabuleiro));
        }
        else {
            for (Node child : root.getChildren()) {
                if (child.getMinMax() == Integer.MIN_VALUE) {
                    MinMaxCheckersGame(child);
                }
            }

            if (root.isTurn()) {
                 root.setMinMax(evaluator.Minimo(root.getChildren()));
            }
            else {
                root.setMinMax(evaluator.Maximo(root.getChildren()));
            }
        }
    }


}

