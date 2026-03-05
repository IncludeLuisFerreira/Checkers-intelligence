package AI;

import java.util.ArrayList;
import java.util.List;

import Model.Node;
import Model.Position;
import Model.Tabuleiro;
import Engine.MoveManagement;
import Engine.Translator;

public class Tree {

    private final Translator translator;

    public Tree(int TAM_TABULEIRO) {
        this.translator = new Translator(TAM_TABULEIRO);
    }

    public void montarArvoreIA(Node arvore, int profundidade, Tabuleiro tabuleiro, boolean isWhiteTurn) {
        if (profundidade == 4) return;

        ArrayList<Node> jogadasPossiveis = retornarJogadasPossiveis(tabuleiro, isWhiteTurn);
        for (Node jogada : jogadasPossiveis) {
            Tabuleiro tabuleiroClone = tabuleiro.clone();
            MoveManagement tempMoveManagement = new MoveManagement(tabuleiroClone, translator);
            tempMoveManagement.execMove(jogada);


            jogada.setMatriz(tabuleiroClone);
            jogada.setTurn(isWhiteTurn);
            arvore.addChild(jogada);

            if (tempMoveManagement.isCapture(jogada)) {
                if (!tempMoveManagement.verifyDoubleCapture(jogada.getDest()))
                    isWhiteTurn = !isWhiteTurn;
            }

            this.montarArvoreIA(jogada, profundidade + 1, tabuleiroClone, !isWhiteTurn);
        }


    }

    public ArrayList<Node> retornarJogadasPossiveis(Tabuleiro tabuleiro, boolean isWhiteTurn) {
        ArrayList<Node> jogadasPossiveis = new ArrayList<>();
        MoveManagement tempMoveManagement = new MoveManagement(tabuleiro, translator);

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {

                if (!tabuleiro.isEmpty(new Position(i, j)) && tabuleiro.isWhite(new Position(i, j)) == isWhiteTurn)  {
                    jogadasPossiveis.addAll(tempMoveManagement.getMoves(
                            translator.getCharFromPosition(new Position(i, j))
                    ));
                }
            }
        }
        return jogadasPossiveis;
    }



    public static void main(String[] args) {
        Tree tree = new Tree(6);
        Node root = new Node();
        tree.montarArvoreIA(root, 0, new Tabuleiro(), true);
    }

}
