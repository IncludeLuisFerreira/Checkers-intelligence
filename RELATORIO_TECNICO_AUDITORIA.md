# RELATÓRIO TÉCNICO DE AUDITORIA - JOGO DE DAMAS 6x6

**Data:** 05/04/2026  
**Projeto:** Sistema de Jogo de Damas com IA (Minimax)  
**Arquitetura:** Model-View-Engine-AI  

---

## 🔴 BUGS CRÍTICOS E FALHAS DE LÓGICA

### 1. BUG CRÍTICO: Captura Obrigatória Não Funciona para Damas em Todas as Direções

**[ARQUIVO]:** `src/Engine/MoveManagement.java`  
**[GRAVIDADE]:** Crítica  
**[DESCRIÇÃO]:** A função `addAllMovesKing()` (linhas 169-193) possui lógica incorreta para capturas de Damas. Quando uma Dama encontra um inimigo, ela para imediatamente após adicionar apenas UMA casa após o inimigo, violando a regra de que Damas podem continuar se movendo após capturar. Além disso, a função não permite múltiplas capturas em sequência na mesma direção.

**[POR QUE MUDAR]:** 
- Viola regras oficiais de damas onde Damas podem se mover múltiplas casas após captura
- A IA não consegue avaliar corretamente jogadas de captura múltipla com Damas
- Jogador humano fica limitado em movimentos válidos

**[SOLUÇÃO]:**
```java
private void addAllMovesKing(List<Node> moves, Position pos, char from) {
    int[][] direction = {{1,1}, {1, -1}, {-1, 1}, {-1, -1}};

    for (int[] dir : direction) {
        Position current = pos;
        Position enemyPos = null;
        boolean foundEnemy = false;

        while (true) {
            current = current.getPosition(current, dir[0], dir[1]);

            if (tabuleiro.isInvalidParam(current)) break;

            if (tabuleiro.isEmpty(current)) {
                if (foundEnemy) {
                    // Adiciona todas as casas após captura
                    moves.add(new Node(from, translator.getCharFromPosition(current)));
                } else {
                    // Movimento simples
                    moves.add(new Node(from, translator.getCharFromPosition(current)));
                }
            }
            else if (tabuleiro.isEnemy(pos, current) && !foundEnemy) {
                foundEnemy = true;
                enemyPos = current;
            }
            else {
                // Encontrou peça aliada ou segundo inimigo
                break;
            }
        }
    }
}
```

---

### 2. BUG CRÍTICO: Vazamento de Memória na Árvore de Busca

**[ARQUIVO]:** `src/AI/Tree.java`  
**[GRAVIDADE]:** Crítica  
**[DESCRIÇÃO]:** A função `montarArvoreIA()` (linhas 21-50) clona o tabuleiro para cada nó filho, mas armazena a referência da matriz interna via `jogada.setMatriz(tabuleiroClone)` (linha 34). O método `setMatriz()` em `Node.java` (linha 52) armazena a referência direta da matriz, não uma cópia, causando compartilhamento indevido de estado entre nós.

**[POR QUE MUDAR]:**
- Múltiplos nós compartilham a mesma matriz, corrompendo o estado da árvore
- Avaliação MinMax fica incorreta pois nós diferentes avaliam o mesmo estado
- Consumo excessivo de memória sem benefício (clones inúteis)
- Profundidade limitada a 5 quando deveria suportar mais

**[SOLUÇÃO]:**
```java
// Em Node.java, modificar setMatriz:
public void setMatriz(Tabuleiro tabuleiro) {
    char[][] original = tabuleiro.getMatriz();
    this.matriz = new char[original.length][];
    for (int i = 0; i < original.length; i++) {
        this.matriz[i] = original[i].clone();
    }
}
```

---

### 3. BUG CRÍTICO: Promoção Hardcoded para Tabuleiro 6x6

**[ARQUIVO]:** `src/Engine/PromotionManagement.java`  
**[GRAVIDADE]:** Moderada  
**[DESCRIÇÃO]:** A função `canBePromoted()` (linhas 13-20) usa valores hardcoded (linha 0 para brancas, linha 5 para pretas). Embora o projeto seja 6x6, isso viola o princípio DRY e dificulta manutenção.

**[POR QUE MUDAR]:**
- Código frágil e não escalável
- Se o tamanho do tabuleiro mudar em testes, quebra silenciosamente
- Viola Single Responsibility Principle (SRP)

