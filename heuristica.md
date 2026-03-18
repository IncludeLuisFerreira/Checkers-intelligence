# Estudo de heurísitcas

## Básica

### Descrição
Contar o número de peças e de damas, comparar com o oponente.
Resultado positivo: IA está "ganhando"
Resultado negativo: IA está "perdendo"

### Pontos positivos
Simplicidade de implementação

````
int white
int black

for W in White:
    if W is King
        W += 2
    else
        W ++

// Mesmo for para as brancas

# Se for maior ta ganhando
return black > white
````


### Pontos Negativos
Não necessáriamente o jogador que tiver mais peças é aquele
que vai ganhar. Exemplo: jogador 1 tem mais peças que jogador B,
porém o B consegue fazer capturas em séries e acabar com o jogo.



## Minimizar jogadas do Oponente

Se fizesse um método que busca sempre a menor possíbilidades de
jogadas do oponente?

### Perguntas a serem respondidas
* Será que eu diminuir a jogadas do adversário faz eu jogar melhor?

  Eu acho que não, mas torna ele previsível, se consigo prever a
  jogada eu posso contra-atacar de maneira mais eficiente.
  obs: não faço ideia do que estou falando

* Diminuir a jogadas do adversário fará com que eu tenha mais jogadas?

  Não necessáriamente vai fazer eu ter mais jogadas ou melhores jogadas,
  mas, imagino eu, esse método combinado pode trazer um bom resultado


### Pontos positivos
* Jogadas do oponente se tornam previsíveis
* Busca o caminho que deixa o oponente preso

```
    # Parece muito simples e errado, e as capturas múltiplas?
    list<jogadas> oponente = pegarjogadas()
    return openetes.len()
```

Pode se melhorar ela fazendo uma função que da pesos diferentes para
cada tipo de jogada que o oponente tenha.

```
    M # quantidade de movimentos do oponente
    
    if move is captura:
        M += 2
        self(move) # passar uma chamada recursiva para ver se 
                    é captura múltipla
    
    if move is simple_move:
        M ++
        
    if move is king_move:
        M += 3
        
    if move is king_captura:
        M += 4
        self(move)  # Chamada recursiva
        
    
```

Imagino que com essa adição, a função será mais plausível em saber quantas jogadas
o oponente pode ter. Com esse método de dar valor a jogadas, posso podar a sub árvore que
tenha os maiores valores para o oponente

### Pontos negativos
* Não necessáriamente chega em um contexto mais favorável para a IA


Acho que para ter mais ideias sobre heurísticas preciso conhecer quais são as
estratégias para se ganhar um jogo de damas