package Logic;

import Entities.Tabuleiro;


/**
 * @author includeluisferreira
 * <p>
 *
 * Classe LogicTabuleiro procura abstrair a lógica de movimentos
 * e captura do jogo de Damas. Ela opera sobre um objeto <b>Tabuleiro</b>
 * que representa o estado da matriz de char.
 * <p>
 *
 * Principais funções:
 * <p>
 * - Validar e executar movimentos simples e capturas.
 * - Controlar o movimento das damas.
 * - Promoção de peças ao atingir a última linha.
 * - Verificar e identificar situações de captura obrigatória durante o turno.
 */
public class LogicTabuleiro {

    private final Tabuleiro tabuleiroLogico;
    boolean turn = true;

    public LogicTabuleiro(Tabuleiro tabuleiroLogico) {
        this.tabuleiroLogico = tabuleiroLogico;
    }

    /* =========== FUNÇÃO DE REGRA DE MOVIMENTO DE UMA PEÇA SIMPLES ===========*/
    private boolean simpleMove(int r1, int c1, int r2, int c2) {
        if (!isEmpty(r2, c2))
            return false;

        if (tabuleiroLogico.getMatriz()[r1][c1] == '1' && r1 <= r2) {
            return false;
        }
        else if (tabuleiroLogico.getMatriz()[r1][c1] == '2' && r1 >= r2) {
            return false;
        }

        tabuleiroLogico.getMatriz()[r2][c2] = tabuleiroLogico.getMatriz()[r1][c1];
        tabuleiroLogico.getMatriz()[r1][c1] = '0';
        return true;
    }

    /*======================= VERIFICA CAPTURA =======================*/
    private boolean verifyCapture(int r1, int c1, int r2, int c2) {
        int linhaMeio = (r1 + r2) / 2;
        int colunaMeio = (c1 + c2) / 2;
        char piece = tabuleiroLogico.getMatriz()[r1][c1];

        if (!isEmpty(linhaMeio, colunaMeio) && isEmpty(r2, c2) && isEnemy(r1, c1, linhaMeio, colunaMeio)) {
            tabuleiroLogico.getMatriz()[linhaMeio][colunaMeio] = '0';
            tabuleiroLogico.getMatriz()[r2][c2] = piece;
            tabuleiroLogico.getMatriz()[r1][c1] = '0';
            tabuleiroLogico.setCanEat(r1, c1, false);
            return true;
        }

        return false;
    }

    /*======================= FUNÇÕES DE UTILIDADE DA DAMA =======================*/
    private boolean isDama(int r1, int c1) {
        return "34".contains(String.valueOf(tabuleiroLogico.getMatriz()[r1][c1]));
    }

    private boolean isDiagonal(int r1, int c1, int r2, int c2) {
        return ((r1 + c1) == (r2 + c2) || (r1 - c1) == (r2 - c2));
    }


    /*======================= MOVIMENTOS DA DAMA =======================*/
    private boolean moveDama(int r1, int c1, int r2, int c2) {

        if (!isEmpty(r2, c2))
            return false;

        int direcao = (c1 - c2 < 0 ? -1 : 1);

        if (!isEmpty(r2 + direcao, c2 + direcao) && isEnemy(r1, c1, r2 + direcao, c2 + direcao)) {
            tabuleiroLogico.getMatriz()[r2+direcao][c2+direcao] = '0';
        }

        tabuleiroLogico.setCanEat(r1, c1, false);
        tabuleiroLogico.getMatriz()[r2][c2] = tabuleiroLogico.getMatriz()[r1][c1];
        tabuleiroLogico.getMatriz()[r1][c1] = '0';

        return true;
    }

    /*======================= FUNÇÃO GERAL DE MOVIMENTO DE PEÇAS =======================*/
    public boolean moverPecaLogica(int r1, int c1, int r2, int c2) {

        boolean mov = false;

        // Movimento simples
        if (Math.abs(r1 - r2) == 1 && Math.abs(c1 - c2) == 1)
            mov =  simpleMove(r1, c1, r2, c2);

        // Movimento de captura
        if (Math.abs(r1 - r2) == 2 && Math.abs(c1 - c2) == 2)
            mov = verifyCapture(r1, c1, r2, c2);

        if (isDama(r1, c1) && isDiagonal(r1, c1, r2, c2))
            mov =  moveDama(r1, c1, r2, c2);

        if (mov) {
            if (tabuleiroLogico.getMatriz()[r2][c2] == '2' && r2 == 5) {
                tabuleiroLogico.getMatriz()[r2][c2] = '4';
            }
            if (tabuleiroLogico.getMatriz()[r2][c2] == '1' && r2 == 0) {
                tabuleiroLogico.getMatriz()[r2][c2] = '3';
            }
        }

        return mov;
    }

