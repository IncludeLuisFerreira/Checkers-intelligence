package Engine;

@FunctionalInterface
public interface GameOverListener {
    void onGameOver(boolean isWhiteWinner);
}
