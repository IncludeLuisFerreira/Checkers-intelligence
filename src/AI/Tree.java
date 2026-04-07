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
    private final int MAXHEIGHT = 12;       // Máximo que consegui no meu computador (8s: 3.207.202 filhos)
    private final int AVAREGEHEIGHT = 10;
    private int childrenCount = 0;

    public Tree(int TAM_TABULEIRO) {
        this.translator = new Translator(TAM_TABULEIRO);
    }

    public void montarArvoreIA(Node arvore, int profundidade, Tabuleiro tabuleiro, boolean isWhiteTurn) {
        ArrayList<Node> jogadasPossiveis = retornarJogadasPossiveis(tabuleiro, isWhiteTurn);

        if (profundidade == 10 || jogadasPossiveis.isEmpty()) return;

        for (Node jogada : jogadasPossiveis) {
            childrenCount++;        // Apenas para logs
            Tabuleiro tabuleiroClone = tabuleiro.clone();
            MoveManagement tempMoveManagement = new MoveManagement(tabuleiroClone, translator);
            tempMoveManagement.execMove(jogada, isWhiteTurn);

            // Fazer as podas

            jogada.setMatriz(tabuleiroClone);
            jogada.setTurn(isWhiteTurn);
            arvore.addChild(jogada);

           boolean nextTurn;
           if (tempMoveManagement.isCapture(jogada) &&
               tempMoveManagement.verifyDoubleCapture(jogada.getDest())) {
               nextTurn = isWhiteTurn;
           }
           else
               nextTurn = !isWhiteTurn;

            this.montarArvoreIA(jogada, profundidade + 1, tabuleiroClone, nextTurn);
        }


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



//    public static void main(String[] args) {
//        MorePiecesEvaluation morePieces = new MorePiecesEvaluation();
//        MinMax minMax = new MinMax(morePieces);
//        long startTime = System.currentTimeMillis();
//
//        Tree tree = new Tree(6, morePieces);
//        Node root = new Node();
//        tree.montarArvoreIA(root, 0, new Tabuleiro(), true);
//        tree.p();
//
//        long endTime = System.currentTimeMillis();
//
//        System.out.println("Tempo: " + (endTime - startTime) / 1000 + "s");
//        minMax.MinMaxCheckersGame(root, new Tabuleiro());
//
//    }

}
