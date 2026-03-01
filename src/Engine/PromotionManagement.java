package Engine;

import Model.Position;
import Model.Tabuleiro;

public class PromotionManagement {

    private final Tabuleiro tabuleiro;

    public PromotionManagement(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public void canBePromoted(Position pos) {
        if (tabuleiro.isDama(pos)) return;

        if (tabuleiro.isWhite(pos) && pos.getRow() == 0) {
            tabuleiro.setPos(pos, Tabuleiro.WHITEKING);
        }
        else if (!tabuleiro.isWhite(pos) && pos.getRow() == 5) {
            tabuleiro.setPos(pos, Tabuleiro.BLACKKING);
        }
    }
}
