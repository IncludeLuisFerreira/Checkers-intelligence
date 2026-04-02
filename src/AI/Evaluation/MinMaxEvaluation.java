package AI.Evaluation;

import Model.Node;
import Model.Tabuleiro;

import java.util.ArrayList;

public abstract class MinMaxEvaluation {


    public MinMaxEvaluation() {
    }

    // Função abstrata para a implementação de múltiplas heurísticas
    public abstract int Evaluation(Node node, Tabuleiro tabuleiro);

    // E se eu conseguir organizar o arraylist por tamanho de minMax? Min no index 0 e Max no index size-1
    public int Minimo(ArrayList<Node> children) {
        int min = Integer.MAX_VALUE;

        for (Node node : children) {
            int val = node.getMinMax();
            if (min > val) min = val;
        }
        return min;
    }

    public int Maximo(ArrayList<Node> children) {
        int max = Integer.MIN_VALUE;

        for (Node node : children) {
            int val = node.getMinMax();
            if (max < val) max = val;
        }

        return max;
    }
}
