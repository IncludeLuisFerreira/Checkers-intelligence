package View;

import Engine.Translator;
import Model.*;

import java.awt.*;
import java.util.List;

/**
 * @author includeluisferreira
 * <p>
 * Classe PaintTabuleiro é responsável pela parte visual do tabuleiro,
 * manipulando cores e destaques das casas com base no estado lógico
 * fornecido por <b>LogicTabuleiro</b>.
 * <p>
 * Principais funções:
 * <p>
 * - Definir a coloração padrão das casas do tabuleiro.
 * - Destacar visualmente movimentos possíveis e capturas.
 * - Diferenciar casas de destino e peças que podem ser capturadas.
 * - Restaurar a coloração original após cancelar destaques.
 * <p>
 * //TODO
 *  Implementar Position e passar toda a lógica de jogadas possiveis,
 *  retornar List<Intpair> para essa classe e assim colorir
 */
public class PaintTabuleiro {

    private final CasaBotao[][] tabuleiroInterface;
    private final Tabuleiro tabuleiro;
    private final Translator translator;

    public final Color YELLOW = Color.YELLOW;
    public final Color GREEN = new Color(119, 149, 86);
    public final Color WHITE =new Color(235, 235, 208);

    public PaintTabuleiro(Tabuleiro tabuleiro, CasaBotao[][] tabuleiroInterface, Translator translator) {
        this.tabuleiroInterface = tabuleiroInterface;
        this.tabuleiro = tabuleiro;
        this.translator = translator;

    }

    public void setBg(Position pos, Color color) {
        if (!tabuleiro.isInvalidParam(pos))
            tabuleiroInterface[pos.getRow()][pos.getCol()].setBackground(color);
    }

    public void setBg(int i, int j, Color color) {
        if (!tabuleiro.isInvalidParam(i, j))
            tabuleiroInterface[i][j].setBackground(color);
    }

    public void colorirCasa(int i, int j) {
        if ((i + j) % 2 == 0) {setBg(i, j, WHITE);}
        else {setBg(i, j, GREEN);}
    }

    // Péssimo jeito de implementar vizualização de jogadas, vai causar um ‘bug’ mais tarde
    public void showPossibleMoves(Position from, List<Move> moves) {
        setBg(from, Color.YELLOW);
        for (Move move : moves) {
            Position pos = translator.getPositionFromMove(move).getLast();
            setBg(pos, Color.GRAY);
        }
    }

    public void unShowPossibleMoves(Position from, List<Move> moves) {
        setBg(from, GREEN);
        for (Move move : moves) {
            Position pos = translator.getPositionFromMove(move).getLast();
            setBg(pos, GREEN);
        }
    }


}
