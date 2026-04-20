package AI;


import AI.Evaluation.Evaluation;
import AI.Evaluation.PositionalEvaluation;
import AI.Evaluation.QuantityEvaluation;
import Engine.Translator;
import Model.Node;
import Model.Tabuleiro;


public class AI {


    private Tree tree;
    private Node root;
    private Tabuleiro tabuleiro;
    private int TREE_HEIGHT = 0;
    private Evaluation evaluation;
    Translator translator;

    public AI(Tabuleiro tabuleiro, Translator translator, LevelAI level) {
        this.tabuleiro = tabuleiro;
        this.translator = translator;
        levelConfig(level);

        switch (level) {
            case EASY -> evaluation = new QuantityEvaluation();
            case MEDIUM -> evaluation = new QuantityEvaluation();
            case HARD -> evaluation = new PositionalEvaluation(translator);
            case DEFAULT -> evaluation = null;
        }

        tree  = new Tree(tabuleiro.getTam(), evaluation, TREE_HEIGHT);
        root = new Node();
    }

    // Configuração de dificuldade da IA
    private void levelConfig(LevelAI level) {
        switch (level) {
            case LevelAI.EASY -> {
                TREE_HEIGHT = 8;
                evaluation = new QuantityEvaluation();
            }
            case LevelAI.MEDIUM -> {
                TREE_HEIGHT = 10;
                evaluation = new QuantityEvaluation();
            }
            case LevelAI.HARD -> {
                TREE_HEIGHT = 14;
                evaluation = new PositionalEvaluation(translator);
            }
        }
    }

    public void montarArvore(boolean isWhite) {
        root.clear();
        //long inicio = System.currentTimeMillis();

        if (evaluation != null) {
            tree.montarArvoreIA(root, 0, tabuleiro, isWhite,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        else {
            tree.RandomMove(tabuleiro, isWhite);
        }

//        long fim = System.currentTimeMillis();
//        tree.print();
//        System.out.println("Tempo: " + (fim - inicio) + "ms");
    }

    public Node getBestMove() {
        return tree.BestMove(root);
    }

    public Node getRandomMove(boolean isWhiteTurn) {
        return tree.RandomMove(tabuleiro, isWhiteTurn);
    }
}
