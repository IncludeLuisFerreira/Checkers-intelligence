package AI;


import AI.Evaluation.MinMaxEvaluation;
import Model.Node;
import Model.Tabuleiro;

public class AI {

    private Tree tree;
    private Node root;
    private Tabuleiro tabuleiro;

    public AI(Tabuleiro tabuleiro, MinMaxEvaluation evaluation) {
        this.tabuleiro = tabuleiro;
        tree  = new Tree(tabuleiro.getTam(), evaluation);
        root = new Node();
    }

    public void montarArvore(boolean isWhite) {
        root.clear();
        long inicio = System.currentTimeMillis();

        tree.montarArvoreIA(root, 0, tabuleiro, isWhite,
                Integer.MIN_VALUE, Integer.MAX_VALUE);

        long fim = System.currentTimeMillis();
        tree.print();
        System.out.println("Tempo: " + (fim - inicio) + "ms");
    }

    public Node getBestMove() {
        return tree.BestMove(root);
    }
}
