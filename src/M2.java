import AI.LevelAI;
import Engine.Engine;
import Model.Tabuleiro;
import View.CasaBotao;
import View.Interface;
import View.MenuDificuldadeView;

import javax.swing.*;

public class M2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // — Menu temático de seleção de dificuldade —
            MenuDificuldadeView menu = new MenuDificuldadeView();
            LevelAI level = menu.aguardarEscolha();

            // — Inicialização do jogo —
            Tabuleiro tabuleiro = new Tabuleiro();
            CasaBotao[][] botoes = new CasaBotao[6][6];
            initBotoes(botoes, level);

            Engine engine = new Engine(tabuleiro, botoes, level);
            Interface ui = new Interface(botoes, engine);
            engine.setGameOverListener(ui::declararVencedor);
            engine.sincronizarView();
        });
    }

    private static void initBotoes(CasaBotao[][] botoes, LevelAI level) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                botoes[i][j] = new CasaBotao(level);
            }
        }
    }
}