**[SOLUÇÃO]:**
```java
public class PromotionManagement {
    private final Tabuleiro tabuleiro;
    private final int lastRow;

    public PromotionManagement(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.lastRow = tabuleiro.getTam() - 1;
    }

    public void canBePromoted(Position pos) {
        if (tabuleiro.isDama(pos)) return;

        if (tabuleiro.isWhite(pos) && pos.getRow() == 0) {
            tabuleiro.setPos(pos, Tabuleiro.WHITEKING);
        }
        else if (!tabuleiro.isWhite(pos) && pos.getRow() == lastRow) {
            tabuleiro.setPos(pos, Tabuleiro.BLACKKING);
        }
    }
}
```

---

### 4. BUG GRAVE: Lógica de Multi-Captura Incompleta

**[ARQUIVO]:** `src/Engine/Engine.java`  
**[GRAVIDADE]:** Crítica  
**[DESCRIÇÃO]:** A função `verificarSePodeCapturar()` (linhas 127-143) verifica se há capturas adicionais após uma captura, mas não valida se a peça que capturou é a MESMA que deve continuar capturando. Isso permite que o jogador mude de peça no meio de uma sequência de capturas obrigatórias.

**[POR QUE MUDAR]:**
- Viola regra fundamental de damas: captura múltipla deve ser com a mesma peça
- Permite trapaça do jogador
- IA não respeita essa regra, fazendo jogadas inválidas

**[SOLUÇÃO]:**
```java
private boolean verificarSePodeCapturar(Position clicked) {
    char pieceChar = translator.getCharFromPosition(clicked);
    List<Node> allMoves = moveManagement.getAllMovesForPiece(pieceChar);
    List<Node> captureMoves = new ArrayList<>();
    
    for (Node move : allMoves) {
        // Verifica se a origem é a peça que acabou de capturar
        if (move.getOrigin() == pieceChar && moveManagement.isCapture(move)) {
            captureMoves.add(move);
        }
    }

    if (!captureMoves.isEmpty()) {
        origin = clicked;
        movimentos = captureMoves;
        destacarMovimentos(movimentos);
        mustCaptured = true; // Flag para forçar captura
        return true;
    }
    mustCaptured = false;
    return false;
}
```

---

### 5. BUG MODERADO: Contador de Peças Dessincronizado

**[ARQUIVO]:** `src/Model/Tabuleiro.java`  
**[GRAVIDADE]:** Moderada  
**[DESCRIÇÃO]:** Os contadores `white` e `black` (linhas 16-17) são inicializados com 6, mas não são atualizados quando peças são promovidas ou quando o tabuleiro é clonado. O método `clone()` (linhas 62-72) não copia os contadores.

**[POR QUE MUDAR]:**
- Condição de vitória `isOver()` fica incorreta
- IA avalia estados de jogo errados
- Clones de tabuleiro têm contadores errados

**[SOLUÇÃO]:**
```java
@Override
public Tabuleiro clone() {
    try {
        Tabuleiro clone = (Tabuleiro) super.clone();
        clone.matriz = new char[TAMANHO][];
        for (int i = 0; i < TAMANHO; i++) {
            clone.matriz[i] = this.matriz[i].clone();
        }
        clone.white = this.white;
        clone.black = this.black;
        return clone;
    } catch (CloneNotSupportedException e) {
        throw new RuntimeException("Falha ao clonar tabuleiro", e);
    }
}
```

---

### 6. BUG CRÍTICO: IA Usa Movimento Aleatório ao Invés de Melhor Jogada

**[ARQUIVO]:** `src/AI/AI.java`  
**[GRAVIDADE]:** Crítica  
**[DESCRIÇÃO]:** O método `getBestMove()` (linha 25) retorna `tree.RandomMove(root)` ao invés de `tree.BestMove(root)`. Isso anula completamente o algoritmo Minimax, fazendo a IA jogar aleatoriamente.

**[POR QUE MUDAR]:**
- IA não usa a árvore de busca calculada
- Desperdício de processamento (monta árvore mas não usa)
- IA joga como iniciante ao invés de usar estratégia

**[SOLUÇÃO]:**
```java
public Node getBestMove() {
    return tree.BestMove(root);
}
```

---

### 7. BUG MODERADO: Profundidade da Árvore Inconsistente