    /*======================= LÓGICA DE TURNO =======================*/
    // True -> white; False -> black
    public boolean whoseTurn() {
      return turn;
    }

    public void changeTurn() {
        turn = !turn;
    }


    /*======================= FUNÇÃO PARA TESTAR SE A CASA É VAZIA =======================*/
    public boolean isEmpty(int r, int c) {
        if (isNotParamValid(r, c)) return false;
        return tabuleiroLogico.getMatriz()[r][c] == '0';
    }

    /*======================= FUNÇÃO DE UTILIDADE PARA EVITAR PARÂMETROS FORA DO LIMITE =======================*/
    public boolean isNotParamValid(int r, int c) {
        return r < 0 || r > 5 || c < 0 || c > 5;
    }

    /*======================= PEGAR O TIPO DA PEÇA EM UMA COORDENADA =======================*/
   public char getType(int r, int c) {
        if (isNotParamValid(r, c)) return '#';  // Tipo nao reconhecido
        return tabuleiroLogico.getMatriz()[r][c];
   }

   public int getTam() {
        return tabuleiroLogico.getTam();
   }

    /*======================= VERIFICA SE É INIMIGO =======================*/
    /// Verifica se a peça na coordenada (r1, c1) é inimiga da peça na coordenada (r2, c2)
   public boolean isEnemy(int r1, int c1, int r2, int c2) {
        char type1 = getType(r1, c1);
        char type2 = getType(r2, c2);

        if (type2 == type1)
            return false;

        if ("13".contains(String.valueOf(type1)) && "13".contains(String.valueOf(type2)))
            return false;

       return !"24".contains(String.valueOf(type1)) || !"24".contains(String.valueOf(type2));
   }

    /*======================= TESTING FUNCTIONS =======================*/

    public boolean pieceCanEat(int r, int c) {
        return tabuleiroLogico.getCanEat(r, c);
    }

    public boolean anyoneCanEat(boolean turn) {
        return anyKingCanEat(turn) || anySimplePieceCanEat(turn);
    }

    private boolean anyKingCanEat(boolean turn) {
        boolean canEat = false;
        char type = (turn ? '3' : '4');

        for (int i = 0; i < getTam(); i++) {
            for (int j = 0; j < getTam(); j++) {

                if (!isDama(i, j) || getType(i, j) != type) continue;

                for (int k = 0; k < getTam() - j - 1; k++) {

                    if (!isEmpty(i + 1 + k, j + 1 + k) && isEmpty(i + 2 + k, j + 2 + k)) {
                        canEat = true;
                        tabuleiroLogico.setCanEat(i, j, canEat);
                        continue;
                    }

                    if (!isEmpty(i - 1 - k, j + 1 + k) && isEmpty(i - 2 - k, j + 2 + k)) {
                        canEat = true;
                        tabuleiroLogico.setCanEat(i, j, canEat);
                    }
                }

                for (int k = 0; k < j; k++) {

                    if (!isEmpty(i - 1 - k, j - 1 - k) && isEmpty(i - 2 - k, j - 2 - k)) {
                        canEat = true;
                        tabuleiroLogico.setCanEat(i, j, canEat);
                    }
                }
            }
        }
        return canEat;
    }


    private boolean anySimplePieceCanEat(boolean turn) {

        int direcao;
        boolean canEat = false;
        char type = (turn ? '1' : '2');

        for (int i = 0; i < getTam(); i++) {
            for (int j = 0; j < getTam(); j++) {

                if (isEmpty(i, j) || getType(i, j) != type) continue;

                if (getType(i, j) == '1') direcao = -1;
                else direcao = 1;

                if (!isEmpty(i + direcao, j + direcao) && isEmpty(i + 2 * direcao, j + 2 * direcao) && isEnemy(i, j, i + direcao, j + direcao)) {
                    tabuleiroLogico.setCanEat(i, j, true);
                    canEat = true;
                }

                if (!isEmpty(i + direcao, j - direcao) && isEmpty(i + 2 * direcao, j - 2 * direcao) && isEnemy(i, j, i + direcao, j - direcao)) {
                    tabuleiroLogico.setCanEat(i, j, true);
                    canEat = true;
                }

            }
        }
        return canEat;
    }
}
