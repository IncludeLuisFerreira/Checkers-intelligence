package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AI.Evaluation.MinMaxEvaluation;
import AI.Evaluation.MorePiecesEvaluation;
import Model.Node;
import Model.Position;
import Model.Tabuleiro;
import Engine.MoveManagement;
import Engine.Translator;

public class Tree {

    private final Translator translator;
    private final int MAXHEIGHT = 16;       // Máximo que consegui no meu computador (8s: 3.207.202 filhos)
    private final int AVAREGEHEIGHT = 12;
    private int childrenCount = 0;
    private final MinMaxEvaluation evaluator;

    public Tree(int TAM_TABULEIRO, MinMaxEvaluation evaluator) {
        this.translator = new Translator(TAM_TABULEIRO);
        this.evaluator = evaluator;
    }

    /**
      Montagem da árvore otimizada com podas alfa e beta
     */
    public int montarArvoreIA(Node arvore, int profundidade, Tabuleiro tabuleiro,
                               boolean isWhiteTurn, int alpha, int beta) {
        ArrayList<Node> jogadasPossiveis = retornarJogadasPossiveis(tabuleiro, isWhiteTurn);

        if (profundidade == MAXHEIGHT || jogadasPossiveis.isEmpty()) {
            int score = evaluator.Evaluation(arvore, tabuleiro);
            arvore.setMinMax(score);
            return score;
        }

        boolean isMaximizing = !isWhiteTurn;
        int bestValue = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Node jogada : jogadasPossiveis) {
            childrenCount++;

            Tabuleiro tabuleiroClone = tabuleiro.clone();
            MoveManagement tempMoveManagement = new MoveManagement(tabuleiroClone, translator);
            tempMoveManagement.execMove(jogada, isWhiteTurn);

            boolean nextTurn = false;

            // Se NAO for captura e captura multipla ele troca de turn
            if (!(tempMoveManagement.isCapture(jogada) &&
                tempMoveManagement.verifyDoubleCapture(jogada.getDest()))) {
                nextTurn = !isWhiteTurn;
            }

            if (profundidade == 0) {
                jogada.setMatriz(tabuleiroClone);
                jogada.setTurn(isWhiteTurn);
                arvore.addChild(jogada);
            }

            int childValue = montarArvoreIA (
                    jogada, profundidade + 1, tabuleiroClone,
                    nextTurn, alpha, beta
            );
            jogada.setMinMax(childValue);

            if (isMaximizing) {
                if (childValue > bestValue) bestValue = childValue;
                if (bestValue > alpha) alpha = bestValue;
            } else {
                if (childValue < bestValue) bestValue = childValue;
                if (bestValue < beta) beta = bestValue;
            }

            if (beta <= alpha)
                break;
        }

        arvore.setMinMax(bestValue);
        return bestValue;
    }

    public ArrayList<Node> retornarJogadasPossiveis(Tabuleiro tabuleiro, boolean isWhiteTurn) {
        ArrayList<Node> jogadasPossiveis = new ArrayList<>();
        ArrayList<Node> capturas = new ArrayList<>();
        MoveManagement tempMoveManagement = new MoveManagement(tabuleiro, translator);

        if (tabuleiro.isOver())
            return jogadasPossiveis;

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {

                // Acho que tá errado, a função não ve quando o time oposto está vazio
                if (!tabuleiro.isEmpty(new Position(i, j)) && tabuleiro.isWhite(new Position(i, j)) == isWhiteTurn)  {
                    jogadasPossiveis.addAll(tempMoveManagement.getMoves(
                            translator.getCharFromPosition(new Position(i, j))
                    ));
                }
            }
        }

        for (Node jogada : jogadasPossiveis) {
            if (tempMoveManagement.isCapture(jogada))
                capturas.add(jogada);
        }

        return capturas.isEmpty() ? jogadasPossiveis : capturas;
    }

    public void print() {
        System.out.println(childrenCount);
    }

    // Escolhe o nó com maior pontuação
    public Node BestMove(Node root) {
        if (root.getChildren().isEmpty()) return null;

        Node move = root.getChildren().getFirst();
        int value = move.getMinMax();

        for (Node child : root.getChildren()) {
            if (value < child.getMinMax()) {
                move = child;
                value = child.getMinMax();
            }
        }

        return move;
    }

    public Node RandomMove(Node root) {
        Random random = new Random();
        int randomNumber = random.nextInt(root.getChildren().size());

        return root.getChildren().get(randomNumber);
    }



    public static void main(String[] args) {
        MorePiecesEvaluation morePieces = new MorePiecesEvaluation();
        MinMax minMax = new MinMax(morePieces);
        long startTime = System.currentTimeMillis();

        int INFINITO = Integer.MAX_VALUE;

        Tree tree = new Tree(6, morePieces);
        Node root = new Node();
        tree.montarArvoreIA(root, 0, new Tabuleiro(), true, -INFINITO, INFINITO);

        long endTime = System.currentTimeMillis();

        System.out.println("Tempo: " + (endTime - startTime) / 1000 + "s");
        tree.print();

    }

}