**[ARQUIVO]:** `src/AI/Tree.java`  
**[GRAVIDADE]:** Moderada  
**[DESCRIÇÃO]:** A constante `MAXHEIGHT = 12` (linha 17) não é usada. A profundidade real é hardcoded como 5 na linha 23 (`if (profundidade == 5`). Além disso, o comentário menciona profundidade 4, mas o código usa 5.

**[POR QUE MUDAR]:**
- Código confuso e inconsistente
- Dificulta ajuste de dificuldade da IA
- Constante inútil ocupa memória

**[SOLUÇÃO]:**
```java
public class Tree {
    private final Translator translator;
    private final int MAX_DEPTH = 4;  // Profundidade configurável
    private int childrenCount = 0;

    public Tree(int TAM_TABULEIRO) {
        this.translator = new Translator(TAM_TABULEIRO);
    }

    public void montarArvoreIA(Node arvore, int profundidade, Tabuleiro tabuleiro, boolean isWhiteTurn) {
        ArrayList<Node> jogadasPossiveis = retornarJogadasPossiveis(tabuleiro, isWhiteTurn);

        if (profundidade == MAX_DEPTH || jogadasPossiveis.isEmpty()) return;
        
        // resto do código...
    }
}
```

---

### 8. BUG GRAVE: Função isEnemy() com Lógica Confusa

**[ARQUIVO]:** `src/Model/Tabuleiro.java`  
**[GRAVIDADE]:** Moderada  
**[DESCRIÇÃO]:** A função `isEnemy()` (linhas 127-140) usa lógica com strings e contains() de forma confusa. As linhas 135-138 têm dupla negação desnecessária.

**[POR QUE MUDAR]:**
- Código difícil de entender e manter
- Propenso a bugs em modificações futuras
- Performance ruim (conversão para String desnecessária)

**[SOLUÇÃO]:**
```java
public boolean isEnemy(Position piece, Position otherPiece) {
    if (isInvalidParam(otherPiece) || isEmpty(otherPiece)) return false;
    
    char type1 = matriz[piece.getRow()][piece.getCol()];
    char type2 = matriz[otherPiece.getRow()][otherPiece.getCol()];

    boolean piece1IsWhite = (type1 == WHITEPIECE || type1 == WHITEKING);
    boolean piece2IsWhite = (type2 == WHITEPIECE || type2 == WHITEKING);

    return piece1IsWhite != piece2IsWhite;
}
```

---

### 9. BUG MODERADO: Avaliação MinMax Não Considera Damas

**[ARQUIVO]:** `src/AI/Evaluation/MorePiecesEvaluation.java`  
**[GRAVIDADE]:** Moderada  
**[DESCRIÇÃO]:** A função `Evaluation()` (linhas 10-26) conta todas as peças igualmente, sem dar peso maior para Damas. Uma Dama vale muito mais que uma peça comum.

**[POR QUE MUDAR]:**
- IA não valoriza promoções
- Estratégia fraca: troca Dama por peça comum
- Não reflete valor real das peças

**[SOLUÇÃO]:**
```java
@Override
public int Evaluation(Node node, Tabuleiro tabuleiro) {
    int whiteScore = 0;
    int blackScore = 0;
    
    final int PIECE_VALUE = 1;
    final int KING_VALUE = 3;

    for (int i = 0; i < tabuleiro.getTam(); i++) {
        for (int j = 0; j < tabuleiro.getTam(); j++) {
            Position pos = new Position(i, j);
            if (tabuleiro.isEmpty(pos)) continue;

            char piece = tabuleiro.getPos(pos);
            int value = tabuleiro.isDama(pos) ? KING_VALUE : PIECE_VALUE;

            if (tabuleiro.isWhite(pos)) {
                whiteScore += value;
            } else {
                blackScore += value;
            }
        }
    }

    return blackScore - whiteScore;
}
```

---

### 10. BUG GRAVE: Falta Validação de Captura Obrigatória no Início do Turno

**[ARQUIVO]:** `src/Engine/Engine.java`  
**[GRAVIDADE]:** Crítica  
**[DESCRIÇÃO]:** Na função `handleClick()` (linhas 68-72), a validação de captura obrigatória só acontece DEPOIS do jogador selecionar uma peça. Se o time tem capturas disponíveis, o jogador deveria ser impedido de selecionar peças que não podem capturar.

**[POR QUE MUDAR]:**
- Permite jogadas ilegais (mover peça quando há captura obrigatória com outra)
- UX ruim: jogador seleciona peça e nada acontece
- IA não segue essa regra consistentemente

