package Engine;

/**
 * Listener de fim de jogo.
 *
 * Implementado por {@link View.Interface} e ligado ao {@link Engine}.
 */
public interface GameOverListener {

    /** Chamado quando um lado vence por captura de todas as peças ou travamento. */
    void onGameOver(boolean whiteWon);

    /** Chamado quando o empate é detectado (duas damas, sem captura possível). */
    void onDraw();
}
