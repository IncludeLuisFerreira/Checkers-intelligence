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

    public static final int MAXHEIGHT = 18;       // No final da partida fica muito demorado
    public static final int AVAREGEHEIGHT = 14;


    private final int height;
    private final Translator translator;
    private int childrenCount = 0;
    private final MinMaxEvaluation evaluator;

    public Tree(int TAM_TABULEIRO, MinMaxEvaluation evaluator, int HEIGHT) {
        this.translator = new Translator(TAM_TABULEIRO);
        this.evaluator = evaluator;
        this.height = HEIGHT;
    }

    /**
      Montagem da árvore otimizada com podas alfa e beta
     */
    public int montarArvoreIA(Node arvore, int profundidade, Tabuleiro tabuleiro,
                               boolean isWhiteTurn, int alpha, int beta) {
        ArrayList<Node> jogadasPossiveis = retornarJogadasPossiveis(tabuleiro, isWhiteTurn);

        if (profundidade == height || jogadasPossiveis.isEmpty()) {
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

            // So preciso armazenar as jogadas do primeiro nivel para a IA executar
            // Pode otimizar fazendo retornar a jogada?
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

    /** Função que verifica todas as jogadas possíveis de um determinado time em um determinado contexto de
     * tabuleiro. Em caso de capturas, a função retorna uma lista que apresenta apenas capturas. <b>Regra de captura
     * obrigatória </b>
     *
     * @param tabuleiro um clone do tabuleiro
     * @param isWhiteTurn True se é turno das brancas; false se e turno das pretas
     * @return ArrayList de possíveis jogadas
     */
    public ArrayList<Node> retornarJogadasPossiveis(Tabuleiro tabuleiro, boolean isWhiteTurn) {
        ArrayList<Node> jogadasPossiveis = new ArrayList<>();
        ArrayList<Node> capturas = new ArrayList<>();
        MoveManagement tempMoveManagement = new MoveManagement(tabuleiro, translator);

        if (tabuleiro.isOver())
            return jogadasPossiveis;

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {

                // pensando aqui, mesmo que eu achei uma captura, essa funcao vai explorar todas as jogadas.
                // Nao sei se faz muita diferenca, mas pode melhorar o processamento
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

    // Escolhe o nó com maior pontuação, será provavelmente integrado ao montar arvore
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

    // Pega as jogadas possíveis e escolhe um de forma aleatória
    public Node RandomMove(Node root, Tabuleiro tabuleiro, boolean isWhiteTurn) {
        ArrayList<Node> jogadas = retornarJogadasPossiveis(tabuleiro,isWhiteTurn);

        Random random = new Random(System.nanoTime());
        int index = random.nextInt(jogadas.size());

        return jogadas.get(index);
    }


    public static void main(String[] args) {
        MorePiecesEvaluation morePieces = new MorePiecesEvaluation();
        MinMax minMax = new MinMax(morePieces);
        long startTime = System.currentTimeMillis();
        int INFINITO = Integer.MAX_VALUE;

        Tree tree = new Tree(6, morePieces, 16);
        Node root = new Node();

        tree.montarArvoreIA(root, 0, new Tabuleiro(), true, -INFINITO, INFINITO);
        System.out.println("Melhor movimento: " + tree.BestMove(root).toString());

        long endTime = System.currentTimeMillis();


        System.out.println("Tempo: " + (endTime - startTime) + "ms");
        tree.print();


        // Teste de tempo do RandomMove

        startTime = System.currentTimeMillis();

        root.clear();
        root = tree.RandomMove(root, new Tabuleiro(), true);

        endTime = System.currentTimeMillis();

        System.out.println("Melhor movimento: " + root.toString());
        System.out.println("Tempo: " + (endTime - startTime)  + "ms");
        root.clear();
    }

}