**[SOLUÇÃO]:**
```java
if (origin.getRow() == -1) {
    if (tabuleiro.isEmpty(clicked) || isWhiteTurn != tabuleiro.isWhite(clicked)) return;

    List<Node> possibleMoves = moveManagement.getMoves(translator.getCharFromPosition(clicked));
    boolean teamHasCaptures = moveManagement.teamHasCaptures(isWhiteTurn);

    // Se o time tem capturas, só permite selecionar peças que podem capturar
    if (teamHasCaptures) {
        if (possibleMoves.isEmpty() || !moveManagement.hasCaptures(possibleMoves)) {
            // Feedback visual: peça não pode ser selecionada
            JOptionPane.showMessageDialog(null, 
                "Captura obrigatória! Selecione uma peça que pode capturar.", 
                "Movimento Inválido", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    movimentos = possibleMoves;
    origin = clicked;
    destacarMovimentos(movimentos);
}
```

---

## 🟡 MELHORIAS DE ARQUITETURA E OTIMIZAÇÃO

### 11. MELHORIA: Violação do Princípio de Responsabilidade Única

**[ARQUIVO]:** `src/Engine/Engine.java`  
**[GRAVIDADE]:** Melhoria  
**[DESCRIÇÃO]:** A classe `Engine` (linhas 1-217) faz TUDO: gerencia UI, lógica de jogo, IA, validação, e até popup de easter egg. Isso viola SRP e dificulta testes unitários.

**[POR QUE MUDAR]:**
- Código difícil de testar
- Mudanças em uma responsabilidade afetam outras
- Dificulta trabalho em equipe

**[SOLUÇÃO]:**
Refatorar em classes separadas:
```java
// GameController.java - Orquestra o jogo
// MoveValidator.java - Valida movimentos
// AIController.java - Gerencia IA
// UIController.java - Gerencia interface
```

---

### 12. MELHORIA: Falta de Tratamento de Exceções

**[ARQUIVO]:** `src/Model/Tabuleiro.java`  
**[GRAVIDADE]:** Melhoria  
**[DESCRIÇÃO]:** O método `clone()` (linha 70) retorna `null` em caso de erro, o que pode causar `NullPointerException` silencioso.

**[POR QUE MUDAR]:**
- Erros silenciosos são difíceis de debugar
- Viola fail-fast principle

**[SOLUÇÃO]:**
```java
@Override
public Tabuleiro clone() {
    try {
        Tabuleiro clone = (Tabuleiro) super.clone();
        clone.matriz = new char[TAMANHO][];
        for (int i = 0; i < TAMANHO; i++) {
            clone.matriz[i] = this.matriz[i].clone();
        }
        clone.white = this.white;
        clone.black = this.black;
        return clone;
    } catch (CloneNotSupportedException e) {
        throw new RuntimeException("Falha crítica ao clonar tabuleiro", e);
    }
}
```

---

### 13. MELHORIA: Uso Ineficiente de SwingUtilities.invokeLater

**[ARQUIVO]:** `src/Engine/Engine.java`  
**[GRAVIDADE]:** Melhoria  
**[DESCRIÇÃO]:** Nas linhas 109-112 e 195-207, há uso aninhado de `SwingUtilities.invokeLater()`, causando atrasos desnecessários na UI.

**[POR QUE MUDAR]:**
- UI trava durante cálculo da IA
- Experiência ruim para o usuário
- Pode causar race conditions

**[SOLUÇÃO]:**
```java
// Executar IA em thread separada
private void executarMelhorJogada() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(() -> {
        Node bestMove = ai.getBestMove();
        
        if (bestMove != null) {
            SwingUtilities.invokeLater(() -> {
                aplicarMovimentoIA(bestMove);
            });
        }
    });
    executor.shutdown();
}

private void aplicarMovimentoIA(Node bestMove) {
    boolean wasCapture = moveManagement.isCapture(bestMove);
    if (wasCapture) tabuleiro.capture(false);
    
    moveManagement.execMove(bestMove);
    sincronizarView();

    Position destPos = translator.getPositionFromChar(bestMove.getDest());
    if (!(wasCapture && verificarSePodeCapturar(destPos))) {
        turnManagement.changeTurn();
    }
}
```

---

### 14. MELHORIA: Falta de Poda Alpha-Beta

**[ARQUIVO]:** `src/AI/MinMax.java`  
**[GRAVIDADE]:** Melhoria  
**[DESCRIÇÃO]:** O algoritmo Minimax (linhas 11-29) não implementa poda Alpha-Beta, explorando toda a árvore desnecessariamente.

