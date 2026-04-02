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

    public void MinMaxCheckersGame(Node root, Tabuleiro tabuleiro) {
        if (root.getChildren().isEmpty()) {
            root.setMinMax(evaluator.Evaluation(root, tabuleiro));
        }
        else {
            for (int i = 0; i < root.getChildren().size(); i++) {
                Node child = root.getChildren().get(i);
                if (child.getMinMax() == Integer.MIN_VALUE) {
                    MinMaxCheckersGame(child, tabuleiro);
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

