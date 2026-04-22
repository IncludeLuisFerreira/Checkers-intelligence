package View;

import AI.LevelAI;
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

    public Color DARK_SQUARE;
    public Color LIGHT_SQUARE;
    public Color SELECTED;
    public Color MOVE_HINT;

    // DEFAULT

    public void configurarCoresDefault() {
       this.DARK_SQUARE = new Color(119, 149, 86);
        this.LIGHT_SQUARE = new Color(235, 235, 208);
        this.SELECTED = Color.yellow;
        this.MOVE_HINT = Color.gray;
    }
    // HATI
    public void configurarCoresHati() {
        // Tabuleiro Natural Frio (Madeira Acinzentada / Prata)
        this.DARK_SQUARE = new Color(86, 99, 106);    // Cinza ardósia / Freixo escuro
        this.LIGHT_SQUARE = new Color(218, 223, 225); // Bétula clara (levemente prateada)

        // Destaques (Azul acinzentado muito suave para não parecer neon)
        this.SELECTED = new Color(120, 160, 180);
        this.MOVE_HINT = new Color(120, 160, 180, 100);
    }

    // SKOOL
    public void configurarCoresSkoll() {
        // Tabuleiro Natural Quente (Cerejeira / Mogno Clássico)
        this.DARK_SQUARE = new Color(166, 82, 45);    // Cerejeira avermelhada (Cherry wood)
        this.LIGHT_SQUARE = new Color(243, 223, 184); // Buxo quente (Boxwood)

        // Destaques (Cobre / Ouro envelhecido suave)
        this.SELECTED = new Color(210, 140, 70);
        this.MOVE_HINT = new Color(239, 139, 139, 158);
    }

    // FENRIR
    public void configurarCoresFenrir() {
        // Casas Escuras: Azul Ardósia Profundo (Baseado na pedra do tabuleiro da imagem)
        // Uma cor de pedra natural, mas com um fundo frio.
        this.DARK_SQUARE = new Color(0x313241);

        // Casas Claras: Cinza Granito (Para dar o contraste de pedra polida)
        // Mantém o aspecto de "material real" e não apenas uma cor chapada.
        this.LIGHT_SQUARE = new Color(0x827c97);

        // Selected: Roxo Elétrico / Púrpura (Cor exata dos olhos e runas do Fenrir na imagem)
        // É o destaque que traz a "magia" para o tabuleiro de pedra.
        this.SELECTED = new Color(145, 70, 255);

        // Move Hint: O mesmo Roxo, mas com transparência para parecer um brilho sobre a pedra.
        this.MOVE_HINT = new Color(145, 70, 255, 90);
    }


    public PaintTabuleiro(Tabuleiro tabuleiro, CasaBotao[][] tabuleiroInterface, Translator translator, LevelAI level) {
        this.tabuleiroInterface = tabuleiroInterface;
        this.tabuleiro = tabuleiro;
        this.translator = translator;

//        System.out.println(level);

        switch (level) {
            case EASY: {
                configurarCoresHati();
                break;
            }
            case MEDIUM: {
                configurarCoresSkoll();
                break;
            }
            case HARD: {
                configurarCoresFenrir();
                break;
            }
            case DEFAULT: {
                configurarCoresDefault();
                break;
            }
        }

    }

    public void setBg(Position pos, Color color) {
        if (!tabuleiro.isInvalidParam(pos))
            tabuleiroInterface[pos.getRow()][pos.getCol()].setBackground(color);
    }

    public void setBg(int i, int j, Color color) {
        if (!tabuleiro.isInvalidParam(i, j)) {
            tabuleiroInterface[i][j].setBackground(color);
        }
    }

    public void colorirCasa(int i, int j) {
        if ((i + j) % 2 == 0) {setBg(i, j, LIGHT_SQUARE);}
        else {setBg(i, j, DARK_SQUARE);}
    }

    // Péssimo jeito de implementar vizualização de jogadas, vai causar um ‘bug’ mais tarde
    public void showPossibleMoves(Position from, List<Node> moves) {
        setBg(from, SELECTED);
        if (moves == null || moves.isEmpty()) return;
        for (Node move : moves) {
            Position pos = translator.getPositionFromMove(move).getLast();
            setBg(pos, MOVE_HINT);
        }
    }

    public void unShowPossibleMoves(Position from, List<Node> moves) {
        setBg(from, DARK_SQUARE);
        if (moves == null || moves.isEmpty()) return;
        for (Node move : moves) {
            Position pos = translator.getPositionFromMove(move).getLast();
            setBg(pos, DARK_SQUARE);
        }
    }

    public void atualizarCasa(int i, int j, char piece) {
        tabuleiroInterface[i][j].setTipoPeca(piece);
    }


}
