/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Douglas
 */
public class Tabuleiro implements Cloneable {

    private char[][] matriz;
    private final boolean[][] canEat;
    private final int TAMANHO = 6;

    public static final char EMPTY = '0';
    public static final char WHITEPIECE = '1';
    public static final char BLACKPIECE = '2';
    public static final char WHITEKING = '3';
    public static final char BLACKKING = '4';

    public Tabuleiro() {
        this.matriz = new char[TAMANHO][TAMANHO];
        this.canEat = new boolean[TAMANHO][TAMANHO];
        inicializar();
    }

    private void inicializar() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 2) {
                        matriz[i][j] = BLACKPIECE; // Pretas
                    } else if (i > 3) {
                        matriz[i][j] = WHITEPIECE; // Brancas
                    }
                    else {
                        matriz[i][j] = EMPTY;
                    }
                }
                else {
                    matriz[i][j] = '0';
                }
                canEat[i][j] = false;
            }
        }
    }

    private void print(char[][] arena) {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                System.out.print(matriz[i][j]);
            }
            System.out.println();
        }
    }

    /*======================= GETTERS AND SETTERS =======================*/

    public boolean getCanEat(int i, int j) {
        return canEat[i][j];
    }

    public void setCanEat(int i, int j, boolean canEat) {
        this.canEat[i][j] = canEat;
    }

    @Override
    public Tabuleiro clone() {
        try {
            Tabuleiro clone = (Tabuleiro) super.clone();
            clone.matriz = new char[TAMANHO][];
            for (int i = 0; i < TAMANHO; i++) {
                clone.matriz[i] = this.matriz[i].clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean isDama(Position pos) {
       char type = matriz[pos.getRow()][pos.getCol()];
       return type == WHITEKING || type == BLACKKING;
    }

    public boolean isDiagonal(Position from, Position to) {
        return ((from.getRow() + from.getCol()) == (to.getRow() + to.getCol()) || (from.getRow() - from.getCol()) == (to.getRow() - to.getCol()));
    }


    /*======================= FUNÇÃO PARA TESTAR SE A CASA É VAZIA =======================*/
    public boolean isEmpty(Position pos) {
        if (isInvalidParam(pos)) return false;
        return matriz[pos.getRow()][pos.getCol()] == EMPTY;
    }

    /*======================= FUNÇÃO DE UTILIDADE PARA EVITAR PARÂMETROS FORA DO LIMITE =======================*/
    public boolean isInvalidParam(Position pos) {
        return pos.getRow() < 0 || pos.getRow() > 5 || pos.getCol() < 0 || pos.getCol() > 5;
    }

    public boolean isInvalidParam(int r, int c) {
        return r < 0 || r > 5 || c < 0 || c > 5;
    }

    /*======================= PEGAR O TIPO DA PEÇA EM UMA COORDENADA =======================*/
    public char getType(Position pos) {
        if (isInvalidParam(pos)) return '#';  // Tipo não reconhecido
        return matriz[pos.getRow()][pos.getCol()];
    }

    public char getType(int r, int c) {
        if (isInvalidParam(r, c)) return '#';
        return matriz[r][c];
    }

    public int getTam() {
        return TAMANHO;
    }

    /*======================= VERIFICA SE É INIMIGO =======================*/
    /// Verifica se a peça na coordenada (r1, c1) é inimiga da peça na coordenada (r2, c2)
    public boolean isEnemy(Position piece, Position otherPiece) {
        char type1 = matriz[piece.getRow()][piece.getCol()];
        char type2 = matriz[otherPiece.getRow()][otherPiece.getCol()];

        if (type2 == type1)
            return false;

        if ("13".contains(String.valueOf(type1)) && "13".contains(String.valueOf(type2)))
            return false;

        return !"24".contains(String.valueOf(type1)) || !"24".contains(String.valueOf(type2));
    }

    public char[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(char[][] matriz) {
        this.matriz = matriz;
    }
    
    public void setPos(Position pos, char type) {
        matriz[pos.getRow()][pos.getCol()] = type;
    }
    
    public char getPos(Position pos) {
        return matriz[pos.getRow()][pos.getCol()];
    }

    public boolean isWhite(Position pos) {
        if (isInvalidParam(pos)) return false;
        return "13".contains(String.valueOf(matriz[pos.getRow()][pos.getCol()]));
    }
}
