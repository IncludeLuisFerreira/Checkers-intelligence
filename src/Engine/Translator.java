package Engine;

import Model.Position;
import Model.Move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Translator {

    private final HashMap<Character, Position> charToPos;
    private final HashMap<Position, Character> posToChar;
    private final int TAM;

    public Translator(int TAM_TABULEIRO) {
        charToPos = new HashMap<>();
        posToChar = new HashMap<>();
        TAM = TAM_TABULEIRO;
        completeHashMap();
    }

   private void completeHashMap() {
        char c = 'A';

        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if ((i + j) % 2 != 0) {
                    Position pos = new Position(i, j);
                    charToPos.put(c, pos);
                    posToChar.put(pos, c++); // Esse daqui me deu um problema do cacete pq tava salvando o hash do objeto como chave e n a linha e coluna.
                }
            }
        }
   }

   public Position getPositionFromChar(char c) {
        return charToPos.get(c);
   }

   public List<Position> getPositionFromMove(Move move) {
        List<Position> list = new ArrayList<>();
        list.add(getPositionFromChar(move.from()));
        list.add(getPositionFromChar(move.to()));
        return list;
   }

   public char getCharFromPosition(Position pos) {
        return posToChar.get(pos);
   }
}
