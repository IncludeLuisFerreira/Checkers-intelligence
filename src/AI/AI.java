package AI;


import AI.Evaluation.MinMaxEvaluation;
import Model.Node;
import Model.Tabuleiro;

public class AI {

    private Tree tree;
    private Node root;
    private MinMax minMax;
    private Tabuleiro tabuleiro;

    public AI(Tabuleiro tabuleiro, MinMaxEvaluation evaluation) {
        this.tabuleiro = tabuleiro;
        minMax = new MinMax(evaluation);
        tree  = new Tree(tabuleiro.getTam());
        root = new Node();
    }

    public void montarArvore(boolean isWhite) {
        root.clear();
        tree.montarArvoreIA(root, 0,  tabuleiro, isWhite);
        minMax.MinMaxCheckersGame(root, tabuleiro);
    }

    public Node getBestMove() {
        return tree.BestMove(root);
    }
}
