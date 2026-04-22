import AI.LevelAI;
import AI.TrainingConfig;
import Engine.Engine;
import Engine.GameOverListener;
import Model.Tabuleiro;
import View.CasaBotao;
import View.Interface;
import View.MenuDificuldadeView;
import View.TreinoConfigView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::reiniciar);
    }

    /** Relança o fluxo completo: menu → (config treino) → jogo. */
    public static void reiniciar() {

        // 1. Seleção de dificuldade
        MenuDificuldadeView menu  = new MenuDificuldadeView();
        LevelAI             level = menu.aguardarEscolha();

        // 2. Se Treino, abre configurador personalizado
        TrainingConfig trainingConfig = null;
        if (level == LevelAI.DEFAULT) {
            TreinoConfigView configView = new TreinoConfigView();
            trainingConfig = configView.aguardarConfig();
        }

        // 3. Montagem do jogo
        Tabuleiro    tabuleiro = new Tabuleiro();
        CasaBotao[][] botoes   = new CasaBotao[6][6];
        initBotoes(botoes, level);

        Engine    engine = new Engine(tabuleiro, botoes, level, trainingConfig);
        Interface ui     = new Interface(botoes, engine, level, Main::reiniciar);

        // 4. Listener de fim de jogo — suporta vitória E empate
        engine.setGameOverListener(new GameOverListener() {
            @Override
            public void onGameOver(boolean whiteWon) {
                SwingUtilities.invokeLater(() -> ui.declararVencedor(whiteWon));
            }
            @Override
            public void onDraw() {
                SwingUtilities.invokeLater(ui::declararEmpate);
            }
        });

        engine.sincronizarView();

        // 5. Se jogador escolheu pretas, IA (brancas) abre o jogo
        engine.iniciar();
    }

    private static void initBotoes(CasaBotao[][] botoes, LevelAI level) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                botoes[i][j] = new CasaBotao(level);
            }
        }
    }
}
