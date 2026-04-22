package Model;

public class Tabuleiro implements Cloneable {

    private char[][] matriz;
    private final int TAMANHO = 6;
    private short white = 6;
    private short black = 6;

    public static final char EMPTY      = '0';
    public static final char WHITEPIECE = '1';
    public static final char BLACKPIECE = '2';
    public static final char WHITEKING  = '3';
    public static final char BLACKKING  = '4';

    public Tabuleiro() {
        this.matriz = new char[TAMANHO][TAMANHO];
        inicializar();
    }

    private void inicializar() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {
                    if      (i < 2) matriz[i][j] = BLACKPIECE;
                    else if (i > 3) matriz[i][j] = WHITEPIECE;
                    else            matriz[i][j] = EMPTY;
                } else {
                    matriz[i][j] = EMPTY;
                }
            }
        }
    }

    /*======================= CLONE =======================*/

    @Override
    public Tabuleiro clone() {
        try {
            Tabuleiro clone = (Tabuleiro) super.clone();
            clone.matriz = new char[TAMANHO][];
            for (int i = 0; i < TAMANHO; i++) {
                clone.matriz[i] = this.matriz[i].clone();
            }
            clone.black = this.black;
            clone.white = this.white;
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /*======================= CONSULTAS =======================*/

    public boolean isDama(Position pos) {
        char type = matriz[pos.getRow()][pos.getCol()];
        return type == WHITEKING || type == BLACKKING;
    }

    public boolean isDiagonal(Position from, Position to) {
        return (from.getRow() + from.getCol()) == (to.getRow() + to.getCol())
                || (from.getRow() - from.getCol()) == (to.getRow() - to.getCol());
    }

    public boolean isEmpty(Position pos) {
        if (isInvalidParam(pos)) return false;
        return matriz[pos.getRow()][pos.getCol()] == EMPTY;
    }

    public boolean isInvalidParam(Position pos) {
        return pos.getRow() < 0 || pos.getRow() > 5
                || pos.getCol() < 0 || pos.getCol() > 5;
    }

    public boolean isInvalidParam(int r, int c) {
        return r < 0 || r > 5 || c < 0 || c > 5;
    }

    public char getType(Position pos) {
        if (isInvalidParam(pos)) return '#';
        return matriz[pos.getRow()][pos.getCol()];
    }

    public char getType(int r, int c) {
        if (isInvalidParam(r, c)) return '#';
        return matriz[r][c];
    }

    public boolean isEnemy(Position piece, Position otherPiece) {
        if (isInvalidParam(otherPiece)) return false;
        char t1 = matriz[piece.getRow()][piece.getCol()];
        char t2 = matriz[otherPiece.getRow()][otherPiece.getCol()];
        if (t2 == t1) return false;
        if ("13".indexOf(t1) >= 0 && "13".indexOf(t2) >= 0) return false;
        return !("24".indexOf(t1) >= 0 && "24".indexOf(t2) >= 0);
    }

    public boolean isWhite(Position pos) {
        if (isInvalidParam(pos)) return false;
        return "13".indexOf(matriz[pos.getRow()][pos.getCol()]) >= 0;
    }

    /*======================= MUTAÇÕES =======================*/

    public void capture(boolean isWhiteTurn) {
        if (isWhiteTurn  && black > 0) black--;
        else if (!isWhiteTurn && white > 0) white--;
    }

    public void setPos(Position pos, char type) {
        matriz[pos.getRow()][pos.getCol()] = type;
    }

    public char getPos(Position pos) {
        return matriz[pos.getRow()][pos.getCol()];
    }

    /*======================= GETTERS / SETTERS =======================*/

    public int     getTam()          { return TAMANHO; }
    public char[][] getMatriz()      { return matriz; }
    public void    setMatriz(char[][] m) { this.matriz = m; }
    public int     getWhiteCount()   { return white; }
    public int     getBlackCount()   { return black; }   // ADICIONADO para detecção de empate

    public void incrementWhite() { white++; }

    /*======================= FIM DE JOGO =======================*/

    /** Retorna true se algum dos lados ficou sem peças. */
    public boolean isOver() {
        return black == 0 || white == 0;
    }

    /*======================= CARGA DE CONFIGURAÇÃO =======================*/

    public void carregarConfiguracao(char[][] novaMatriz) {
        this.white = 0;
        this.black = 0;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                this.matriz[i][j] = novaMatriz[i][j];
                char c = novaMatriz[i][j];
                if      (c == WHITEPIECE || c == WHITEKING) white++;
                else if (c == BLACKPIECE || c == BLACKKING) black++;
            }
        }
    }
}
