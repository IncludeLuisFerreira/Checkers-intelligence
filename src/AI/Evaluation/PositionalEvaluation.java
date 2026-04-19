package AI.Evaluation;

import Engine.MoveManagement;
import Engine.Translator;
import Model.Node;
import Model.Position;
import Model.Tabuleiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PositionalEvaluation extends Evaluation{
    private final Translator translator;

    // ===================== PESOS =====================
    // Ajuste estes valores para mudar o estilo de jogo da IA.
    // Valores maiores = aquele fator influencia mais a decisão.
    private static final int MATERIAL_PIECE        = 100; // valor de uma peça comum
    private static final int MATERIAL_KING         = 175; // valor de uma dama (75% a mais)
    private static final int POSITION_WEIGHT       =   2; // bônus de posicionamento
    private static final int MOBILITY_WEIGHT       =   1; // diferença de movimentos disponíveis
    private static final int CAPTURE_THREAT_WEIGHT =   3; // capturas que posso fazer agora
    private static final int VULNERABILITY_WEIGHT  =   2; // minhas peças em perigo

    // ===================== TABELAS POSICIONAIS =====================
    // Bônus por casa para peças PRETAS (avançam da linha 0 → linha 5)
    // Casas claras são sempre 0 (nunca ocupadas no jogo de damas)
    // Mais alto na tabela = mais bônus
    private static final int[][] BLACK_TABLE = {
            { 0,  0,  0,  0,  0,  0 },  // linha 0 — base das pretas (ponto de partida)
            { 0,  1,  1,  1,  1,  0 },  // linha 1 — pouco progresso
            { 0,  2,  3,  3,  2,  0 },  // linha 2 — começa a pressionar
            { 0,  3,  4,  4,  3,  0 },  // linha 3 — centro do tabuleiro
            { 0,  4,  5,  5,  4,  0 },  // linha 4 — ameaça promoção
            { 0,  5,  6,  6,  5,  0 },  // linha 5 — prestes a virar dama
    };

    // Bônus por casa para peças BRANCAS (avançam da linha 5 → linha 0)
    // Espelhamento vertical do BLACK_TABLE
    private static final int[][] WHITE_TABLE = {
            { 0,  5,  6,  6,  5,  0 },  // linha 0 — prestes a virar dama
            { 0,  4,  5,  5,  4,  0 },  // linha 1 — ameaça promoção
            { 0,  3,  4,  4,  3,  0 },  // linha 2 — centro do tabuleiro
            { 0,  2,  3,  3,  2,  0 },  // linha 3 — começa a pressionar
            { 0,  1,  1,  1,  1,  0 },  // linha 4 — pouco progresso
            { 0,  0,  0,  0,  0,  0 },  // linha 5 — base das brancas (ponto de partida)
    };

    public PositionalEvaluation(Translator translator) {
        this.translator = translator;
    }

    @Override
    public int avaliation(Tabuleiro tabuleiro) {

        // ======================================================
        // TERMO 1: MATERIAL  |  TERMO 2: POSIÇÃO
        // Calculados juntos no mesmo loop para evitar varrer o
        // tabuleiro duas vezes.
        // ======================================================
        int blackMaterial = 0, whiteMaterial = 0;
        int blackPosition = 0, whitePosition = 0;

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {
                Position pos = new Position(i, j);
                if (tabuleiro.isEmpty(pos)) continue;

                boolean isWhite = tabuleiro.isWhite(pos);
                boolean isKing  = tabuleiro.isDama(pos);

                // Material: dama vale MATERIAL_KING, peça normal vale MATERIAL_PIECE
                int matValue = isKing ? MATERIAL_KING : MATERIAL_PIECE;

                // Posição: consulta a tabela correspondente à cor da peça
                // Damas não usam tabela posicional — elas são valiosas em qualquer casa
                int posValue = isKing ? 0
                        : (isWhite ? WHITE_TABLE[i][j] : BLACK_TABLE[i][j]);

                if (isWhite) {
                    whiteMaterial += matValue;
                    whitePosition += posValue;
                } else {
                    blackMaterial += matValue;
                    blackPosition += posValue;
                }
            }
        }

        // ======================================================
        // TERMOS 3, 4, 5: MOBILIDADE, AMEAÇAS, VULNERABILIDADE
        // Calculados varrendo os movimentos de cada peça.
        // ======================================================
        MoveManagement mm = new MoveManagement(tabuleiro, translator);

        int blackMoves          = 0, whiteMoves          = 0;
        int blackCaptureThreats = 0, whiteCaptureThreats = 0;

        // Set de posições ameaçadas (usamos Set para não contar a mesma
        // peça duas vezes quando dois atacantes miram no mesmo alvo)
        Set<Position> piecesBlackCanCapture = new HashSet<>(); // peças brancas em perigo
        Set<Position> piecesWhiteCanCapture = new HashSet<>(); // peças pretas em perigo

        for (int i = 0; i < tabuleiro.getTam(); i++) {
            for (int j = 0; j < tabuleiro.getTam(); j++) {
                Position pos = new Position(i, j);
                if (tabuleiro.isEmpty(pos)) continue;

                boolean isWhite  = tabuleiro.isWhite(pos);
                char    posChar  = translator.getCharFromPosition(pos);

                // getAllMovesForPiece retorna TODOS os movimentos (sem filtro de captura obrigatória)
                // Isso é importante para contar a mobilidade real de cada peça.
                List<Node> allMoves = mm.getAllMovesForPiece(posChar);

                if (isWhite) {
                    whiteMoves += allMoves.size(); // Termo 3: mobilidade branca

                    for (Node move : allMoves) {
                        if (mm.isCapture(move)) {
                            whiteCaptureThreats++; // Termo 4: ameaça ofensiva branca

                            // Termo 5: registra qual peça preta está vulnerável
                            Position from     = translator.getPositionFromChar(move.getOrigin());
                            Position to       = translator.getPositionFromChar(move.getDest());
                            Position captured = mm.findCapturedPosition(from, to);
                            if (captured != null) piecesWhiteCanCapture.add(captured);
                        }
                    }
                } else {
                    blackMoves += allMoves.size(); // Termo 3: mobilidade preta

                    for (Node move : allMoves) {
                        if (mm.isCapture(move)) {
                            blackCaptureThreats++; // Termo 4: ameaça ofensiva preta

                            // Termo 5: registra qual peça branca está vulnerável
                            Position from     = translator.getPositionFromChar(move.getOrigin());
                            Position to       = translator.getPositionFromChar(move.getDest());
                            Position captured = mm.findCapturedPosition(from, to);
                            if (captured != null) piecesBlackCanCapture.add(captured);
                        }
                    }
                }
            }
        }

        // Vulnerabilidade: número de peças ÚNICAS ameaçadas de cada lado
        int blackVulnerability = piecesWhiteCanCapture.size(); // pretas em perigo
        int whiteVulnerability = piecesBlackCanCapture.size(); // brancas em perigo

        // ======================================================
        // FÓRMULA FINAL
        // Cada (black - white) positivo = vantagem das pretas
        // Vulnerabilidade é invertida: (white - black) porque
        // vulnerabilidade é negativa para quem a sofre.
        // ======================================================
        return (blackMaterial - whiteMaterial) +
                (blackPosition       - whitePosition)       * POSITION_WEIGHT +
                (blackMoves          - whiteMoves)          * MOBILITY_WEIGHT +
                (blackCaptureThreats - whiteCaptureThreats) * CAPTURE_THREAT_WEIGHT +
                (whiteVulnerability  - blackVulnerability)  * VULNERABILITY_WEIGHT;
    }
}
