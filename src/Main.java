import Engine.Engine;
import Model.Tabuleiro;
import View.CasaBotao;
import View.Interface;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Tabuleiro tabuleiro = new Tabuleiro();
            CasaBotao[][] botoes = new CasaBotao[6][6];
            initBotoes(botoes);

            Engine engine = new Engine(tabuleiro, botoes);
            Interface ui = new Interface(botoes, engine);
            engine.setGameOverListener(ui::declararVencedor);
            engine.sincronizarView();

        });
    }

    private static void initBotoes(CasaBotao[][] botoes) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                botoes[i][j] = new CasaBotao();
            }
        }
    }
}

