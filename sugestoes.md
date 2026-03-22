# Aula de IA - TODO-LIST

* A função de saber se uma peça pode ser capturada tem que ser muito rápida (situação atual: quadrática).
* Representar uma jogada tem que ser muito rápida.
* Usar a representação de jogada como [A, B].
* Fazer a tradução dos quadrados em letras. (hashmap).
* Verificar se existe ganhador (Colocar o total de peças brancas e pretas, decrementar do inimigo assim que comer).
* Fazer uma heurística de poda na árvore para que ela consiga terminar o jogo antes de ter várias damas.
* No momento que tiver somente duas damas no tabuleiro, verifique se nenhuma delas pode capturar, declara-se empate.
* Verificar se quem acabou de jogar pode jogar novamente.

# Aula de IA - TODO-LIST 2

CRIAÇÃO DA ÁRVORE

- PARA O ESTADO DO TABULEIRO, VERIFICAR JOGADAS POSSÍVEIS
- PARA CADA JOGADA POSSÍVEL, CRIA UM NÓ;
- ADICIONAMOS OS NÓS NA ÁRVORE;
- ENTRAMOS RECURSIVAMENTE NOS NÓS FILHOS


# Implementar por brincadeira

* Pop-up do comercial do IF para ganhar uma peça.

# Aula de IA - TODO-LIST 3

* Altura da árvore da IA: 10
    OPTATIVA
    * Representação da matriz em vetor de char (‘String’) de 18 posições.

# Aula de IA - TODO-LIST 3

* Pode-se representar somente as casas que não estão vazias com vetores de peças pretas e outro de peças brancas.
* Cada aluno tem que implementar a sua própria heurística de ganhar, perder e empate.
* Implementar uma melhoria para o MinMax no nosso contexto de projeto.
* Fazer aleatório do nível 10 para frente ou recriar a árvore a partir daquele nível.
* Thread  pra montar a árvore enquanto o usuário joga
* Construir a árvore e depois rodar o MinMax