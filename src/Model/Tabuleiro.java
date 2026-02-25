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
                        matriz[i][j] = '2'; // Pretas
                    } else if (i > 3) {
                        matriz[i][j] = '1'; // Brancas
                    }
                    else {
                        matriz[i][j] = '0';
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
       char type = matriz[pos.r()][pos.c()];
       return type == WHITEKING || type == BLACKKING;
    }

    public boolean isDiagonal(Position from, Position to) {
        return ((from.r() + from.c()) == (to.r() + to.c()) || (from.r() - from.c()) == (to.r() - to.c()));
    }


    /*======================= FUNÇÃO PARA TESTAR SE A CASA É VAZIA =======================*/
    public boolean isEmpty(Position pos) {
        if (isNotParamValid(pos)) return false;
        return matriz[pos.r()][pos.c()] == EMPTY;
    }

    /*======================= FUNÇÃO DE UTILIDADE PARA EVITAR PARÂMETROS FORA DO LIMITE =======================*/
    public boolean isNotParamValid(Position pos) {
        return pos.r() < 0 || pos.r() > 5 || pos.c() < 0 || pos.c() > 5;
    }

    /*======================= PEGAR O TIPO DA PEÇA EM UMA COORDENADA =======================*/
    public char getType(Position pos) {
        if (isNotParamValid(pos)) return '#';  // Tipo não reconhecido
        return matriz[pos.r()][pos.c()];
    }

    public int getTam() {
        return TAMANHO;
    }

    /*======================= VERIFICA SE É INIMIGO =======================*/
    /// Verifica se a peça na coordenada (r1, c1) é inimiga da peça na coordenada (r2, c2)
    public boolean isEnemy(Position piece, Position otherPiece) {
        char type1 = matriz[piece.r()][piece.c()];
        char type2 = matriz[otherPiece.r()][otherPiece.c()];

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
        matriz[pos.r()][pos.c()] = type;
    }
    
    public char getPos(Position pos) {
        return matriz[pos.r()][pos.c()];
    }

    public boolean isWhite(Position pos) {
        return "13".contains(String.valueOf(matriz[pos.r()][pos.c()]));
    }
}
