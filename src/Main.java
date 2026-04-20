import AI.LevelAI;
import Engine.Engine;
import Model.Tabuleiro;
import View.CasaBotao;
import View.Interface;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            String[] opcoes = {"Fácil", "Médio", "Difícil", "Treinamento"};

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Escolha a dificuldade:",
                    "Nível da IA",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            LevelAI level = switch (escolha) {
                case 0 -> LevelAI.EASY;
                case 1 -> LevelAI.MEDIUM;
                case 2 -> LevelAI.HARD;
                case 3 -> LevelAI.DEFAULT; // fallback
                default -> LevelAI.DEFAULT;
            };

            Tabuleiro tabuleiro = new Tabuleiro();
            CasaBotao[][] botoes = new CasaBotao[6][6];
            initBotoes(botoes, level);

            Engine engine = new Engine(tabuleiro, botoes, level);
            Interface ui = new Interface(botoes, engine);
            engine.setGameOverListener(ui::declararVencedor);
            engine.sincronizarView();

        });
    }

    private static void initBotoes(CasaBotao[][] botoes,LevelAI level) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                botoes[i][j] = new CasaBotao(level);
            }
        }
    }
}