**[POR QUE MUDAR]:**
- Performance ruim: explora ramos que não afetam decisão
- Limita profundidade da busca
- IA mais lenta que o necessário

**[SOLUÇÃO]:**
```java
public int MinMaxCheckersGameAlphaBeta(Node root, Tabuleiro tabuleiro, int alpha, int beta, boolean isMaximizing) {
    if (root.getChildren().isEmpty()) {
        return evaluator.Evaluation(root, tabuleiro);
    }

    if (isMaximizing) {
        int maxEval = Integer.MIN_VALUE;
        for (Node child : root.getChildren()) {
            int eval = MinMaxCheckersGameAlphaBeta(child, tabuleiro, alpha, beta, false);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);
            if (beta <= alpha) break; // Poda Beta
        }
        root.setMinMax(maxEval);
        return maxEval;
    } else {
        int minEval = Integer.MAX_VALUE;
        for (Node child : root.getChildren()) {
            int eval = MinMaxCheckersGameAlphaBeta(child, tabuleiro, alpha, beta, true);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);
            if (beta <= alpha) break; // Poda Alpha
        }
        root.setMinMax(minEval);
        return minEval;
    }
}
```

---

### 15. MELHORIA: Falta de Liberação de Memória na Árvore

**[ARQUIVO]:** `src/Model/Node.java`  
**[GRAVIDADE]:** Melhoria  
**[DESCRIÇÃO]:** O método `clear()` (linhas 68-73) limpa a lista de filhos, mas não chama `clear()` recursivamente nos filhos, causando vazamento de memória.

**[POR QUE MUDAR]:**
- Memória não é liberada adequadamente
- Árvores antigas permanecem na memória
- Pode causar OutOfMemoryError em jogos longos

**[SOLUÇÃO]:**
```java
public void clear() {
    if (children != null) {
        for (Node child : children) {
            child.clear(); // Recursivo
        }
        children.clear();
        children = null;
    }
    matriz = null;
    minMax = 0;
}
```

---

## 📊 RESUMO EXECUTIVO

### Bugs Críticos Encontrados: 6
1. Captura de Damas não funciona corretamente
2. Vazamento de memória na árvore
3. Multi-captura permite trocar de peça
4. IA usa movimento aleatório
5. Contador de peças dessincronizado
6. Falta validação de captura obrigatória

### Bugs Moderados: 4
7. Promoção hardcoded
8. Profundidade inconsistente
9. Lógica isEnemy() confusa
10. Avaliação não considera valor de Damas

### Melhorias Sugeridas: 5
11. Violação de SRP
12. Falta tratamento de exceções
13. UI trava durante IA
14. Falta poda Alpha-Beta
15. Vazamento de memória em Node

---

## 🎯 PRIORIDADE DE CORREÇÃO

**URGENTE (Corrigir Primeiro):**
- Bug #6: IA usa movimento aleatório (anula todo o Minimax)
- Bug #1: Captura de Damas incorreta (regra fundamental)
- Bug #4: Multi-captura permite trocar peça (permite trapaça)

**ALTA:**
- Bug #2: Vazamento de memória (limita profundidade)
- Bug #5: Contador dessincronizado (afeta vitória)
- Bug #10: Validação de captura obrigatória

**MÉDIA:**
- Bugs #3, #7, #8, #9
- Melhorias #14, #15

**BAIXA:**
- Melhorias #11, #12, #13

---

## 📝 OBSERVAÇÕES FINAIS

O projeto demonstra boa organização arquitetural, mas possui bugs críticos que comprometem a jogabilidade e a eficácia da IA. A correção dos bugs #1, #4 e #6 é essencial para o funcionamento correto do jogo. As melhorias de performance (poda Alpha-Beta) e gerenciamento de memória permitirão aumentar a profundidade de busca e melhorar a qualidade das jogadas da IA.

**Estimativa de Esforço:**
- Correção de bugs críticos: 8-12 horas
- Correção de bugs moderados: 4-6 horas  
- Implementação de melhorias: 6-8 horas
- **Total:** 18-26 horas de desenvolvimento

---

**Relatório gerado por:** Amazon Q Developer  
**Metodologia:** Análise estática de código + Revisão de lógica de negócio  
**Padrões avaliados:** SOLID, Clean Code, Java Best Practices
