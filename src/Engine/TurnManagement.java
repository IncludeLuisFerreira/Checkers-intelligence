package Engine;
/**
 * Regras dd classe TurnManagement:
 *<p>
 *     Um turno deve mudar quando não houve captura ou quando não tem como fazer uma nova captura
 *</p>
 */
public class TurnManagement {

    private boolean turn = true;

    /*======================= LÓGICA SIMPLES DE TURNO =======================*/
    // True -> white; False -> black
    public boolean whoseTurn() {
        return turn;
    }

    public void decide(boolean haveCaptured, boolean canCapture) {
        if (!haveCaptured || !canCapture) {
            changeTurn();
        }
    }

    private void changeTurn() {
        turn = !turn;
    }
}
